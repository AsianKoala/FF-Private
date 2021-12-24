package robotuprising.ftc2021.subsystems.wraith

object Turret : MotorSubsystem(
        ControlledMotorConfig(
                "turret",
                false,

                MotorControlType.POSITION_PID,

                90.0,
                1.0,
                5.0,

                deadzone = 0.5,
                positionLowerLimit = -91.0,
                positionUpperLimit = 91.0
        )
) {

    val turretAngle: Double get() = position

    val homing: Boolean get() = isAtTarget

    fun setTurretAngle(angle: Double) {
        controller.reset()
        controller.targetPosition = angle
    }
}