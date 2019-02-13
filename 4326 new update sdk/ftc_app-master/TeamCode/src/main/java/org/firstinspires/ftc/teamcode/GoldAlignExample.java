/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.configuration.annotations.AnalogSensorType;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="GoldAlign Example1.0", group="DogeCV")

public class GoldAlignExample extends OpMode
{
    // Detector object
    private GoldAlignDetector detector;
    CRServo sample;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    DcMotor lift;

    private ElapsedTime runtime = new ElapsedTime();

    boolean detected = false; //- 2/12/19
    boolean lifted = true; //- 2/12/19
    boolean outofLander = false;
    int counter = 0;
    int pos = 1000; //center = 0; right = 1; left == 2


    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;


    @Override
    public void init() {
        telemetry.addData("Status", "DogeCV 2018.0 - Gold Align Example");

        sample = hardwareMap.crservo.get("sample");

        // Set up detector
        detector = new GoldAlignDetector(); // Create detector
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(), 1, false); // Initialize detector with app context and camera
        detector.useDefaults(); // Set detector to use default settings

        // Optional tuning
        detector.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005; //

        detector.ratioScorer.weight = 5; //
        detector.ratioScorer.perfectRatio = 1.0; // Ratio adjustment

        detector.enable(); // Start the detector!

        lift = hardwareMap.dcMotor.get("lift1");

        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
    }

    /*
     * Code to run REPEATEDLY when the driver hits INIT
     */
    @Override
    public void init_loop() {

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
//         liftMove(1, 70, 4);
//         encoderDrive(1, 5,5,-5,-5,3);
//         lifted = true;
    }

    /*
     * Code to run REPEATEDLY when the driver hits PLAY
     */
    @Override
    public void loop() {


        telemetry.addData("IsAligned" , detector.getAligned()); // Is the bot aligned with the gold mineral?
        telemetry.addData("X Pos" , detector.getXPosition()); // Gold X position
        telemetry.addData("counter", counter);
        telemetry.addData("Position", pos);
        telemetry.update();
//        sample.setPower(-.2);

        if(detected == false) {
            counter++;
            if (!detector.getAligned()) {//if the camera has not seen the gold mineral //-.25to-.35  -.65to-.75   -.9to-1.1
                drive(-.35, -.35, .35, .35); //spin until the mineral is seen

            } else {  //drive toward the gold mineral to knock it out once it is seen
                drive(0, 0, 0, 0);
                wait(2);
                drive(.35, .35, -.35, -.35);
                wait(5);
                drive(0, 0, 0, 0);
                wait(2);
                drive(-.5, -.5,-.5,-.5);
                wait(15);
                drive(0, 0, 0, 0);

                telemetry.addData("Reached!!!!!!!!!!", counter);
                telemetry.update();
                detected  = true;
//                for(int i = 0; i < 5; i++) {
//                    if(!detector.getAligned()) {
//
//                        detected = false;
//                    }
//                }
                if(detected) {

                    drive(0,0,0,0);
                    telemetry.addData("Reached and correctly stopped!!", counter);
                    telemetry.update();

                }

            }
            if(counter > 300) {
                pos =2;
                detected = true;
                drive(.5, .5,-.5,-.5);
                wait(15);
                drive(0, 0, 0, 0);
                wait(2);
                drive(-.5, -.5,-.5,-.5);
                wait(15);
                drive(0, 0, 0, 0);
            }
        }
        else {
            if(counter < 150) {
                pos = 0;
            }else if(counter < 300) {
                pos = 1;
            }else {
                pos = 2;
            }
        }



    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        // Disable the detector
        detector.disable();
    }

    public void wait(int time) {
        try {
            Thread.sleep(time * 100);//milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public String getOrientation(double power) {
        if(power > -.25 && power < -.35) {
            return "left";
        }else if(power > -.65 && power < -.75) {
            return "center";
        }else if(power < -0.9) {
            return "right";
        }  else {
            return "failure";
        }
    }

    public void drive(double leftFrontPower, double leftBackPower, double rightFrontPower, double rightBackPower)
    {
        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightFrontPower);
        rightBack.setPower(rightBackPower);

    }

public void liftMove(double power,
                      double liftInches,
                      double timeoutS) {
    int liftTarget;

    // Ensure that the opmode is still active


        // Determine new target position, and pass to motor controller
        liftTarget = lift.getCurrentPosition() + (int) (liftInches * COUNTS_PER_INCH);

        lift.setTargetPosition(liftTarget);

        // Turn On RUN_TO_POSITION
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        lift.setPower(Math.abs(power));


        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is “safer” in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        while (
                (runtime.seconds() < timeoutS/10) &&
                (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy()) && lift.isBusy()) {

            // Display it for the driver.


        }



}

    public void encoderDrive(double power,
                             double rightfrontInches, double leftfrontInches, double rightbackInches, double leftbackInches,
                             double timeoutS) {
        int newleftBackTarget;
        int newrightBackTarget;
        int newleftFrontTarget;
        int newrightFrontTarget;
        // Ensure that the opmode is still active


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
            while (
                    (runtime.seconds() < (timeoutS/10)) &&
                    (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy()) && lift.isBusy()) {

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

        }




}
