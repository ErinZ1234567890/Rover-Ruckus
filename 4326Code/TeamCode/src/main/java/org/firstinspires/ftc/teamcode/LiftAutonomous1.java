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

@Autonomous(name="LiftAutonomous1", group="Autonomous")

public class LiftAutonomous1 extends OpMode {
    DcMotor lift;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;

    public void init() {
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        lift = hardwareMap.dcMotor.get("lift");
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);
    }

    public void loop() {
        telemetry.addData("text", "Lift Power: " + lift);
    }

     public void start() {
        lift.setPower(-.5);
        wait(20);
        lift.setPower(0);
        wait(10);
        tryingtostopstopping=true;
        drive(0,0,.5,.5);
        wait(7.5);
        marker();
        /*drive(-0.5,-.5,-.5,-.5);
        wait(5);
        drive(0,0,0,0);
       wait(5);
       lift.setPower(.5);
       wait(20);
        drive(-0.5,-.5,-.5,-.5);
        wait(5);*/

        drive(0,0,0,0);
    }

    public void marker(){
        if(tryingtostopstopping){
            drive(.5,.5,.5,.5);
            wait(15);
            drive(0,0,0,0);
            wait(5);
            drive(-.5,-.5,.5,.5);
            wait(20);
            drive(0,0,0,0);
            wait(10);
            drive(.5,.5,-.5,-.5);
            wait(25);
            drive(1,1,1,1);
            wait(20);
        }
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
