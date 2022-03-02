package neil.ftc21.v2.auto.roadrunner.sim

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.MeepMeep.Background
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark
import com.noahbres.meepmeep.roadrunner.DriveShim
import kotlin.math.PI

object MeepMeepTesting {
    @JvmStatic
    fun main(args: Array<String>) {
        // System.setProperty("sun.java2d.opengl", "true");

        val firstDeposit = Pose2d(8.0, -43.0, PI - PI / 4)
        val deposit = Pose2d(6.0, -36.0, Math.toRadians(145.0))
        val intakeFwd = Pose2d(52.0, -52.0, 0.0)
        val exit = Pose2d(38.0, -43.0, PI)

        val mm = MeepMeep(800) // Set field image
            .setBackground(Background.FIELD_FREIGHT_FRENZY) // Set theme
            .setTheme(ColorSchemeBlueDark()) // Background opacity from 0-1
            .setBackgroundAlpha(1f) // Set constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
            .followTrajectorySequence { drive: DriveShim ->
                drive.trajectorySequenceBuilder(Pose2d(8.0, -60.0, PI / 2))
                        .lineToLinearHeading(firstDeposit)
                        .waitSeconds(0.5)
                        .turn(PI / 4)
                        .setReversed(true)
                        .splineToSplineHeading(intakeFwd, - PI / 3)
                        .setReversed(false)
                        .splineToSplineHeading(exit, PI)
                        .splineToSplineHeading(deposit, Math.toRadians(145.0))
                        .waitSeconds(0.5)
//                            .setReversed(true)
//                            .splineToSplineHeading(intakeFwd, Math.toRadians(-60.0))
//                            .setReversed(false)
//                            .splineToSplineHeading(deposit, Math.toRadians(145.0))
//                            .waitSeconds(0.5)
//
//                            .setReversed(true)
//                            .splineToSplineHeading(intakeFwd, Math.toRadians(-60.0))
//                            .setReversed(false)
//                            .splineToSplineHeading(deposit, Math.toRadians(145.0))
//                            .waitSeconds(0.5)
//
//                            .setReversed(true)
//                            .splineToSplineHeading(intakeFwd, Math.toRadians(-60.0))
//                            .setReversed(false)
//                            .splineToSplineHeading(deposit, Math.toRadians(145.0))
//                            .waitSeconds(0.5)
//
//                            .setReversed(true)
//                            .splineToSplineHeading(intakeFwd, Math.toRadians(-60.0))
//                            .setReversed(false)
//                            .splineToSplineHeading(deposit, Math.toRadians(145.0))
//                            .waitSeconds(0.5)
//
//                            .setReversed(true)
//                            .splineToSplineHeading(intakeFwd, Math.toRadians(-60.0))
//                            .setReversed(false)
//                            .splineToSplineHeading(deposit, Math.toRadians(145.0))
//                            .waitSeconds(0.5)
                    .build()
            }
            .start()
    }
}
