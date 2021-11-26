package robotuprising.ftc2021.auto.drive

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.PIDFCoefficients

/*
 * Constants shared between multiple drive types.
 *
 * TODO: Tune or adjust the following constants to fit your robot. Note that the non-final
 * fields may also be edited through the dashboard (connect to the robot's WiFi network and
 * navigate to https://192.168.49.1:8080/dash). Make sure to save the values here after you
 * adjust them in the dashboard; **config variable changes don't persist between app restarts**.
 *
 * These are not the only parameters; some are located in the localizer classes, drive base classes,
 * and op modes themselves.
 */

@Config
object DriveConstants {
    const val TICKS_PER_REV = 537.7
    const val MAX_RPM = 312.0

    const val RUN_USING_ENCODER = false
    var MOTOR_VELO_PID = PIDFCoefficients(
        0.0, 0.0, 0.0,
        getMotorVelocityF(MAX_RPM / 60 * TICKS_PER_REV)
    )

    @JvmField var WHEEL_RADIUS = 1.88976 // in
    @JvmField var GEAR_RATIO = 1.0 // output (wheel) speed / input (motor) speed
    @JvmField var TRACK_WIDTH = 15.6

    @JvmField var kV = 0.017713
    @JvmField var kA = 0.00315
    @JvmField var kStatic = 0.00115

    @JvmField var MAX_VEL = 30.0
    @JvmField var MAX_ACCEL = 30.0

    @JvmField var MAX_ANG_VEL = 3.710134128484536
    @JvmField var MAX_ANG_ACCEL = Math.toRadians(60.0)

    @JvmStatic
    fun encoderTicksToInches(ticks: Double): Double {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV
    }

    @JvmStatic
    fun rpmToVelocity(rpm: Double): Double {
        return rpm * GEAR_RATIO * 2 * Math.PI * WHEEL_RADIUS / 60.0
    }

    @JvmStatic
    fun getMotorVelocityF(ticksPerSecond: Double): Double {
        // see https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#heading=h.61g9ixenznbx
        return 32767 / ticksPerSecond
    }
}
