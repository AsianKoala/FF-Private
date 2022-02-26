package robotuprising.ftc2021.v3.subsystems

import robotuprising.koawalib.hardware.motor.KMotor
import robotuprising.koawalib.subsystem.drive.KMecanumOdoDrive
import robotuprising.koawalib.subsystem.odometry.OdoConfig

class Kei(
        fl: KMotor,
        bl: KMotor,
        fr: KMotor,
        br: KMotor,
        odoConfig: OdoConfig
) : KMecanumOdoDrive(fl, bl, fr, br, odoConfig)
