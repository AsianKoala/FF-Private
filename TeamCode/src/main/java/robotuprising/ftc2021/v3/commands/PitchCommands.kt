package robotuprising.ftc2021.v3.commands

import robotuprising.ftc2021.v3.subsystems.Pitch
import robotuprising.koawalib.command.commands.CommandBase

object PitchCommands {
    open class PitchCommand(private val pitch: Pitch, private val angle: Double) : CommandBase() {
        override fun execute() {
            pitch.setPitchAngle(angle)
        }

        override val isFinished: Boolean
            get() = pitch.isAtTarget

        init {
            addRequirements(pitch)
        }
    }

    class HomeCommand(pitch: Pitch) :  PitchCommand(pitch, Pitch.homeAngle)
    class SharedCommand(pitch: Pitch) : PitchCommand(pitch, Pitch.sharedAngle)
    class AllianceHighCommand(pitch: Pitch) : PitchCommand(pitch, Pitch.allianceHighAngle)
    class AllianceMidCommand(pitch: Pitch) : PitchCommand(pitch, Pitch.allianceMidAngle)
    class AllianceLowCommand(pitch: Pitch) : PitchCommand(pitch, Pitch.allianceLowAngle)
}