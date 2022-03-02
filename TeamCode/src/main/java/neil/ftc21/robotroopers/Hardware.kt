package neil.ftc21.robotroopers

import neil.ftc21.robotroopers.subsystems.Arm
import neil.koawalib.hardware.KServo
import neil.koawalib.hardware.motor.KMotor
import neil.koawalib.hardware.motor.KMotorEx
import neil.koawalib.control.PIDExController
import neil.koawalib.hardware.sensor.KRev2mDistanceSensor

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