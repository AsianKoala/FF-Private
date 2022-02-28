package robotuprising.ftc2021.v3.commands

import robotuprising.ftc2021.v3.subsystems.Slides
import robotuprising.koawalib.command.commands.CommandBase

object SlideCommands {
    open class SlidesCommand(private val slides: Slides, private val angle: Double) : CommandBase() {
        override fun execute() {
            slides.setSlideInches(angle)
        }

        override val isFinished: Boolean
            get() = slides.isAtTarget

        init {
            addRequirements(slides)
        }
    }

    class Home(slides: Slides) :  SlidesCommand(slides, Slides.homeInches)
    class Shared(slides: Slides) : SlidesCommand(slides, Slides.sharedInches)
    class AllianceHigh(slides: Slides) : SlidesCommand(slides, Slides.allianceHighInches)
    class AllianceMid(slides: Slides) : SlidesCommand(slides, Slides.allianceMidInches)
    class AllianceLow(slides: Slides) : SlidesCommand(slides, Slides.allianceLowInches)
}