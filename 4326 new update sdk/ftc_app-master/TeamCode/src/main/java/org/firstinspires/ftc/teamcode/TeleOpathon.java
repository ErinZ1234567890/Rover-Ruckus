package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "TeleOpathon1.2", group = "TeleOp 1.1")
public class TeleOpathon extends OpMode {
    double left, right, drive, turn, max;

    DcMotor lift;

    DcMotor flip;

    DcMotor rightDrive;
    DcMotor leftDrive;

    CRServo intake;
    public void init() {
        lift = hardwareMap.dcMotor.get("lift");

        rightDrive = hardwareMap.dcMotor.get("right");
        leftDrive = hardwareMap.dcMotor.get("left");

        flip = hardwareMap.dcMotor.get("flip");

        intake = hardwareMap.get(CRServo.class, "intake");

    }

    @Override
    public void loop() {


        if (gamepad2.right_trigger > 0) {
            flip.setPower(gamepad2.right_trigger);
        } else if (gamepad2.left_trigger > 0) {
            flip.setPower(-gamepad2.left_trigger);
        } else {
            flip.setPower(0);
        }

        lift.setPower(gamepad2.right_stick_y);

        if(gamepad2.a) {
            intake.setPower(1);
        }
        if(gamepad2.y) {
            intake.setPower(0);        }
        if (gamepad2.b) {
            intake.setPower(-1);
        }


        rightDrive.setPower(gamepad1.right_stick_y);
        leftDrive.setPower(gamepad1.left_stick_y);

        left  = drive + turn;
        right = drive - turn;

        max = Math.max(Math.abs(left), Math.abs(right));
        if (max > 1.0)
        {
            left /= max;
            right /= max;
        }
        rightDrive.setPower(right);
        leftDrive.setPower(left);

    }

    @Override
    public void stop() {
        lift.setPower(0);
        rightDrive.setPower(0);
        leftDrive.setPower(0);
    }
}