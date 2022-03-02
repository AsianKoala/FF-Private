package neil.ftc21.v3.subsystems

import neil.koawalib.hardware.motor.KMotor
import neil.koawalib.subsystem.drive.KMecanumOdoDrive
import neil.koawalib.subsystem.odometry.OdoConfig

class Drive(
        fl: KMotor,
        bl: KMotor,
        fr: KMotor,
        br: KMotor,
        odoConfig: OdoConfig
) : KMecanumOdoDrive(fl, bl, fr, br, odoConfig)
