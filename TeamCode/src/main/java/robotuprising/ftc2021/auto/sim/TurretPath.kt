package robotuprising.ftc2021.auto.sim

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark
import com.noahbres.meepmeep.roadrunner.DriveShim
import com.noahbres.meepmeep.roadrunner.DriveTrainType
import kotlin.math.PI

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
                .setDriveTrainType(DriveTrainType.TANK)
                .followTrajectorySequence { drive: DriveShim ->
                    drive.trajectorySequenceBuilder(Pose2d(8.0, -60.0, PI / 2))
                            .splineTo(deposit, Math.toRadians(0.0))

                            .splineTo(intakeFwd, Math.toRadians(-20.0))
                            .setReversed(true)
                            .splineTo(intakeExit, Math.toRadians(180.0))
                            .lineTo(deposit)
                            .setReversed(false)

                            .splineTo(intakeFwd, Math.toRadians(-20.0))
                            .setReversed(true)
                            .splineTo(intakeExit, Math.toRadians(180.0))
                            .lineTo(deposit)
                            .setReversed(false)

                            .splineTo(intakeFwd, Math.toRadians(-20.0))
                            .setReversed(true)
                            .splineTo(intakeExit, Math.toRadians(180.0))
                            .lineTo(deposit)
                            .setReversed(false)

                            .splineTo(intakeFwd, Math.toRadians(-20.0))
                            .setReversed(true)
                            .splineTo(intakeExit, Math.toRadians(180.0))
                            .lineTo(deposit)
                            .setReversed(false)

                            .splineTo(intakeFwd, Math.toRadians(-20.0))
                            .setReversed(true)
                            .splineTo(intakeExit, Math.toRadians(180.0))
                            .lineTo(deposit)
                            .setReversed(false)

                            .splineTo(intakeFwd, Math.toRadians(-20.0))
                            .setReversed(true)
                            .splineTo(intakeExit, Math.toRadians(180.0))
                            .lineTo(deposit)
                            .setReversed(false)

                            .splineTo(intakeFwd, Math.toRadians(-20.0))
                            .setReversed(true)
                            .splineTo(intakeExit, Math.toRadians(180.0))
                            .lineTo(deposit)
                            .setReversed(false)

                            .splineTo(intakeFwd, Math.toRadians(-20.0))
                            .setReversed(true)
                            .splineTo(intakeExit, Math.toRadians(180.0))
                            .lineTo(deposit)
                            .setReversed(false)

//                            .splineTo(intakeFwd, Math.toRadians(-20.0))
//                            .setReversed(true)
//                            .splineTo(intakeExit, Math.toRadians(180.0))
//                            .lineTo(deposit)
//                            .setReversed(false)

//                            .splineTo(intakeFwd, Math.toRadians(-20.0))
//                            .setReversed(true)
//                            .splineTo(intakeExit, Math.toRadians(180.0))
//                            .lineTo(deposit)
//                            .setReversed(false)

                            .splineTo(intakeExit, 0.0)
                            .build()
                }
                .start()

    }
}
