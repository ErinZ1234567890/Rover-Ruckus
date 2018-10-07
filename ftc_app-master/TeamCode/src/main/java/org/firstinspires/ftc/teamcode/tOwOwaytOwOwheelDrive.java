package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "OwO.0", group = "TeleOp 1.1")
public class tOwOwaytOwOwheelDrive extends OpMode {
    DcMotor fr, br, fl, bl;
    DcMotor lift;

    @Override
    public void init() {
        lift = hardwareMap.dcMotor.get("lift");

        fr = hardwareMap.dcMotor.get("fr");
        br = hardwareMap.dcMotor.get("br");
        fl = hardwareMap.dcMotor.get("fl");
        bl = hardwareMap.dcMotor.get("bl");
    }

    @Override
    public void loop() {

        if (gamepad1.right_trigger > 0) {
            lift.setPower(gamepad1.right_trigger);
        } else if (gamepad1.left_trigger > 0) {
            lift.setPower(-gamepad1.left_trigger);
        } else {
            lift.setPower(0);
        }

        fr.setPower(gamepad1.right_stick_y);
        br.setPower(gamepad1.right_stick_y);

        fl.setPower(-gamepad1.left_stick_y);
        bl.setPower(-gamepad1.left_stick_y);
    }

    @Override
    public void stop() {
    }
}