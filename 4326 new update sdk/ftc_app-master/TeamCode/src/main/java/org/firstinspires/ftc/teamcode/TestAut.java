package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="Testing Aut", group="Autonomous")

public class TestAut extends LinearOpMode {
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;

    private ElapsedTime runtime = new ElapsedTime();

    boolean runOnce = false;

    public void runOpMode() {
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()) {  //just in case
            if (runOnce == false) {
                encoderDrive(100,10,-10,-10,10, 20); //2 secs ?
                encoderDrive(100,-10,10,10,-10, 20);
                wait(5);
                encoderDrive(100,10,-10,-10,10);
                encoderDrive(100,-10,10,10,-10);
                runOnce = true; //to make sure it doesnt crash at the end
            } else {
                telemetry.addData("text", "runOnce is done");
            }
        }
    }

    public void stopRobot() {
        drive(0, 0, 0, 0);
        wait(3);
    }

    public void wait(int time) {
        try {
            Thread.sleep(time * 100);//milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void drive(double leftFrontPower, double leftBackPower, double rightFrontPower, double rightBackPower) { //old drive for refrence
        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightBackPower);
        rightBack.setPower(rightBackPower);

    }

    public void encoderDrive(double power,
                             double rightfrontInches, double leftfrontInches, double rightbackInches, double leftbackInches,
                             double timeoutS) {
        power /= 100;
        int newleftBackTarget;
        int newrightBackTarget;
        int newleftFrontTarget;
        int newrightFrontTarget;
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newleftBackTarget = leftBack.getCurrentPosition() + (int) (leftbackInches * COUNTS_PER_INCH);
            newrightBackTarget = rightBack.getCurrentPosition() + (int) (rightbackInches * COUNTS_PER_INCH);
            newleftFrontTarget = leftFront.getCurrentPosition() + (int) (leftfrontInches * COUNTS_PER_INCH);
            newrightFrontTarget = rightFront.getCurrentPosition() + (int) (rightfrontInches * COUNTS_PER_INCH);
            leftBack.setTargetPosition(newleftBackTarget);
            rightBack.setTargetPosition(newrightBackTarget);
            leftFront.setTargetPosition(newleftFrontTarget);
            rightFront.setTargetPosition(newrightFrontTarget);

            // Turn On RUN_TO_POSITION
            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            leftBack.setPower(Math.abs(power));
            rightBack.setPower(Math.abs(power));
            leftFront.setPower(Math.abs(power));
            rightFront.setPower(Math.abs(power));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is “safer” in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() && (runtime.seconds() < (timeoutS/10)) &&
                    (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy()) ) {
                telemetry.addData("unknown", "is running...");
                telemetry.update();
                // Display it for the driver.
            }

            // Stop all motion;
            leftBack.setPower(0);
            rightBack.setPower(0);
            leftFront.setPower(0);
            rightFront.setPower(0);
            // Turn off RUN_TO_POSITION
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move
        }
    }
    public void encoderDrive(double power,
                             double rightfrontInches, double leftfrontInches, double rightbackInches, double leftbackInches) {
        power /= 100;
        int newleftBackTarget;
        int newrightBackTarget;
        int newleftFrontTarget;
        int newrightFrontTarget;
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newleftBackTarget = leftBack.getCurrentPosition() + (int) (leftbackInches * COUNTS_PER_INCH);
            newrightBackTarget = rightBack.getCurrentPosition() + (int) (rightbackInches * COUNTS_PER_INCH);
            newleftFrontTarget = leftFront.getCurrentPosition() + (int) (leftfrontInches * COUNTS_PER_INCH);
            newrightFrontTarget = rightFront.getCurrentPosition() + (int) (rightfrontInches * COUNTS_PER_INCH);
            leftBack.setTargetPosition(newleftBackTarget);
            rightBack.setTargetPosition(newrightBackTarget);
            leftFront.setTargetPosition(newleftFrontTarget);
            rightFront.setTargetPosition(newrightFrontTarget);

            // Turn On RUN_TO_POSITION
            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            leftBack.setPower(Math.abs(power));
            rightBack.setPower(Math.abs(power));
            leftFront.setPower(Math.abs(power));
            rightFront.setPower(Math.abs(power));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is “safer” in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() && (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy()) ) {
                telemetry.addData("unknown", "is running...");
                telemetry.update();
                // Display it for the driver.
            }

            // Stop all motion;
            leftBack.setPower(0);
            rightBack.setPower(0);
            leftFront.setPower(0);
            rightFront.setPower(0);
            // Turn off RUN_TO_POSITION
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move
        }
    }
}