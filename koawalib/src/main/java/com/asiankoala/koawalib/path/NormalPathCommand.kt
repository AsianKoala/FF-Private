package com.asiankoala.koawalib.path

import com.asiankoala.koawalib.command.commands.CommandBase
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.subsystem.drive.KMecanumOdoDrive

class NormalPathCommand(
        private val drive: KMecanumOdoDrive,
        private val path: NormalPath,
        private val tol: Double
) : CommandBase() {
    override fun initialize() {
        drive.powers = Pose()
    }
    
    override fun execute() {
        drive.powers = path.update(drive.position, tol).first
    }

    override fun end(interrupted: Boolean) {
        drive.powers = Pose()
    }

    override val isFinished: Boolean get() = path.isFinished

    init {
        addRequirements(drive)
    }

}