package robotuprising.ftc2021.v3.subsystems

import robotuprising.koawalib.hardware.KMotor
import robotuprising.koawalib.subsystem.motor.MotorSubsystem
import robotuprising.koawalib.subsystem.motor.controllers.OpenLoopController

class IntakeSubsystem(motor: KMotor) : MotorSubsystem(motor, OpenLoopController()) {
    fun turnOn() {
        setPower(1.0)
    }

    fun turnReverse() {
        setPower(-1.0)
    }

    fun turnOff() {
        setPower(0.0)
    }
}