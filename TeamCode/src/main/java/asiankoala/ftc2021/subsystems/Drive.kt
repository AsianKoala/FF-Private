package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.subsystem.drive.KMecanumOdoDrive
import com.asiankoala.koawalib.subsystem.odometry.OdoConfig

class Drive(
    fl: KMotor,
    bl: KMotor,
    fr: KMotor,
    br: KMotor,
    odoConfig: OdoConfig
) : KMecanumOdoDrive(fl, bl, fr, br, odoConfig)
