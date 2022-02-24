package robotuprising.ftc2021.v3.opmodes

import robotuprising.ftc2021.v3.Hardware
import robotuprising.ftc2021.v3.Rin
import robotuprising.ftc2021.v3.commands.intake.IntakeTurnOffCommand
import robotuprising.ftc2021.v3.commands.intake.IntakeTurnOnCommand
import robotuprising.koawalib.command.group.SequentialCommandGroup
import robotuprising.koawalib.command.scheduler.CommandScheduler
import robotuprising.koawalib.math.MathUtil.radians
import robotuprising.koawalib.math.Pose
import robotuprising.koawalib.path.PurePursuitCommand
import robotuprising.koawalib.path.waypoints.StopWaypoint
import robotuprising.koawalib.path.waypoints.Waypoint
import robotuprising.koawalib.structure.CommandOpMode
import robotuprising.koawalib.structure.OpModeState

class Auto : CommandOpMode() {
    override fun mInit() {
        val hardware = Hardware()
        val rin = Rin(hardware)
        val startPose = Pose()

//        CommandScheduler.scheduleOnceForState(SequentialCommandGroup(
//                { rin.kei.startPose = startPose },
//                PurePursuitCommand(
//                        rin.kei,
//                        Waypoint(0, 0, 0),
//                        Waypoint(0, 16, 8),
//                        Waypoint(16, 16, 8),
//                        StopWaypoint(16, 32, 8, 90.0.radians, 2.0)
//                ).alongWith(IntakeTurnOnCommand(rin.intake))
//        ).andThen(IntakeTurnOffCommand(rin.intake)), OpModeState.LOOP)
    }
}