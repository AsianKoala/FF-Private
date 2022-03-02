package neil.ftc21.v3.commands

import neil.ftc21.v3.subsystems.Pitch
import neil.koawalib.command.commands.CommandBase

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

    class Home(pitch: Pitch) :  PitchCommand(pitch, Pitch.homeAngle)
    class Shared(pitch: Pitch) : PitchCommand(pitch, Pitch.sharedAngle)
    class AllianceHigh(pitch: Pitch) : PitchCommand(pitch, Pitch.allianceHighAngle)
    class AllianceMid(pitch: Pitch) : PitchCommand(pitch, Pitch.allianceMidAngle)
    class AllianceLow(pitch: Pitch) : PitchCommand(pitch, Pitch.allianceLowAngle)
}