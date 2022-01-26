package robotuprising.ftc2021.subsystems.osiris

import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.ftc2021.hardware.osiris.interfaces.Loopable
import robotuprising.ftc2021.manager.GameStateManager
import robotuprising.ftc2021.statemachines.IntakeStateMachine
import robotuprising.ftc2021.util.Constants

object Osiris : Subsystem(), Loopable {
    private val turret = Turret
    private val slide = Slide
    private val arm = Arm
    private val outtake = Outtake

    private val systemState = OsirisState()

    private val systemGoal = OsirisState()

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

    private fun followSetpoint() {
        if(!turretEnabled && systemState.turret != systemGoal.turret) {
            turret.setTurretLockAngle(systemGoal.turret)
            turretEnabled = true
        } else if(turret.isAtTarget) {
            turretEnabled = false
            systemState.turret = systemGoal.turret
        }

        if(!slideEnabled && systemState.slide != systemGoal.slide) {
            slide.setSlideLockTarget(systemGoal.slide)
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
            if(systemState.outtake == Constants.outtakeReadyPosition && systemState.arm != systemGoal.arm) {
                // if outtake is home, wait for arm to move
            } else {
                outtake.moveServoToPosition(systemGoal.outtake)
                outtakeEnabled = true
                outtakeTimer.reset()
            }
        } else if(outtakeTimer.seconds() > maxOuttakeTime && outtakeEnabled) {
            outtakeEnabled = false
            systemState.outtake = systemGoal.outtake
        }

        if(systemState.isAtDesiredState(systemGoal)) {
            setSubsystemsDisabled()
            followingGoal = false
        }
    }

    fun setGoal(newGoal: OsirisState) {
        systemGoal.apply {
            turret = newGoal.turret
            slide = newGoal.slide
            arm = newGoal.arm
            outtake = newGoal.outtake
        }

        followingGoal = true
    }

    val currGoal get() = systemGoal
    val currState get() = systemState
    val done get() = !followingGoal

    override fun reset() {
        setSubsystemsDisabled()
    }

    override fun updateDashboard(debugging: Boolean) {}

    override fun loop() {
        followSetpoint()
    }


    val depositGoalRed = OsirisState(Constants.turretRedDepositAngle, Constants.slideDepositInches, Constants.armAllianceDepositPosition, Constants.outtakeDepositPosition)
    val resetGoal = OsirisState()
    val depositGoalBlue = depositGoalRed.copy(turret = Constants.turretBlueDepositAngle)
}