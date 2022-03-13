package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.hardware.sensor.KDistanceSensor
import com.asiankoala.koawalib.subsystem.intake.IntakeConfig
import com.asiankoala.koawalib.subsystem.intake.KDistanceSensorIntake

class Intake(
    motor: KMotor,
    distanceSensor: KDistanceSensor,
    config: IntakeConfig,
    distanceThreshold: Double)
: KDistanceSensorIntake(motor, distanceSensor, config, distanceThreshold)