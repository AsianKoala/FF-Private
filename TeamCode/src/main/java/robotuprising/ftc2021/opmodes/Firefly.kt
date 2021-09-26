package robotuprising.ftc2021.opmodes

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import org.openftc.revextensions2.ExpansionHubMotor

@TeleOp
class Firefly : OpMode() {

    lateinit var driveTrain: DriveTrain

    override fun init() {
        driveTrain = DriveTrain(
            hardwareMap[ExpansionHubMotor::class.java, "FL"],
            hardwareMap[ExpansionHubMotor::class.java, "FR"],
            hardwareMap[ExpansionHubMotor::class.java, "BL"],
            hardwareMap[ExpansionHubMotor::class.java, "BR"]
        )
    }

    override fun loop() {
        driveTrain.update()
        teleopControl(0.25)
    }

    fun teleopControl(driveScale: Double) {
        driveTrain.powers = Pose(
            Point(
                gamepad1.left_stick_x * driveScale,
                -gamepad1.left_stick_y * driveScale
            ),
            Angle(-gamepad1.right_stick_x * driveScale, AngleUnit.RAW)
        )
    }
}
