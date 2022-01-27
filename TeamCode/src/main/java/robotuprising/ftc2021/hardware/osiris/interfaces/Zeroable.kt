package robotuprising.ftc2021.hardware.osiris.interfaces

interface Zeroable {
    val zeroed: Boolean

    fun zero()
}

/*
for turret:
in init, freely move turret
when hit limit switch, record current position as offset, add Constants.turretDefault, for absolute value

to freely move turret, need to be in float mode

then after it zeros, need to switch to brake mode

and then go to init position



could make a ZeroableMotorSubsystem and extend MotorSubsystem




 */