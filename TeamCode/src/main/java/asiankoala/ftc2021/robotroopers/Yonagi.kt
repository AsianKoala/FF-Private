package asiankoala.ftc2021.robotroopers

import asiankoala.ftc2021.robotroopers.subsystems.Drive
import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.subsystem.odometry.OdoConfig

class Yonagi {
    val fl = KMotor("FL").reverse
    val fr = KMotor("FR")
    val bl = KMotor("BL").reverse
    val br = KMotor("BR")

    val drive = Drive(fl, fr, bl , br, OdoConfig(
        0.0, 0.0, 0.0, fl, fl, fl,
    ))
}
