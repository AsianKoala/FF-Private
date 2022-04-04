package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.*
import asiankoala.ftc2021.subsystems.Turret
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.CommandScheduler
import com.asiankoala.koawalib.command.commands.GoToPointCommand
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.MecanumDriveCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.radians
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.util.Logger
import com.asiankoala.koawalib.util.LoggerConfig
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class HutaoTeleOp : CommandOpMode() {
    private lateinit var hutao: Hutao

    override fun mInit() {
        Logger.config = LoggerConfig(isLogging = true, isPrinting = false, isLoggingTelemetry = false, isDebugging = false, maxErrorCount = 1)
        hutao = Hutao()
        hutao.drive.setStartPose(Pose(0.0, 0.0, heading = 90.0.radians))
        hutao.drive.setDefaultCommand(MecanumDriveCommand(
                hutao.drive,
                driver.leftStick,
                driver.rightStick,
                1.0, 1.0, 1.0,
                xScalar = 0.7, yScalar = 0.7, rScalar = 0.7
        ))

        driver.rightTrigger.onPress(IntakeSequenceCommand(hutao.intake,
                hutao.outtake, hutao.indexer, hutao.turret, Turret.turretBlueAngle, hutao.arm))

        val depositSequence = SequentialCommandGroup(
                DepositCommand(hutao.slides, hutao.indexer) { driver.leftTrigger.invokeBoolean() },
                WaitCommand(0.5),
                ResetAfterDepositCommand(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer, hutao.arm)
        )

        CommandScheduler.scheduleWatchdog({ driver.leftTrigger.isJustPressed && !depositSequence.isScheduled}, depositSequence)

        driver.rightBumper.onPress(
                GoToPointCommand(
                        hutao.drive,
                        Pose(24.0, 24.0, 45.0.radians),
                        2.0,
                        2.0.radians,
                        true,
                        0.8,
                        0.8,
                        shouldTelemetry = false
                )
        )

        driver.rightBumper.onPress(DuckCommands.DuckSpinSequence(hutao.duck, Alliance.BLUE))
    }

    override fun mStart() {
        hutao.turret.setPIDTarget(180.0)
        hutao.slides.setPIDTarget(0.0)
    }

    override fun mLoop() {
        Logger.addTelemetryData("power", hutao.drive.powers)
        Logger.addTelemetryData("position", hutao.drive.position)
        Logger.addTelemetryData("turret angle", hutao.turretEncoder.position)
        Logger.addTelemetryData("slides inches", hutao.slideEncoder.position)
    }
}