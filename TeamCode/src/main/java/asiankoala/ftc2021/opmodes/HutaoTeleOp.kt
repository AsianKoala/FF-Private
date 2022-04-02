package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.*
import asiankoala.ftc2021.subsystems.Turret
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.subsystem.drive.MecanumDriveCommand
import com.asiankoala.koawalib.util.Logger
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class HutaoTeleOp : CommandOpMode() {
    private lateinit var hutao: Hutao
    override fun mInit() {
        hutao = Hutao()

        hutao.drive.setDefaultCommand(MecanumDriveCommand(
                hutao.drive,
                driver.leftStick,
                driver.rightStick,
                1.0, 1.0, 1.0,
                xScalar = 0.7, yScalar = 0.7, rScalar = 0.7
        ))

        driver.rightTrigger.onPress(IntakeSequenceCommand(hutao.intake,
                hutao.outtake, hutao.indexer, hutao.turret, Turret.turretBlueAngle, hutao.arm))

        driver.leftTrigger.onPress(
                SequentialCommandGroup(
                        DepositCommand(hutao.slides, hutao.indexer) { driver.rightBumper.invokeBoolean() },
                        WaitCommand(0.5),
                        ResetAfterDepositCommand(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer, hutao.arm)
                )
        )
    }

    override fun mStart() {
        hutao.turret.disabled = false
        hutao.slides.disabled = false
        hutao.turret.setPIDTarget(180.0)
        hutao.slides.setPIDTarget(0.0)
    }

    override fun mLoop() {
        hutao.imu.periodic()
        Logger.addTelemetryData("power", hutao.drive.powers)
        Logger.addTelemetryData("position", hutao.drive.position)
        Logger.addTelemetryData("turret angle", hutao.turretEncoder.position)
        Logger.addTelemetryData("slides inches", hutao.slideEncoder.position)
    }
}