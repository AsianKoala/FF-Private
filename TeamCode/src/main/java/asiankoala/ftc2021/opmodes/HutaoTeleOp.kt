package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.*
import asiankoala.ftc2021.subsystems.Turret
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.CommandScheduler
import com.asiankoala.koawalib.command.commands.GoToPointCommand
import com.asiankoala.koawalib.command.commands.MecanumDriveCommand
import com.asiankoala.koawalib.command.commands.PathCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.radians
import com.asiankoala.koawalib.path.Path
import com.asiankoala.koawalib.path.Waypoint
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
        bindDrive()
        bindIntake()
        bindDeposit()
        bindDuck()
        bindPath()
        ready()
    }

    private fun bindDrive() {
        hutao.drive.setStartPose(Pose(0.0, 0.0, heading = 90.0.radians))
        hutao.drive.setDefaultCommand(MecanumDriveCommand(
                hutao.drive,
                driver.leftStick,
                driver.rightStick,
                1.0, 1.0, 1.0,
                xScalar = 0.7, yScalar = 0.7, rScalar = 0.7
        ))
    }

    private fun bindIntake() {
        val intakeSequence = IntakeSequenceCommand(hutao.intake,
                hutao.outtake, hutao.indexer, hutao.turret, Turret.turretBlueAngle, hutao.arm) { driver.rightTrigger.invokeBoolean() }

        CommandScheduler.scheduleWatchdog({ driver.rightTrigger.isJustPressed && !intakeSequence.isScheduled}, intakeSequence)
    }

    private fun bindDeposit() {
        val depositSequence = SequentialCommandGroup(
                DepositCommand(hutao.slides, hutao.indexer) { driver.leftTrigger.invokeBoolean() },
                WaitCommand(0.5),
                ResetAfterDepositCommand(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer, hutao.arm, hutao.encoders.slideEncoder)
        )

        CommandScheduler.scheduleWatchdog({ driver.leftTrigger.isJustPressed && !depositSequence.isScheduled}, depositSequence)
    }

    private fun bindDuck() {
        driver.leftBumper.onPress(DuckCommands.DuckSpinSequence(hutao.duck, Alliance.BLUE))
    }

    private fun bindPath() {
        val waypoints = listOf(
                Waypoint(0.0, 0.0, 0.0),
                Waypoint(12.0, 30.0, 14.0),
                Waypoint(24.0, 36.0, 14.0, deccelAngle = 40.0.radians),
                Waypoint(52.0, 36.0, 14.0, headingLockAngle = 0.0, lowestSlowDownFromTurnError = 0.2, deccelAngle = 40.0.radians)
        )
        val path = Path(waypoints)
        driver.rightBumper.onPress(PathCommand(hutao.drive, path, 2.0))
    }

    private fun ready() {
        hutao.turret.setPIDTarget(180.0)
        hutao.slides.setPIDTarget(0.0)
        hutao.turret.disabled = false
        hutao.slides.disabled = false
//        hutao.drive.unregister()
    }

    override fun mLoop() {
        Logger.addTelemetryData("power", hutao.drive.powers.rawString())
        Logger.addTelemetryData("position", hutao.drive.position)
        Logger.addTelemetryData("turret angle", hutao.encoders.turretEncoder.position)
        Logger.addTelemetryData("slides inches", hutao.encoders.slideEncoder.position)
    }
}
