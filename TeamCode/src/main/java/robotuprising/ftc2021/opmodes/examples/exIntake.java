package robotuprising.ftc2021.opmodes.examples;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class exIntake {
    private exHwMap hwMap;
    private DistanceSensor sensor;

    public exIntake(HardwareMap hardwareMap) {
        hwMap = new exHwMap(hardwareMap);
        sensor = hardwareMap.get(DistanceSensor.class, "sensor");
    }

    public void turnIntakeOn() {
        hwMap.intake.setPower(1);
    }

    public double getDistanceSensorReading() {
        return sensor.getDistance(DistanceUnit.MM);
    }
}
