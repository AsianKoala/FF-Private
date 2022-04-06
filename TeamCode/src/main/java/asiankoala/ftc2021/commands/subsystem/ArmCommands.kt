package asiankoala.ftc2021.commands.subsystem

import asiankoala.ftc2021.subsystems.Arm
import com.asiankoala.koawalib.command.commands.InstantCommand

object ArmCommands {
    class ArmHomeCommand(arm: Arm) : InstantCommand(arm::home, arm)
    class ArmDepositHighCommand(arm: Arm) : InstantCommand(arm::depositHigh, arm)
    class ArmDepositSharedCommand(arm: Arm) : InstantCommand(arm::depositShared, arm)
}