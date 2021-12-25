package robotuprising.ftc2021.opmodes.junk.nakiri.testing

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
@Disabled
class DashboardTest : OpMode() {

    override fun init() {
    }

    override fun loop() {
        val packet = TelemetryPacket()
        packet.put("x", 3.7)
        packet.put("status", "alive")
        FtcDashboard.getInstance().sendTelemetryPacket(packet)
    }
}
