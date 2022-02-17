package robotuprising.ftc2021.v2.auto.roadrunner.drive.advanced

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.ftc2021.v2.auto.roadrunner.drive.SampleMecanumDrive

/**
 * This opmode explains how you follow multiple trajectories in succession, asynchronously. This
 * allows you to run your own logic beside the drive.update() command. This enables one to run
 * their own loops in the background such as a PID controller for a lift. We can also continuously
 * write our pose to PoseStorage.
 *
 *
 * The use of a State enum and a currentState field constitutes a "finite state machine."
 * You should understand the basics of what a state machine is prior to reading this opmode. A good
 * explanation can be found here:
 * https://www.youtube.com/watch?v=Pu7PMN5NGkQ (A finite state machine introduction tailored to FTC)
 * or here:
 * https://gm0.org/en/stable/docs/software/finite-state-machines.html (gm0's article on FSM's)
 *
 *
 * You can expand upon the FSM concept and take advantage of command based programming, subsystems,
 * state charts (for cyclical and strongly enforced states), etc. There is still a lot to do
 * to supercharge your code. This can be much cleaner by abstracting many of these things. This
 * opmode only serves as an initial starting point.
 */
@Disabled
@Autonomous(group = "advanced")
class AsyncFollowingFSM : LinearOpMode() {
    // This enum defines our "state"
    // This is essentially just defines the possible steps our program will take
    enum class State {
        TRAJECTORY_1, // First, follow a splineTo() trajectory
        TRAJECTORY_2, // Then, follow a lineTo() trajectory
        TURN_1, // Then we want to do a point turn
        TRAJECTORY_3, // Then, we follow another lineTo() trajectory
        WAIT_1, // Then we're gonna wait a second
        TURN_2, // Finally, we're gonna turn again
        IDLE // Our bot will enter the IDLE state when done
    }

    // We define the current state we're on
    // Default to IDLE
    var currentState = State.IDLE

    // Define our start pose
    // This assumes we start at x: 15, y: 10, heading: 180 degrees
    var startPose: Pose2d = Pose2d(15.0, 10.0, Math.toRadians(180.0))
    @Throws(InterruptedException::class)
    override fun runOpMode() {
        // Initialize our lift
        val lift: Lift = Lift(hardwareMap)

        // Initialize SampleMecanumDrive
        val drive = SampleMecanumDrive(hardwareMap)

        // Set inital pose
        drive.poseEstimate = (startPose)

        // Let's define our trajectories
        val trajectory1: Trajectory = drive.trajectoryBuilder(startPose)
            .splineTo(Vector2d(45.0, -20.0), Math.toRadians(90.0))
            .build()

        // Second trajectory
        // Ensure that we call trajectory1.end() as the start for this one
        val trajectory2: Trajectory = drive.trajectoryBuilder(trajectory1.end())
            .lineTo(Vector2d(45.0, 0.0))
            .build()

        // Define the angle to turn at
        val turnAngle1 = Math.toRadians(-270.0)

        // Third trajectory
        // We have to define a new end pose because we can't just call trajectory2.end()
        // Since there was a point turn before that
        // So we just take the pose from trajectory2.end(), add the previous turn angle to it
        val newLastPose: Pose2d = trajectory2.end().plus(Pose2d(0.0, 0.0, turnAngle1))
        val trajectory3: Trajectory = drive.trajectoryBuilder(newLastPose)
            .lineToConstantHeading(Vector2d(-15.0, 0.0))
            .build()

        // Define a 1.5 second wait time
        val waitTime1 = 1.5
        val waitTimer1 = ElapsedTime()

        // Define the angle for turn 2
        val turnAngle2 = Math.toRadians(720.0)
        waitForStart()
        if (isStopRequested()) return

        // Set the current state to TRAJECTORY_1, our first step
        // Then have it follow that trajectory
        // Make sure you use the async version of the commands
        // Otherwise it will be blocking and pause the program here until the trajectory finishes
        currentState = State.TRAJECTORY_1
        drive.followTrajectoryAsync(trajectory1)
        while (opModeIsActive() && !isStopRequested()) {
            // Our state machine logic
            // You can have multiple switch statements running together for multiple state machines
            // in parallel. This is the basic idea for subsystems and commands.

            // We essentially define the flow of the state machine through this switch statement
            when (currentState) {
                State.TRAJECTORY_1 -> // Check if the drive class isn't busy
                    // `isBusy == true` while it's following the trajectory
                    // Once `isBusy == false`, the trajectory follower signals that it is finished
                    // We move on to the next state
                    // Make sure we use the async follow function
                    if (!drive.isBusy) {
                        currentState = State.TRAJECTORY_2
                        drive.followTrajectoryAsync(trajectory2)
                    }
                State.TRAJECTORY_2 -> // Check if the drive class is busy following the trajectory
                    // Move on to the next state, TURN_1, once finished
                    if (!drive.isBusy) {
                        currentState = State.TURN_1
                        drive.turnAsync(turnAngle1)
                    }
                State.TURN_1 -> // Check if the drive class is busy turning
                    // If not, move onto the next state, TRAJECTORY_3, once finished
                    if (!drive.isBusy) {
                        currentState = State.TRAJECTORY_3
                        drive.followTrajectoryAsync(trajectory3)
                    }
                State.TRAJECTORY_3 -> // Check if the drive class is busy following the trajectory
                    // If not, move onto the next state, WAIT_1
                    if (!drive.isBusy) {
                        currentState = State.WAIT_1

                        // Start the wait timer once we switch to the next state
                        // This is so we can track how long we've been in the WAIT_1 state
                        waitTimer1.reset()
                    }
                State.WAIT_1 -> // Check if the timer has exceeded the specified wait time
                    // If so, move on to the TURN_2 state
                    if (waitTimer1.seconds() >= waitTime1) {
                        currentState = State.TURN_2
                        drive.turnAsync(turnAngle2)
                    }
                State.TURN_2 -> // Check if the drive class is busy turning
                    // If not, move onto the next state, IDLE
                    // We are done with the program
                    if (!drive.isBusy) {
                        currentState = State.IDLE
                    }
                State.IDLE -> {}
            }

            // Anything outside of the switch statement will run independent of the currentState

            // We update drive continuously in the background, regardless of state
            drive.update()
            // We update our lift PID continuously in the background, regardless of state
            lift.update()

            // Read pose
            val poseEstimate: Pose2d = drive.poseEstimate

            // Continually write pose to `PoseStorage`
            PoseStorage.currentPose = poseEstimate

            // Print pose to telemetry
            telemetry.addData("x", poseEstimate.x)
            telemetry.addData("y", poseEstimate.y)
            telemetry.addData("heading", poseEstimate.heading)
            telemetry.update()
        }
    }

    // Assume we have a hardware class called lift
    // Lift uses a PID controller to maintain its height
    // Thus, update() must be called in a loop
    internal inner class Lift(hardwareMap: HardwareMap?) {
        fun update() {
            // Beep boop this is the lift update function
            // Assume this runs some PID controller for the lift
        }
    }
}
