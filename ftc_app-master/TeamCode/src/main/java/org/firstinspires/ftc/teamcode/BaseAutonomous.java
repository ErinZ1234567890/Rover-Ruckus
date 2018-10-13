//package com.qualcomm.ftcrobotcontroller.opmodes;
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

@Autonomous(name="ColorAutonomous", group="ColorAutonomous")  //AUTONOMOUS!

public class ColorSensorCode_Autonomous extends OpMode {

    DcMotor leftBack;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor rightBack;
    Servo marker;

    public void start() {
    }
    
    public void init(){
        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        marker  = hardwareMap.dcMotor.get("marker");

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        marker.setPosition(0); //for consistency sake
    }

    // Drive Methods //
    public void drivePower(double power) { //traditional way of movement
        leftFront.setPower(power / 100);
        rightFront.setPower(power / 100);
        leftBack.setPower(power / 100);
        rightBack.setPower(power / 100);
    }
    public void driveForward(int pos1, int pos2, int pos3, int pos4, int newTime) {
        while (runtime.seconds() <= (runtime.seconds() + newTime)) {
            leftFront.setPower(pos1 / 10);
            leftBack.setPower(pos2 / 10);
            rightFront.setPower(pos3 / 10);
            rightBack.setPower(pos4 / 10);
        }
        wait(5);
        runtime.reset();
    }
    public void turnRight(pos1, pos2){
        while (runtime.seconds() <= (runtime.seconds() + newTime)) {
            leftFront.setPower(pos1 / 10);
            leftBack.setPower(pos2 / 10);
        }
        wait(5);
        runtime.reset();
    }
    public void turnLeft(pos3, pos4){
        while (runtime.seconds() <= (runtime.seconds() + newTime)) {
            rightFront.setPower(pos3 / 10);
            rightBack.setPower(pos4 / 10);
        }
        wait(5);
        runtime.reset();
    }
    //OTHER USEFULL METHODS
    public void stopRobot() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
        wait(1);
    }
    public void wait(int time) {
        try {
            Thread.sleep(time * 100); //10 = 1sec
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public fullAut() { //theoretically this is what it should look like if we used the servo-inclined plane idea...
        driveForward(5,5,-5,-5, fullTurnFactor/4);
        driveForward(8,8,8,8, 45); //moves towards merker area
        marker.setPosition(1); //releases marker...
        wait(25);
        market.setPosition(0); //retracts servo
        wait(10);
        driveForward(-4,-4,-4,-4, 10); //takes a "step" back
        driveForward(5,5,-5,-5, fullTurnFactor/2); //rotates towards field for teleop :)
        //stop robot
    }
    //actual running code
    int fullTurnFactor = 30; //changable variable that dictates the turns of a robot (units: seconds/10)   ***************************
    public void loop(){
        // TIME CHECK
        if ( time < 1) return;

        //DRIVE
        driveForward(5,5,-5,-5,fullTurnFactor); //supposed to turn 360 degrees --> test
        wait(15); //waits 2 secons added to the normal .5sec wait time
        driveForward(-5,-5,5,5,fullTurnFactor); //theoretically turns back  
        wait(45);
        driveForward(5,5,-5,-5,fullTurnFactor/4); // 90degrees
        wait(15);
        driveForward(5,5,-5,-5,fullTurnFactor/4);
        
        stopRobot();
    }

}
