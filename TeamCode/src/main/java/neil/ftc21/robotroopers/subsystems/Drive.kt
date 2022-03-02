package neil.ftc21.robotroopers.subsystems

import neil.koawalib.hardware.motor.KMotor
import neil.koawalib.subsystem.drive.KMecanumDrive

class Drive(fl: KMotor,
            bl: KMotor,
            fr: KMotor,
            br: KMotor
) : KMecanumDrive(fl, bl, fr, br)