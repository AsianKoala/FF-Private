package robotuprising.ftc2021.opmodes.osiris

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import robotuprising.ftc2021.subsystems.osiris.IntakeStopper
import robotuprising.ftc2021.subsystems.osiris.hardware.Turret

@Autonomous
class RedShitAuto : OsirisOpMode() {
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