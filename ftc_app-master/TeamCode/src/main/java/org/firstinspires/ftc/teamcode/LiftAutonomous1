package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import java.lang.Runtime;
import 	java.util.Timer;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="LiftAutonomous1", group="Autonomous")

public class LiftAutonomous extends OpMode {
    DcMotor lift;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;

    public void init() {
        rightFront = hardwareMap.DcMotor.get("rightFront");
        leftFront = hardwareMap.DcMotor.get("leftFront");
        rightBack = hardwareMap.DcMotor.get("rightBack");
        leftBack = hardwareMap.DcMotor.get("leftBack");
        lift = hardwareMap.DcMotor.get("lift");
    }

    public void loop() {
        telemetry.addData("Lift Power", lift);
    }

    public void start() {
        lift.setPower(-1);
        wait(2);
        drive(1,1,1,1);
        wait(2);
        drive(0,0,0,0);
    }

    public void wait(int time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    public void drive(int leftFrontPower, int leftBackPower, int rightFrontPower, int rightBackPower)
    {
        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightBackPower);
        rightBack.setPower(rightBackPower);

    }
}
