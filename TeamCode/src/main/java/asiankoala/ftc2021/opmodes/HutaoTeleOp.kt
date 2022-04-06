package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.sequences.teleop.DepositAllianceSequence
import asiankoala.ftc2021.commands.sequences.teleop.DepositSharedSequence
import asiankoala.ftc2021.commands.sequences.teleop.HomeSequence
import asiankoala.ftc2021.commands.sequences.teleop.IntakeSequence
import asiankoala.ftc2021.commands.subsystem.DuckCommands
import asiankoala.ftc2021.subsystems.Turret
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.CommandScheduler
import com.asiankoala.koawalib.command.commands.MecanumDriveCommand
import com.asiankoala.koawalib.command.commands.PathCommand
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
class HutaoTeleOp(private val alliance: Alliance) : CommandOpMode() {
    private lateinit var hutao: Hutao

    override fun mInit() {
        Logger.config = LoggerConfig(isLogging = true, isPrinting = false, isLoggingTelemetry = false, isDebugging = false, maxErrorCount = 1)
        hutao = Hutao(Pose(heading = 90.0.radians))
        bindDrive()
        bindIntake()
        bindDeposit()
        bindDuck()
    }

    private fun bindDrive() {
        hutao.drive.setDefaultCommand(MecanumDriveCommand(
                hutao.drive,
                driver.leftStick,
                driver.rightStick,
                1.0, 1.0, 1.0,
                xScalar = 0.7, yScalar = 0.7, rScalar = 0.7
        ))
    }

    private fun bindIntake() {
        driver.rightTrigger.onPress(IntakeSequence(alliance, hutao.intake,
                hutao.outtake, hutao.indexer, hutao.turret, hutao.arm))
    }

    private fun bindDeposit() {
        val depositAlliance = SequentialCommandGroup(
                DepositAllianceSequence(hutao.slides, hutao.indexer) { driver.leftTrigger.invokeBoolean() },
                HomeSequence(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer, hutao.arm, hutao.encoders.slideEncoder)
        )

        CommandScheduler.scheduleWatchdog({ driver.leftTrigger.isJustPressed && !depositAlliance.isScheduled}, depositAlliance)

        val sharedCommand = DepositSharedSequence(alliance, hutao.turret, hutao.arm, hutao.indexer,
                hutao.slides, driver.rightBumper, hutao.intake, hutao.outtake, hutao.encoders.slideEncoder)
        CommandScheduler.scheduleWatchdog({ driver.rightBumper.isJustPressed && hutao.intake.hasMineral && !sharedCommand.isScheduled }, sharedCommand)
    }

    private fun bindDuck() {
        driver.leftBumper.onPress(DuckCommands.DuckSpinSequence(hutao.duck, Alliance.BLUE))
    }

    override fun mLoop() {
        Logger.addTelemetryData("power", hutao.drive.powers.rawString())
        Logger.addTelemetryData("position", hutao.drive.position)
        Logger.addTelemetryData("turret angle", hutao.encoders.turretEncoder.position)
        Logger.addTelemetryData("slides inches", hutao.encoders.slideEncoder.position)

        Logger.logInfo("power: ${hutao.drive.powers.rawString()}")
        Logger.logInfo("position: ${hutao.drive.position}")
    }
}
