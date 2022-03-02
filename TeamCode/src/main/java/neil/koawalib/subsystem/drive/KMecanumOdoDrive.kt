package neil.koawalib.subsystem.drive

import com.acmerobotics.dashboard.config.Config
import neil.koawalib.hardware.motor.KMotor
import neil.koawalib.subsystem.odometry.OdoConfig
import neil.koawalib.subsystem.odometry.Odometry

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