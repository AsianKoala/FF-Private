package asiankoala.ftc2021.commands.subsystem

import asiankoala.ftc2021.subsystems.Duck
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.util.Alliance

object DuckCommands {
    class DuckSetSpeedCommand(duck: Duck, speed: Double) : InstantCommand({duck.setSpeed(speed)}, duck)
    class DuckSpinSequence(duck: Duck, alliance: Alliance) : SequentialCommandGroup(
            DuckSetSpeedCommand(duck, alliance.decide(0.3, -0.3)),
            WaitCommand(2.0),
            DuckSetSpeedCommand(duck, alliance.decide(1.0, -1.0)),
            WaitCommand(0.5),
            DuckSetSpeedCommand(duck, 0.0)
    )
}