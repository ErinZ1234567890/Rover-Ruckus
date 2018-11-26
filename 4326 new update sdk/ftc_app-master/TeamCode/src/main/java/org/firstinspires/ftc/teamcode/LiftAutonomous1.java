package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.Runtime;
import java.util.Timer;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="LiftAutonomous1", group="Autonomous")

public class LiftAutonomous1 extends LinearOpMode {
    DcMotor lift;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    Servo marker;

    boolean robotIsDown = false;
    int quarterTurn = 20; //mock turn factor that does a quarter turn (more accurate)

    boolean runOnce = false;
    public void runOpMode() {
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        lift = hardwareMap.dcMotor.get("lift");
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        marker = hardwareMap.servo.get("marker");
        waitForStart();

        while (opModeIsActive()) {  //just in case
            if(runOnce == false) {
                    //        liftAut();
                    telemetry.addData("text", "the code was updated doof, ur just bad");
                    lift.setPower(-.7);
                    wait(23);
                    lift.setPower(0);
                    wait(10);
                    robotIsDown = true; //delete later if it runs the first time


                    boolean liftOnly = false; // change to true for only lifting
                    telemetry.addData("text", "robotIsDown: " + robotIsDown);
                    wait(10);

                    drive(.5, .5, .5, .5);
                    wait(2);
                    drive(0,0,0,0);
                    wait(10);
                   drive(1,1,-1,-1);
                    wait(5);
                   drive(0,0,0,0);
                   wait(20);
                  drive(1, 1, 1, 1);
                  wait(20);


                //            if (robotIsDown) {
    //                if (liftOnly) {
    //                    drive(5, 5, 5, 5); //drive forward
    //                    wait(5);
    //                    stopRobot();
    //
    //                    liftDown();
    //
    //                    drive(-3, -3, -3, -3); //drive back a bit
    //                    wait(5);
    //                }
    //                else {
    //                    drive(3,3,3,3);
    //                    wait(10);
    //                    drive(-5, -5, 4, 4);
    //                    wait(quarterTurn);
    //                    stopRobot();
    //
    //                    telemetry.addData("text", "Robot should Life Down Now ;)");
    //                    wait(20);
    //                    liftDown();
    //
    //                    markerAut();  //***
    //                    //                landerAut();
    //                }
    //            }
    //            else {
    //                telemetry.addData("text", "Robot is now down according to code, fix variable robotIsDown: " + robotIsDown);
    //            }
                    stopRobot();
                    runOnce = true;
            }
            else{
                telemetry.addData("text", "runOnce is done");
            }
        }
    }
//    public void loop() {
//        telemetry.addData("text", "Lift Power: " + lift);
//    }
//
//    public void start() {
//
//        stopRobot();
//    }

    public void liftAut(){
      //for now
        lift.setPower(-.5);
        wait(26);
        lift.setPower(0);
        wait(10);
        robotIsDown = true; //delete later if it runs the first time
    }
    public void liftDown(){
        lift.setPower(-.7); //lift down
        wait(20);
        lift.setPower(0);
        wait(10);
    }

    public void markerAut(){
        drive(5,5,5,5);
        wait(40);
        stopRobot();

        drive(-5,-5,5,5);
        wait(quarterTurn);
        stopRobot();

        for(int x = 0; x <= 1; x++) {
            marker.setPosition(1); //shacky shake
            wait(9);
            marker.setPosition(0);
            wait(9);
        }
    }
    public void landerAut(){
        drive(2,2,-2,-2);
        wait(quarterTurn/2); //45 degrees suposedly

        drive(5,5,5,5);
        wait(50);
        stopRobot(); //safety ;3
    }

    public void stopRobot(){
        drive(0,0,0,0);
        wait(5);
    }

    public void wait(int time) {
        try {
            Thread.sleep(time * 100);//milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    public void drive(double leftFrontPower, double leftBackPower, double rightFrontPower, double rightBackPower)
    {
        leftFront.setPower(10*leftFrontPower);
        leftBack.setPower(10*leftBackPower);
        rightFront.setPower(10*rightBackPower);
        rightBack.setPower(10*rightBackPower);

    }
}
