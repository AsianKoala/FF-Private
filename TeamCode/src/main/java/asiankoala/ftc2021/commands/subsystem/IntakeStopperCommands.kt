package asiankoala.ftc2021.commands.subsystem

import asiankoala.ftc2021.subsystems.IntakeStopper
import com.asiankoala.koawalib.command.commands.InstantCommand

object IntakeStopperCommands {
    class IntakeStopperUnlockCommand(intakeStopper: IntakeStopper) : InstantCommand(intakeStopper::unlock, intakeStopper)
    class IntakeStopperLockCommand(intakeStopper: IntakeStopper) : InstantCommand(intakeStopper::lock, intakeStopper)
}