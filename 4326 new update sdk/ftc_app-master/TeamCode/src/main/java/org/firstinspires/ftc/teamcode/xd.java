package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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

@Autonomous(name="Ex Dee", group="Autonomous")

public class xd extends OpMode {
    DcMotor lift;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    Servo marker;

    public void init() {
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        lift = hardwareMap.dcMotor.get("lift");
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        marker = hardwareMap.servo.get("marker");
    }

    public void loop() {
        telemetry.addData("text", "Lift Power: " + lift);
    }

    public void start() {
        liftAut();
//        markerAut(); //for later testing...

        stopRobot();
    }

    public void liftAut(){
        //for now
        lift.setPower(-.5);
        wait(28);
        lift.setPower(0);
        wait(10);
        drive(-.5,-.5,-.5,-.5);
        wait(5);

        markerAut();

        stopRobot();
        lift.setPower(.5);
        wait(20);
        lift.setPower(0);
        stopRobot();
    }
    public void markerAut(){
        drive(5,-5,-5,5);
        wait(40);
        stopRobot();
        for(int x = 0; x <= 1; x++) {
            marker.setPosition(1); //shaky shake
            wait(12);
            marker.setPosition(0);
            wait(12);
        }
        stopRobot();
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
        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightBackPower);
        rightBack.setPower(rightBackPower);

    }
}