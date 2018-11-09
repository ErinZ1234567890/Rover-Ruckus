package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "New Chassis TeleOp 1.0", group = "TeleOp")
public class NewChassis extends OpMode {

    double left, right, drive, turn, max;
    DcMotor fr, br, fl, bl;

    public void init() {
        fr = hardwareMap.dcMotor.get("fr");
        br = hardwareMap.dcMotor.get("br");
        fl = hardwareMap.dcMotor.get("fl");
        bl = hardwareMap.dcMotor.get("bl");
    }

    @Override
    public void loop() {

        fr.setPower(gamepad1.right_stick_y);
        br.setPower(gamepad1.right_stick_y);
        fl.setPower(gamepad1.left_stick_y);
        bl.setPower(gamepad1.left_stick_y);
    }

    @Override
    public void stop() {
        fr.setPower(0);
        br.setPower(0);
        fl.setPower(0);
        bl.setPower(0);
    }
}