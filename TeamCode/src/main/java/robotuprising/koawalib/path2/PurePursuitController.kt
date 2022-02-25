package robotuprising.koawalib.path2

import com.qualcomm.robotcore.util.Range
import robotuprising.koawalib.math.MathUtil
import robotuprising.koawalib.math.MathUtil.cos
import robotuprising.koawalib.math.MathUtil.radians
import robotuprising.koawalib.math.MathUtil.sin
import robotuprising.koawalib.math.MathUtil.wrap
import robotuprising.koawalib.math.Point
import robotuprising.koawalib.math.Pose
import kotlin.math.absoluteValue
import kotlin.math.hypot

object PurePursuitController {

    fun goToPosition(currPose: Pose, targetPosition: Point, followAngle: Double = 90.0.radians,
                     stop: Boolean = false, maxMoveSpeed: Double = 1.0, maxTurnSpeed: Double = 1.0,
                     isHeadingLocked: Boolean = false, headingLockAngle: Double = 0.0,
                     slowDownTurnRadians: Double = 60.0.radians, lowestSlowDownFromTurnError: Double = 0.4): Pose {

        val absoluteDelta = targetPosition - currPose.point
        val distanceToPoint = absoluteDelta.hypot

        val angleToPoint = absoluteDelta.atan2
        val deltaAngleToPoint = (angleToPoint - (currPose.heading - 90.0.radians)).wrap

        val relativeXToPosition = distanceToPoint * deltaAngleToPoint.cos
        val relativeYToPosition = distanceToPoint * deltaAngleToPoint.sin

        val relativeAbsMagnitude = hypot(relativeXToPosition, relativeYToPosition)
        var xPower = relativeXToPosition / relativeAbsMagnitude
        var yPower = relativeYToPosition / relativeAbsMagnitude

        if(stop) {
            xPower *= relativeXToPosition.absoluteValue / 12.0
            yPower *= relativeYToPosition.absoluteValue / 12.0
        }

        xPower = MathUtil.clamp(xPower, -maxMoveSpeed, maxMoveSpeed)
        yPower = MathUtil.clamp(yPower, -maxMoveSpeed, maxMoveSpeed)



        val actualRelativePointAngle = (followAngle - 90.0.radians)

        val absolutePointAngle = if(isHeadingLocked) {
            headingLockAngle
        } else {
            angleToPoint + actualRelativePointAngle
        }


        val relativePointAngle = (absolutePointAngle - currPose.heading).wrap
        val deccelAngle = 45.0.radians

        var turnPower = (relativePointAngle / deccelAngle) * maxTurnSpeed
        turnPower = MathUtil.clamp(turnPower, -maxTurnSpeed, maxTurnSpeed)

        if(distanceToPoint < 4.0) {
            turnPower = 0.0
        }

        xPower *= Range.clip(relativeXToPosition.absoluteValue / 2.5, 0.0, 1.0)
        yPower *= Range.clip(relativeYToPosition.absoluteValue / 2.5, 0.0, 1.0)

        turnPower *= Range.clip(relativePointAngle.absoluteValue / 3.0.radians, 0.0, 1.0)




        // slow down if angle is off
        var errorTurnSoScaleMovement = Range.clip(1.0 - (relativePointAngle / slowDownTurnRadians).absoluteValue, lowestSlowDownFromTurnError, 1.0)

        if(turnPower.absoluteValue < 0.00001) {
            errorTurnSoScaleMovement = 1.0
        }

        xPower *= errorTurnSoScaleMovement
        yPower *= errorTurnSoScaleMovement


        return Pose(xPower, yPower, turnPower)
    }
}