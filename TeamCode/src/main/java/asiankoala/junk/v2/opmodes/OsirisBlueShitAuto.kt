package asiankoala.junk.v2.opmodes

import asiankoala.junk.v2.subsystems.IntakeStopper
import asiankoala.junk.v2.subsystems.hardware.Turret

class OsirisBlueShitAuto : OsirisOpMode() {
    override fun mInit() {
        super.mInit()
        Turret.zero()
        Turret.setTurretLockAngle(270.0)
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
