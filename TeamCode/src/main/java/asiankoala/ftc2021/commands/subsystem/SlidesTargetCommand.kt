package asiankoala.ftc2021.commands.subsystem

import asiankoala.ftc2021.subsystems.Slides
import com.asiankoala.koawalib.command.commands.InstantCommand

class SlidesTargetCommand(slides: Slides, target: Double) : InstantCommand({slides.generateAndFollowMotionProfile(target)}, slides)