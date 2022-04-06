package jj

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder
import com.noahbres.meepmeep.roadrunner.DriveShim


object MeepMeep {
    @JvmStatic
    fun main(args: Array<String>) {
        val meepMeep = MeepMeep(800)
        val myBot = DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(30.0, 30.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
                .setDimensions(11.8, 18.0)
                .followTrajectorySequence { drive: DriveShim ->
                    drive.trajectorySequenceBuilder(Pose2d(6.0, 64.0, 0.0))
                            .lineTo(Vector2d(56.0, 64.0))
                            .build()
                }
        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start()
    }
}
