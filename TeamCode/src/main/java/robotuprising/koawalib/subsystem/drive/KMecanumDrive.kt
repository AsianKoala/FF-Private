package robotuprising.koawalib.subsystem.drive

import robotuprising.koawalib.hardware.KMotor
import robotuprising.koawalib.math.Pose
import robotuprising.koawalib.subsystem.Subsystem
import kotlin.math.absoluteValue

open class KMecanumDrive(
        fl: KMotor,
        bl: KMotor,
        fr: KMotor,
        br: KMotor
) : Subsystem {
    private val motors = listOf(fl, bl, fr, br)

    var powers = Pose()

    var disabled = false

    var driveState = DriveStates.DISABLED

    fun stop() {
        driveState = DriveStates.DISABLED
        powers = Pose()
        motors.forEach { it.power = 0.0 }
        disabled = true
    }

    protected fun processPowers() {
        if(!disabled) {
            val fl = powers.y + powers.x + powers.h
            val bl = powers.y - powers.x + powers.h
            val fr = powers.y - powers.x - powers.h
            val br = powers.y + powers.x - powers.h

            val wheels= listOf(fl, bl, fr, br)
            val absMax = wheels.maxOf { it.absoluteValue }
            val scalar = if(absMax > 1.0) absMax else 1.0
            motors.forEachIndexed { i, it -> it.power = wheels[i] / scalar }
        }
    }

    override fun periodic() {
        when(driveState) {
            DriveStates.DISABLED -> {
                powers = Pose()
            }

            DriveStates.MANUAL -> {
                // expect manual powers being set externally
            }

            else -> {
                // assuming drive doesn't have localization
                driveState = DriveStates.DISABLED
            }
        }

        processPowers()
    }

}