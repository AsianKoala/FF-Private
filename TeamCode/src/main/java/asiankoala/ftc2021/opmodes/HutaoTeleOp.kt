package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.IndexerCommands
import asiankoala.ftc2021.commands.IntakeCommands
import asiankoala.ftc2021.commands.OuttakeCommands
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.subsystem.drive.MecanumDriveCommand
import com.asiankoala.koawalib.util.Logger
import com.asiankoala.koawalib.util.LoggerConfig
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class HutaoTeleOp : CommandOpMode() {
    private lateinit var hutao: Hutao
    override fun mInit() {
        Logger.config = LoggerConfig(
                isLogging = true,
                isPrinting = false,
                isLoggingTelemetry = false,
                isDebugging = true,
                maxErrorCount = 1
        )
        hutao = Hutao()

        hutao.drive.setDefaultCommand(MecanumDriveCommand(
                hutao.drive,
                driver.leftStick,
                driver.rightStick.xInverted.yInverted,
                1.0, 1.0, 1.0,
                xScalar = 0.7, yScalar = 0.7, rScalar = 0.7
        ))

        driver.rightTrigger.onPress(IntakeCommands.IntakeTurnOnCommand(hutao.intake))
        driver.leftTrigger.onPress(IntakeCommands.IntakeTurnOffCommand(hutao.intake))
        driver.rightBumper.onPress(IntakeCommands.IntakeTurnReverseCommand(hutao.intake))

//        driver.rightTrigger.onPress(
//                SequentialCommandGroup(
//                        OuttakeCommands.OuttakeHomeCommand(hutao.outtake)
//                                .alongWith(IndexerCommands.IndexerOpenCommand(hutao.indexer)),
//                        WaitCommand(0.2),
//                        IntakeCommands.IntakeTurnOnCommand(hutao.intake)
//                                .alongWith(InstantCommand(hutao.intake::startReading)),
//                        WaitUntilCommand(hutao.intake::hasMineral),
//                        IntakeCommands.IntakeTurnReverseCommand(hutao.intake),
//                        IndexerCommands.IndexerLockCommand(hutao.indexer),
//                        WaitCommand(0.5),
//                        OuttakeCommands.OuttakeCockCommand(hutao.outtake)
//                )
//        )
    }

    override fun mLoop() {
        Logger.addTelemetryData("power", hutao.drive.powers)
        Logger.addTelemetryData("position", hutao.drive.position)
    }
}