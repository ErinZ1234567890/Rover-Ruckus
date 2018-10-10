
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
	  Basic Controls:
	  ~2 motor drive with varying methods using right/left stick controls
	  ~x/y buttons controlling the potential lifer of the glyphs
	  ~left/right trigger used for extending collecters
	  ~b button used used for moving the collector
	  ~left and right bumper used for switching drive train and collector mechanism controls
*/
@TeleOp(name = "Base TeleOp", group = "Tele Op")
public class BaseTeleOp extends OpMode {


    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    int driveSwitch = 1;
    DcMotor lift; //for outtake (though not sure how its going to work exactly)
    DcMotor flip;
    DcMotor collect;
    DcMotor cExtender; //c stands for collect --> aka intake
    int collectSwitch = 1;

    //     @Override
    public void init()
    {
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        lift = hardwareMap.dcMotor.get("lift");
        collect = hardwareMap.dcMotor.get("collect");
        cExtender = hardwareMap.dcMotor.get("cExtender");
    }

    public void loop() {
        if(gamepad1.left_bumper){ //for drive train
            if(gamepad1.dpad_up){
                driveSwitch = 1;
            }
            else if(gamepad1.dpad_right){
                driveSwitch = 2;
            }
            else if(gamepad1.dpad_left){
                driveSwitch = 3;
            }
            else if(gamepad1.dpad_down){
                driveSwitch = 4;
            }
            else{
            }
        }
        if(gamepad1.right_bumper){ //for collector
            if(gamepad1.dpad_up){
                collectSwitch = 1;
            }
            else if(gamepad1.dpad_down){
                collectSwitch = 2;
            }
            else{
            }
        }

        //controls that stay the same regardless of movement (can change to gamepad2 later)
        if(gamepad1.x) { //lifter
            lift.setPower(1);
        }
        else if(gamepad1.y) {
            lift.setPower(-1);
        }
        else {
            lift.setPower(0);
        }
        if(gamepad1.a){
            collect.setPower(1);
        }
        else if(gamepad1.b){
            collect.setPower(-1);
        }
        else{
            collect.setPower(0);
        }

        //basic controls being write to robot (changing)
        switch (driveSwitch) {
            case 1: //basic joystick controls
                backRight.setPower(gamepad1.right_stick_y);
                frontRight.setPower(gamepad1.right_stick_y);
                backLeft.setPower(gamepad1.left_stick_y);
                frontLeft.setPower(gamepad1.right_stick_y);

                break;
            case 2: //full button controls

                while(gamepad1.x){
                    frontRight.setPower(5);
                    backRight.setPower(5);
                    frontLeft.setPower(-5);
                    backLeft.setPower(-5);
                }
                while(gamepad1.b){
                    frontRight.setPower(-5);
                    backRight.setPower(-5);
                    frontLeft.setPower(5);
                    backLeft.setPower(5);
                }

                while (gamepad1.y){
                    frontRight.setPower(5);
                    frontLeft.setPower(5);
                    backRight.setPower(5);
                    backLeft.setPower(5);
                }
                while (gamepad1.a){
                    frontRight.setPower(-5);
                    frontLeft.setPower(-5);
                    backRight.setPower(-5);
                    backLeft.setPower(-5);
                }
                break;
            case 3: //Moving with left, turning with right
                backRight.setPower(gamepad1.right_stick_y);
                frontRight.setPower(gamepad1.right_stick_y);
                backLeft.setPower(gamepad1.left_stick_y);
                frontLeft.setPower(gamepad1.right_stick_y);
                if (gamepad1.right_stick_x > 0) {
                    backRight.setPower(-gamepad1.right_stick_y);
                    frontRight.setPower(-gamepad1.right_stick_y);
                    backLeft.setPower(gamepad1.left_stick_y);
                    frontLeft.setPower(gamepad1.right_stick_y);
                }
                else if(gamepad1.right_stick_x < 0) {
                    backRight.setPower(gamepad1.right_stick_y);
                    frontRight.setPower(gamepad1.right_stick_y);
                    backLeft.setPower(-gamepad1.left_stick_y);
                    frontLeft.setPower(-gamepad1.right_stick_y);
                }
                break;
            case 4: //turning with dPad, moving up/down with right stick
                backRight.setPower(gamepad1.right_stick_y);
                frontRight.setPower(gamepad1.right_stick_y);
                backLeft.setPower(gamepad1.left_stick_y);
                frontLeft.setPower(gamepad1.right_stick_y);
                while(gamepad1.dpad_left){ //turn left
                    frontRight.setPower(5);
                    backRight.setPower(5);
                }
                while(gamepad1.dpad_right){ //turn right
                    frontLeft.setPower(5);
                    backLeft.setPower(5);
                }
                break;
            default:
                telemetry.addData("Text", "Drive Train Switch Error... fix me!!!!!"); //:)
                break;
        }
        switch(collectSwitch){  //for collector stuff
            case 1:
                cExtender.setPower(gamepad1.right_trigger);
                cExtender.setPower(-gamepad1.left_trigger);
                break;
            case 2:
                boolean isExtended = false;
                if(gamepad1.right_trigger > 0.1 && isExtended){
                    cExtender.setPower(.75);
                    wait(22);
                    cExtender.setPower(-.2);
                    wait(5);
                    cExtender.setPower(0);
                    isExtended = false;
                }
                else if(gamepad1.right_trigger > 0.1 && !isExtended){
                    cExtender.setPower(.6);
                    wait(30);
                    cExtender.setPower(0);
                    isExtended = true;
                }
                break;
            default:
                telemetry.addData("Text", "Collector Switch Error... fix me!!!!!"); //:)
                break;
        }


        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("text", "Front Right: " + frontRight);
        telemetry.addData("text", "Front Left: " + frontLeft);
        telemetry.addData("text", "Back Right: " + backRight);
        telemetry.addData("text", "Back Left: " + backLeft);
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
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
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
