package asiankoala.ftc2021

import asiankoala.ftc2021.subsystems.Arm
import asiankoala.ftc2021.subsystems.Indexer
import asiankoala.ftc2021.subsystems.IntakeStopper
import asiankoala.ftc2021.subsystems.Outtake
import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.hardware.sensor.KDistanceSensor
import com.asiankoala.koawalib.hardware.servo.KServo

class Hardware {
    val flMotor = KMotor("fl").brake.reverse
    val blMotor = KMotor("bl").brake.reverse
    val brMotor = KMotor("br").brake
    val frMotor = KMotor("fr").brake
    val intakeMotor = KMotor("intake").reverse
    val turretMotor = KMotor("turret").brake
    val slideMotor = KMotor("slides").float.reverse
    val duckMotor = KMotor("duckSpinner").brake

    val armServo = KServo("arm").startAt(Arm.armHomePosition)
    val indexerServo = KServo("indexer").startAt(Indexer.indexerOpenPosition)
    val outtakeServo = KServo("outtake").startAt(Outtake.outtakeCockPosition)
    val intakeStopperServo = KServo("intakeStopper").startAt(IntakeStopper.UNLOCK_POSITION)

    val distanceSensor = KDistanceSensor("loadingSensor")
}