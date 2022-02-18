package robotuprising.ftc2021.v3.subsystems

import robotuprising.koawalib.hardware.KMotor
import robotuprising.koawalib.subsystem.drive.KMecanumDrive

class Kei(
        fl: KMotor,
        bl: KMotor,
        fr: KMotor,
        br: KMotor
) : KMecanumDrive(fl, bl, fr, br) {

}