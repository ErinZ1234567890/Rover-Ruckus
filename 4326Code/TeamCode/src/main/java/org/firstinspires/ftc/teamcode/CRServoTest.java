package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "CRServoTest 1.0", group = "TeleOp 1.1")
public class CRServoTest extends OpMode {

    CRServo intake;

    public void init() {
        intake = hardwareMap.get(CRServo.class, "intake");
    }

    @Override
    public void loop() {
        intake.setPower(gamepad1.right_stick_y);
    }

    @Override
    public void stop() {
        intake.setPower(0);
    }
}