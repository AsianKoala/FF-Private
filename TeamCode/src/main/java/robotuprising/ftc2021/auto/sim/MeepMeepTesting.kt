package robotuprising.ftc2021.auto.sim

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.MeepMeep.Background
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark
import com.noahbres.meepmeep.roadrunner.DriveShim
import robotuprising.ftc2021.auto.trajectorysequence.TrajectorySequenceBuilder
import kotlin.math.PI

object MeepMeepTesting {
    @JvmStatic
    fun main(args: Array<String>) {
        // TODO: If you experience poor performance, enable this flag
        // System.setProperty("sun.java2d.opengl", "true");

        val deposit = Pose2d(8.0, -43.0, PI - PI / 4)
        val intakeFwd = Pose2d(52.0, -52.0, PI - PI / 4)

        val mm = MeepMeep(800) // Set field image
                .setBackground(Background.FIELD_FREIGHT_FRENZY) // Set theme
                .setTheme(ColorSchemeBlueDark()) // Background opacity from 0-1
                .setBackgroundAlpha(1f) // Set constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)

                .followTrajectorySequence { drive: DriveShim ->
                    drive.trajectorySequenceBuilder(Pose2d(8.0, -60.0, PI / 2))
                            .lineToLinearHeading(deposit)
                            .waitSeconds(0.5)

                            .turn(PI / 4)
                            .setReversed(true)
                            .splineToSplineHeading(intakeFwd, - PI / 4)
                            .setReversed(false)
                            .splineToSplineHeading(Pose2d(deposit.vec(), PI), PI)
                            .turn(-PI / 4)
                            .waitSeconds(0.5)

                            .turn(PI / 4)
                            .setReversed(true)
                            .splineToSplineHeading(intakeFwd, - PI / 4)
                            .setReversed(false)
                            .splineToSplineHeading(Pose2d(deposit.vec(), PI), PI)
                            .turn(-PI / 4)
                            .waitSeconds(0.5)

                            .turn(PI / 4)
                            .setReversed(true)
                            .splineToSplineHeading(intakeFwd, - PI / 4)
                            .setReversed(false)
                            .splineToSplineHeading(Pose2d(deposit.vec(), PI), PI)
                            .turn(-PI / 4)
                            .waitSeconds(0.5)

                            .turn(PI / 4)
                            .setReversed(true)
                            .splineToSplineHeading(intakeFwd, - PI / 4)
                            .setReversed(false)
                            .splineToSplineHeading(Pose2d(deposit.vec(), PI), PI)
                            .turn(-PI / 4)
                            .waitSeconds(0.5)


                            .build()
            }
            .start()
    }
}
