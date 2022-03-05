package asiankoala.ftc2021.commands

import asiankoala.ftc2021.subsystems.Indexer
import asiankoala.ftc2021.subsystems.Intake
import asiankoala.ftc2021.subsystems.Outtake
import com.asiankoala.koawalib.command.commands.CommandBase
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup

object IntakeCommands {
    class IntakeSmartCommand(private val intake: Intake) : CommandBase() {
        override fun init() {
            intake.startReading()
            intake.turnOn()
        }

        override fun execute() {
        }

        override fun end(interrupted: Boolean) {
            intake.stopReading()
            intake.turnOff()
        }

        override val isFinished: Boolean
            get() = intake.isMineralIn

        init {
            addRequirements(intake)
        }
    }

    class IntakeOn(private val intake: Intake) : InstantCommand(intake::turnOn, intake)
    class IntakeOff(private val intake: Intake) : InstantCommand(intake::turnOff, intake)
    class IntakeReverse(private val intake: Intake) : InstantCommand(intake::turnReverse, intake)

    class IntakeSequenceCommand(
        intake: Intake,
        indexer: Indexer,
        outtake: Outtake
    ) : SequentialCommandGroup(
        IntakeSmartCommand(intake),
        IndexerCommands.Lock(indexer)
            .alongWith(OuttakeCommands.Deposit(outtake))
    )
}
