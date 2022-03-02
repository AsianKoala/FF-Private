package neil.ftc21.v2.auto.roadrunner.sim

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark
import com.noahbres.meepmeep.roadrunner.DriveShim
import neil.lib.math.MathUtil.radians

object TurretPath {
    @JvmStatic
    fun main(args: Array<String>) {
        val deposit = Vector2d(8.0, -43.0)
        val intakeFwd = Vector2d(48.0, -48.0)
        val intakeExit = Vector2d(36.0, -43.0)

        val mm = MeepMeep(800) // Set field image
                .setBackground(MeepMeep.Background.FIELD_FREIGHT_FRENZY) // Set theme
                .setTheme(ColorSchemeBlueDark()) // Background opacity from 0-1
                .setBackgroundAlpha(1f) // Set constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
                .setBotDimensions(11.8, 18.0)
                .followTrajectorySequence { drive: DriveShim ->
                    drive.trajectorySequenceBuilder(Pose2d(9.0, 63.0, 90.0.radians))
                            // initial
                            .setReversed(true)
                            .splineToSplineHeading(Pose2d(-1.0, 38.0, 55.0.radians), 240.0.radians)

                            // go to warehouse
                            .setReversed(false)
                            .splineToSplineHeading(Pose2d(15.0, 63.0, 0.0), 0.0)
                            .splineToConstantHeading(Vector2d(51.0, 63.0), 0.0)


                            .setReversed(true)
                            .splineToConstantHeading(Vector2d(15.0, 63.0), 180.0.radians)
                            .splineToSplineHeading(Pose2d(-1.0, 38.0, 55.0.radians), 240.0.radians)





                            .build()
                }
                .start()
    }
}
//                            .setReversed(false)
//                            .splineToSplineHeading(Pose2d(20.0, 63.0, 0.0), 0.0)
//                            .splineToConstantHeading(Vector2d(60.0, 63.0), 0.0)
//                            .setReversed(true)
//                            .splineToConstantHeading(Vector2d(20.0, 63.0), 180.0.radians)
//                            .splineToSplineHeading(Pose2d(-1.0, 38.0, 55.0.radians), 240.0.radians)
