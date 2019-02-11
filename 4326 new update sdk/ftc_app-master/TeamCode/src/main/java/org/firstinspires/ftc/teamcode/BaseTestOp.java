
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "TRUE TELEOP", group = "Tele Op")
public class BaseTestOp extends OpMode {
/*
    CONTROLS: (& = counterpart, + = pressed together)
     -Gamepad1:
       -right stick (forward/backward/straf)
       -left stick (turning - xaxis)
       -right&left triggers (lift up/down)
     -Gamepad2:
       -right stick (flipping arm - yaxis)
       -left stick (extending arm - yaxis)
       -right&left bumpers (intake/outtake)
       -right+left triggers (locks left stick) *
       -right+left triggers + a (unlocks left stick)
     -Both:
       -a button + left dpad (stop's robot)
*/
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor lift;
//    Servo   marker;
    DcMotor flip; //wrist
    DcMotor arm; //extending
    CRServo intake; //spiiiiiiin
//    CRServo sample;

    @Override
    public void init()
    {
        frontRight = hardwareMap.dcMotor.get("rightFront");
        frontLeft = hardwareMap.dcMotor.get("leftFront");
        backLeft = hardwareMap.dcMotor.get("leftBack");
        backRight = hardwareMap.dcMotor.get("rightBack");
       lift = hardwareMap.dcMotor.get("lift1");
        arm = hardwareMap.dcMotor.get("arm");
        flip = hardwareMap.dcMotor.get("flip");
        intake = hardwareMap.crservo.get("intake");

//        marker = hardwareMap.servo.get("marker");
//        sample = hardwareMap.crservo.get("sample");
//        sample.setPower(1);

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        lift.setDirection(DcMotor.Direction.REVERSE);
//        marker.setPosition(0);
    }

    //helper variables
    int driveSwitch = 1;
    double armVal = 0;
    boolean canArm = true;

    public void loop() {
        //         GAMEPAD 1 ********************
        if(gamepad1.left_bumper) { //for drive train
            if (gamepad1.dpad_up) {
                driveSwitch = 1;
            }
            if (gamepad1.dpad_down) {
                driveSwitch = 2;
            }
            else{
                telemetry.addData("text", "driveSwitch error...");
            }
        }
//        // for marker mech
//        if(gamepad1.a){   //just in case
//            if(gamepad1.left_bumper){
//                marker.setPosition(0);
//            }
//            else if(gamepad1.right_bumper){
//                marker.setPosition(1);
//            }
//        }
//        if(gamepad1.b){   //just in case
//            if(gamepad1.left_bumper){
//                sample.setPower(0);
//            }
//            else if(gamepad1.right_bumper){
//                sample.setPower(1);
//            }
//        }

        // for mecanum driving
        if (driveSwitch == 2) {//this one doesn't work nano-des
            frontLeft.setPower(gamepad1.right_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x); //right stick movement, left stick turning
            frontRight.setPower(-gamepad1.right_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x);
            backLeft.setPower(-gamepad1.right_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x);
            backRight.setPower(gamepad1.right_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x); //should have fixed it, but we'll use the other version for now
        }
        else if (driveSwitch == 1){
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
        lift.setPower(scaleInput(gamepad1.right_trigger) - scaleInput(gamepad1.left_trigger));



        //     GAMEPAD 2 *********************
        // for flipping
        flip.setPower(-gamepad2.right_stick_y);

        //intake
        if(gamepad2.right_bumper){
            intake.setPower(1);
        }
        else if(gamepad2.left_bumper){
            intake.setPower(-1);
        }
        else{
            intake.setPower(0);
        }

        //arm extension
        if(gamepad2.left_bumper && gamepad2.a){
            canArm = true;  //just in case
        }
        else if (gamepad2.left_bumper && gamepad2.right_bumper && !gamepad2.a){
            canArm = false;
        }
//        else if (arm.getCurrentPosition() >= 20){ //number of rotations
//            canArm = false;
//        }
        telemetry.addData("Arm Position: ", arm.getCurrentPosition());
        telemetry.update();

        if(canArm) {
            armVal = gamepad2.left_stick_y;
        }
        else{
            armVal = 0;
        }
        arm.setPower(armVal);


        if((gamepad1.a && gamepad1.dpad_left) || (gamepad2.a && gamepad2.dpad_left)) {
            stop();
        }
    }
    public void stop() {
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        lift.setPower(0);
//        marker.setPosition(0);
        intake.setPower(0);
        arm.setPower(0);
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
