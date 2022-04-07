package com.asiankoala.koawalib.command.commands

import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.path.Path
import com.asiankoala.koawalib.subsystem.drive.KMecanumOdoDrive

class PathCommand(
    private val drive: KMecanumOdoDrive,
    private val path: Path,
    private val tol: Double,
    private val xPower: Double
) : CommandBase() {

    override fun initialize() {
        drive.powers = Pose()
    }

    override fun execute() {
        var powers = path.update(drive.position, tol).first
        powers = Pose(powers.x + xPower, powers.y, powers.heading)
        drive.powers = powers
    }

    override fun end(interrupted: Boolean) {
        drive.powers = Pose()
    }

    override val isFinished: Boolean get() = path.isFinished

    init {
        addRequirements(drive)
    }
}
