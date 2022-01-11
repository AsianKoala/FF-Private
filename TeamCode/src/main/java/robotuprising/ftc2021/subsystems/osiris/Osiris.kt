package robotuprising.ftc2021.subsystems.osiris

import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.ftc2021.hardware.osiris.interfaces.Loopable
import robotuprising.ftc2021.manager.GameStateManager

object Osiris : Subsystem(), Loopable {
    private val turret = Turret
    private val slide = Slide
    private val arm = Arm
    private val outtake = Outtake

    private val gameStateManager = GameStateManager

    val systemState = OsirisState()

    val systemGoal = OsirisState()

    private var followingGoal = false

    private var armTimer = ElapsedTime()
    private var outtakeTimer = ElapsedTime()

    private val maxArmTime = 0
    private val maxOuttakeTime = 0

    private var turretEnabled = false
    private var slideEnabled = false
    private var armEnabled = false
    private var outtakeEnabled = false

    private fun setSubsystemsDisabled() {
        turretEnabled = false
        slideEnabled = false
        armEnabled = false
        outtakeEnabled = false
    }

    private fun setGoal(newGoal: OsirisState) {
        systemGoal.apply {
            turret = newGoal.turret
            slide = newGoal.slide
            arm = newGoal.arm
            outtake = newGoal.outtake
        }

        followingGoal = true
    }

    private fun followSetpoint() {
        if(!turretEnabled && systemGoal.turret != systemGoal.turret) {
            turret.setTurretAngle(systemGoal.turret)
            turretEnabled = true
        } else if(turret.isAtTarget) {
            turretEnabled = false
            systemState.turret = systemGoal.turret
        }

        if(!slideEnabled && systemState.slide != systemGoal.slide) {
            slide.setSlideInches(systemGoal.slide)
            slideEnabled = true
        } else if(slide.isAtTarget) {
            slideEnabled = false
            systemState.slide = systemGoal.slide
        }

        if(!armEnabled && systemState.arm != systemGoal.arm) {
            arm.moveServoToPosition(systemGoal.arm)
            armEnabled = true
            armTimer.reset()
        } else if(armTimer.seconds() > maxArmTime && armEnabled) {
            armEnabled = false
            systemState.arm = systemGoal.arm
        }

        if(!outtakeEnabled && systemState.outtake != systemGoal.outtake) {
            outtake.moveServoToPosition(systemGoal.outtake)
            outtakeEnabled = true
            outtakeTimer.reset()
        } else if(outtakeTimer.seconds() > maxOuttakeTime && outtakeEnabled) {
            outtakeEnabled = false
            systemState.outtake = systemGoal.outtake
        }

        if(systemState.isAtDesiredState(systemGoal)) {
            setSubsystemsDisabled()
            followingGoal = false
        }
    }

    override fun reset() {
        setSubsystemsDisabled()
    }

    override fun updateDashboard(debugging: Boolean) {}

    override fun loop() {
        followSetpoint()
    }
}