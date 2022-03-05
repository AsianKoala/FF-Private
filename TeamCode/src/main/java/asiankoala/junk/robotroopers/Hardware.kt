package asiankoala.junk.robotroopers

import asiankoala.junk.robotroopers.subsystems.Arm
import com.asiankoala.koawalib.control.PIDExController
import com.asiankoala.koawalib.hardware.KServo
import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.hardware.motor.KMotorEx
import com.asiankoala.koawalib.hardware.sensor.KRev2mDistanceSensor

class Hardware {
    val fl = KMotor("FL").reverse
    val fr = KMotor("FR")
    val bl = KMotor("BL").reverse
    val br = KMotor("BR")

    val arm = KMotorEx("Arm", PIDExController(Arm.config)).reverse.resetEncoder.float.zero(-114.0)

    val duck = KMotor("DuckL")
    val intake = KMotor("Intake")

    val distanceSensor = KRev2mDistanceSensor("distanceSensor")
    val indexer = KServo("Outtake").startAt(0.9)
}
