package robotuprising.ftc2021.subsystems.osiris.hardware

import robotuprising.ftc2021.subsystems.osiris.motor.*


object Spinner : MotorSubsystem(
        MotorSubsystemConfig(MotorConfig("duckSpinner"), MotorControlType.OPEN_LOOP)
) {
        fun setPower(power: Double) {
                output = power
        }
}