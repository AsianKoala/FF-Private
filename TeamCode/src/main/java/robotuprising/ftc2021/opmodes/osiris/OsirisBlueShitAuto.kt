package robotuprising.ftc2021.opmodes.osiris

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import robotuprising.ftc2021.subsystems.osiris.IntakeStopper
import robotuprising.ftc2021.subsystems.osiris.hardware.Turret

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