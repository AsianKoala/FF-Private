package robotuprising.koawalib.subsystem.drive

import robotuprising.koawalib.command.commands.CommandBase
import robotuprising.koawalib.gamepad.Stick
import robotuprising.koawalib.math.MathUtil
import robotuprising.koawalib.math.MathUtil.d
import robotuprising.koawalib.math.MathUtil.radians
import robotuprising.koawalib.math.MathUtil.wrap
import robotuprising.koawalib.math.Point
import robotuprising.koawalib.math.Pose
import robotuprising.koawalib.util.Alliance

class MecanumDriveCommand(
        private val drive: KMecanumDrive,
        private val leftStick: Stick,
        private val rightStick: Stick,
        private val alliance: Alliance,
        private val xCubic: Double = 1.0,
        private val yCubic: Double = 1.0,
        private val rCubic: Double = 1.0,
        private val fieldOriented: Boolean = false,
        private val headingLock: Boolean = false,
        private val heading: () -> Double = { Double.NaN },
        private val headingLockScalar: Double = 90.0
) : CommandBase() {

    override fun execute() {
        val xRaw = leftStick.xSupplier.invoke()
        val yRaw = -leftStick.ySupplier.invoke()
        val rRaw = rightStick.xSupplier.invoke()

        val xScaled = MathUtil.cubicScaling(xCubic, xRaw)
        val yScaled = MathUtil.cubicScaling(yCubic, yRaw)
        val rScaled = MathUtil.cubicScaling(rCubic, rRaw)


        val final = if(fieldOriented) {
            val translationVector = Point(xScaled, yScaled)
            val rotatedTranslation = translationVector.rotate(alliance.decide(90, -90).d.radians)

            val headingInvoked = heading.invoke()
            val turn = if(headingLock && !headingInvoked.isNaN()) {
                val stickAtan = rightStick.angle
                val deltaAngle = (headingInvoked - stickAtan).wrap
                val rLockScaled = deltaAngle / headingLockScalar

                rLockScaled
            } else {
                rScaled
            }

            Pose(rotatedTranslation, turn)
        } else {
            Pose(xScaled, yScaled, rScaled)
        }

        drive.powers = final
    }

    override val isFinished: Boolean
        get() = false

    init {
        addRequirements(drive)
    }
}