
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Base TeleOp", group = "Tele Op")
public class BaseTeleOp extends OpMode {


    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor lift;
    Servo marker;
    DcMotor flip;
    CRServo intake;

     @Override
    public void init()
    {
        frontRight = hardwareMap.dcMotor.get("rightFront");
        frontLeft = hardwareMap.dcMotor.get("leftFront");
        backLeft = hardwareMap.dcMotor.get("leftBack");
        backRight = hardwareMap.dcMotor.get("rightBack");
        lift = hardwareMap.dcMotor.get("lift");
        marker = hardwareMap.servo.get("marker");
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        flip = hardwareMap.dcMotor.get("flip");
        intake = hardwareMap.crservo.get("intake");
    }

    int driveSwitch = 1;
    public void loop() {
        if(gamepad1.left_bumper) { //for drive train
            if (gamepad1.dpad_up) {
                driveSwitch = 1;
            }
            if (gamepad1.dpad_up) {
                driveSwitch = 2;
            }
            else{
                telemetry.addData("text", "driveSwitch error...");
            }
        }
         // for marker mech

        // for mecanum driving
        if (driveSwitch == 1) {
            frontLeft.setPower(gamepad1.right_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x); //right stick movement, left stick turning
            frontRight.setPower(gamepad1.right_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x);
            backLeft.setPower(gamepad1.right_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x);
            backRight.setPower(gamepad1.right_stick_y + gamepad1.right_stick_x - gamepad1.left_stick_x);
        }
        else if (driveSwitch == 2){
            float drive = gamepad1.right_stick_y;
            float strafe = gamepad1.right_stick_x;
            float turn = gamepad1.left_stick_x;

            float fl = drive - strafe + turn;
            float fr = drive + strafe - turn;
            float bl = drive + strafe + turn;
            float br = drive - strafe - turn;

            frontLeft.setPower(fl);
            frontRight.setPower(fr);
            backLeft.setPower(bl);
            backRight.setPower(br);
        }
        // for lifting
        lift.setPower(gamepad2.right_stick_y);

        // for flipping
        flip.setPower(gamepad2.left_stick_y);
        flip.setPower(gamepad2.left_stick_x / 4); // for testing flipper

        // for intake crservo
        if (gamepad2.right_trigger > 0) {
            intake.setPower(scaleInput(gamepad2.left_trigger));
        } else if (gamepad2.left_trigger > 0) {
            intake.setPower(-scaleInput(gamepad2.left_trigger));
        } else {
            intake.setPower(0);
        }

    }

    public void stop() {
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        lift.setPower(0);
        marker.setPosition(0);
        intake.setPower(0);
    }

    double scaleInput(double dVal)  { //extra scaling method
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}
