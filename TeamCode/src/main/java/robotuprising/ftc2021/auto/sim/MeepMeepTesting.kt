package robotuprising.ftc2021.auto.sim

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.MeepMeep.Background
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark
import com.noahbres.meepmeep.roadrunner.DriveShim

object MeepMeepTesting {
    @JvmStatic
    fun main(args: Array<String>) {
        // TODO: If you experience poor performance, enable this flag
        // System.setProperty("sun.java2d.opengl", "true");

        // Declare a MeepMeep instance
        // With a field size of 800 pixels
        val mm = MeepMeep(800) // Set field image
            .setBackground(Background.FIELD_ULTIMATE_GOAL_DARK) // Set theme
            .setTheme(ColorSchemeRedDark()) // Background opacity from 0-1
            .setBackgroundAlpha(1f) // Set constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
            .followTrajectorySequence { drive: DriveShim ->
                drive.trajectorySequenceBuilder(Pose2d(0.0, 0.0, 0.0))
                    .forward(30.0)
                    .turn(Math.toRadians(90.0))
                    .forward(30.0)
                    .turn(Math.toRadians(90.0))
                    .forward(30.0)
                    .turn(Math.toRadians(90.0))
                    .forward(30.0)
                    .turn(Math.toRadians(90.0))
                    .build()
            }
            .start()
    }
}
