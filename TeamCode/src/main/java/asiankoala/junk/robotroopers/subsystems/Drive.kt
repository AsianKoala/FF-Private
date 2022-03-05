package asiankoala.junk.robotroopers.subsystems

import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.subsystem.drive.KMecanumDrive

class Drive(
    fl: KMotor,
    bl: KMotor,
    fr: KMotor,
    br: KMotor
) : KMecanumDrive(fl, bl, fr, br)
