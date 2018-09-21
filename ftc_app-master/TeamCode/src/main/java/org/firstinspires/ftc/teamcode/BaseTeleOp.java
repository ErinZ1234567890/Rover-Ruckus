

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
		}
		else if(dpad_left){
		}
		else if(dpad_down){
		}
		else{
		}
	}
	
        //basic controls being write to robot (changing)
        switch (driveSwitch) {
            case 0: {
                motorRight = gamepad1.right_stick_y;
                motorLeft = gamepad1.left_stick_y;
            }
            case 1: {
		
            }
        }


        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("*Drive System*");
	telemetry.addData("Right Motor: " + motorRight);
	telemetry.addData("Left Motor: " + motorLeft);

    }
//     @Override
    public void stop() {
    }
}
