package robotuprising.koawalib.subsystem.drive

import com.acmerobotics.dashboard.config.Config
import robotuprising.koawalib.hardware.motor.KMotor
import robotuprising.koawalib.subsystem.odometry.OdoConfig
import robotuprising.koawalib.subsystem.odometry.Odometry

@Config
open class KMecanumOdoDrive(
        fl: KMotor,
        bl: KMotor,
        fr: KMotor,
        br: KMotor,
        config: OdoConfig
) : KMecanumDrive(fl, bl, fr, br) {

    private val odometry = Odometry(config)

    val position get() = odometry.position
    val velocity get() = odometry.velocity

    override fun periodic() {
        super.periodic()
        odometry.localize()
    }
}