package robotuprising.ftc2021.opmodes.examples;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class exHwMap {
    public DcMotor intake;

    public exHwMap(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, "intake");
    }
}
