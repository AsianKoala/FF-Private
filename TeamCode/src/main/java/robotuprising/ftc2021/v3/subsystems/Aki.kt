package robotuprising.ftc2021.v3.subsystems

import robotuprising.ftc2021.v2.auto.pp.PurePursuitPath
import robotuprising.koawalib.hardware.KMotor
import robotuprising.koawalib.math.Pose
import robotuprising.koawalib.subsystem.Subsystem

class Aki(
        private val fl: KMotor,
        private val bl: KMotor,
        private val fr: KMotor,
        private val br: KMotor
) : Subsystem {
    private val motors = listOf(fl, bl, fr, br)

    var powers = Pose()

    var driveState = DriveStates.DISABLED
    enum class DriveStates {
        DISABLED,
        MANUAL,
        PATH,
        TARGET_POINT
    }

}