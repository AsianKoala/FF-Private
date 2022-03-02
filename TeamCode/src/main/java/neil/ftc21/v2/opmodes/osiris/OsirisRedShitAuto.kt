package neil.ftc21.v2.opmodes.osiris

import neil.ftc21.v2.subsystems.osiris.IntakeStopper
import neil.ftc21.v2.subsystems.osiris.hardware.Turret

class OsirisRedShitAuto : OsirisOpMode() {
    override fun mInit() {
        super.mInit()
        IntakeStopper.lock()
        Turret.zero()
        Turret.setTurretLockAngle(90.0)
    }

    override fun mInitLoop() {
        super.mInitLoop()
        IntakeStopper.lock()
    }


    override fun mStart() {
        super.mStart()
        Turret.setTurretLockAngle(180.0)
    }
}