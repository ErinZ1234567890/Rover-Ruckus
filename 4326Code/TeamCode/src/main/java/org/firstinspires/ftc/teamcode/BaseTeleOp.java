package org.firstinspires.ftc.teamcode;


import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorController;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.Runtime;
import java.util.Timer;

/*
          X FRONT X
        X           X    <-- wheel spaces...?
	    X FL     FR X
             XXX
              X
      X  C        C  X <--castors
              X
             XXX
              X
      X  BL       BR  X
        X           X
          X       X
	  Basic Controls:
	  ~2 motor holonomic drive with varying methods using right/left stick controls
	  ~x/y buttons controlling the potential lifer of the glyphs
	  ~left/right trigger used for extending collecters
	  ~b button used used for moving the collector
	  ~left and right bumper used for switching drive train and collector mechanism controls
*/
@TeleOp(name = "BaseTeleOp", group = "BaseTeleOp")
public class BaseTeleOp extends OpMode {


    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    DcMotor lift; //for outtake (though not sure how its going to work exactly)
//    DcMotor flip;
//    DcMotor collect;
//    DcMotor cExtender; //c stands for collect --> aka intake
//    int collectSwitch = 1;

    //     @Override
    public void init()
    {
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        lift = hardwareMap.dcMotor.get("lift");
//        collect = hardwareMap.dcMotor.get("collect");
//        cExtender = hardwareMap.dcMotor.get("cExtender");
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);
    }

    public void loop() {
        leftFront.setPower((float)(gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x));
        rightFront.setPower((float)(-gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x));
        rightBack.setPower((float)(-gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x));
        leftBack.setPower((float)(gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x));

        if (gamepad1.right_bumper) {
            lift.setPower(1);
        } else if (gamepad1.left_bumper) {
            lift.setPower(-1);
        } else {
            lift.setPower(0);
        }

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("text", "Front Right: " + rightFront);
        telemetry.addData("text", "Front Left: " + leftFront);
        telemetry.addData("text", "Back Right: " + rightBack);
        telemetry.addData("text", "Back Left: " + leftBack);
    }
    //     @Override
    public void wait(int time) { //10th seconds
        try {
            Thread.sleep(time * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void stop() {
        rightFront.setPower(0);
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        lift.setPower(0);

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
