
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/*
	Holonomic concepts from:
	http://www.vexforum.com/index.php/12370-holonomic-drives-2-0-a-video-tutorial-by-cody/0
   Robot wheel mapping:
          X FRONT X
        X           X
      X  FL       FR  X
              X
             XXX
              X
      X  BL       BR  X
        X           X
          X       X
*/
@TeleOp(name = "Full Tele Op", group = "Tele Op")
public class full_tele_op extends OpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    int driveSwitch = 0;

//     @Override
    public void init()
    {
        motorRight = hardwareMap.dcMotor.get("right motor");
        motorLeft = hardwareMap.dcMotor.get("left motor");
    }
	
    public void loop() {
	if(left_bumper){
		if(dpad_up){
		    driveSwitch = 0;
		}
		else if(dpad_right){
		    driveSwitch = 1;
		}
		else if(dpad_left){
		    driveSwitch = 2;
		}
		else if(dpad_down){
		    driveSwitch = 3;
		}
		else if(dpad_down){
		    driveSwitch = 4;
		}
		else if(dpad_down){
		    driveSwitch = 5;
		}
		else{
		}
	}
	
        //basic controls being write to robot (changing)
        switch (driveSwitch) {
            case 0:
                motorRight = gamepad1.right_stick_y;
                motorLeft = gamepad1.left_stick_y;
		break;
            case 1:
		break;
	    case 2:
	        motorRight = gamepad1.left_stick_y;
                motorLeft = gamepad1.left_stick_y;
		if (gamepad1.right_stick_x > 0) {
		    motorRight = -gamepad1.right_stick_x;
		    motorLeft = gamepad1.right_stick_x;
		}
		else if(gamepad1.right_stick_x < 0) {
		    motorRight = gamepad1.right_stick_x;
		    motorLeft = -gamepad1.right_stick_x;
		}
		break;
	    case 3:
		break;
	    case 4:
		break;
	}

        telemetry.addData("Text", "*** Robot Data***");
	telemetry.addData("Right Motor: " + motorRight); //drive
	telemetry.addData("Left Motor: " + motorLeft);
    }
//     @Override
    public void stop() {
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
