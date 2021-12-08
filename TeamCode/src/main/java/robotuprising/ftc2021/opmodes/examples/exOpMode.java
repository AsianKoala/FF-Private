package robotuprising.ftc2021.opmodes.examples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class exOpMode extends OpMode {

    private exIntake bIntake;

    @Override
    public void init() {
        bIntake = new exIntake(hardwareMap);
    }

    @Override
    public void loop() {
        bIntake.turnIntakeOn();
        bIntake.getDistanceSensorReading();
    }
}
