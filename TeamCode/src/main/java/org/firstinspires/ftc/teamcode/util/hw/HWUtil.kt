package org.firstinspires.ftc.teamcode.util.hw

import com.qualcomm.robotcore.hardware.DcMotor
import org.openftc.revextensions2.ExpansionHubMotor

object HWUtil {
    fun defaultMotorSetup(arr: Array<out ExpansionHubMotor>) {
        arr.forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }
    }
}
