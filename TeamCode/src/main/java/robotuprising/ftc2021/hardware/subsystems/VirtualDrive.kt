package robotuprising.ftc2021.hardware.subsystems

import com.acmerobotics.roadrunner.drive.MecanumDrive
import robotuprising.lib.math.Angle
import robotuprising.lib.util.Extensions.d

/**
 * to use roadrunner we use a virtual drive that simulates our position on the field
 *
 * To use, this needs to fulfill several conditions:
 *
 * - Get heading from Homura
 * - Get wheel positions from Homura
 * - Set Homura's wheel powers
 */
// TODO
class VirtualDrive : MecanumDrive(0.d, 0.d, 0.d, 0.d, 0.d, 0.d) {

    private var realWheelPositions = mutableListOf(0.d, 0.d, 0.d, 0.d)
    var realIMUAngle = Angle.EAST
    var simulatedWheelPowers = mutableListOf(0.d, 0.d, 0.d, 0.d)

    fun setWheelPositions(positions: List<Double>) {
        positions.forEachIndexed { i, it -> realWheelPositions[i] = it }
    }

    override val rawExternalHeading: Double get() = realIMUAngle.angle

    override fun getWheelPositions(): List<Double> = realWheelPositions

    override fun setMotorPowers(frontLeft: Double, rearLeft: Double, rearRight: Double, frontRight: Double) {
        val simList = listOf(frontLeft, rearLeft, rearRight, frontRight)
        simList.forEachIndexed { i, it -> simulatedWheelPowers[i] = it }
    }
}
