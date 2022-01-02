package robotuprising.ftc2021.subsystems.wraith

import robotuprising.ftc2021.hardware.wraith.interfaces.Loopable
import robotuprising.ftc2021.hardware.wraith.WraithMotor
import robotuprising.lib.control.auto.path.PurePursuitPath
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import kotlin.math.absoluteValue

object Ghost : Subsystem(), Loopable {
    private val fl = WraithMotor("fl", true).brake.openLoopControl.reverse
    private val bl = WraithMotor("bl", true).brake.openLoopControl.reverse
    private val fr = WraithMotor("fr", true).brake.openLoopControl
    private val br = WraithMotor("br", true).brake.openLoopControl
    private val motors = listOf(fl, bl, fr, br)

    var powers = Pose(Point(), Angle(0.0, AngleUnit.RAD))

    var currentPath: PurePursuitPath? = null

    override fun reset() {
        powers = Pose.DEFAULT_RAW
    }

    override fun updateDashboard(debugging: Boolean) {

    }

    override fun loop() {
        val fl = powers.y + powers.x + powers.h.angle
        val bl = powers.y - powers.x + powers.h.angle
        val fr = powers.y - powers.x - powers.h.angle
        val br = powers.y + powers.x - powers.h.angle

        val wheels = listOf(fl, bl, fr, br)
        val absMax = wheels.map { it.absoluteValue }.maxOrNull()!!
        if (absMax > 1.0) {
            motors.forEachIndexed { i, it -> it.power = wheels[i] / absMax }
        } else {
            motors.forEachIndexed { i, it -> it.power = wheels[i] }
        }
    }

}