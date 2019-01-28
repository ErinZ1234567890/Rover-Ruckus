//package org.firstinspires.ftc.robotcontroller.external.samples;
package org.firstinspires.ftc.teamcode;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;



import java.util.Locale;

/*
 * This is an example LinearOpMode that shows how to use
 * the REV Robotics Color-Distance Sensor.
 *
 * It assumes the sensor is configured with the name "sensor_color_distance".
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 */
@Autonomous(name = "Fun color testing*", group = "Sensor")
//@Disabled                            // Comment this out to add to the opmode list
public class ColorFun extends LinearOpMode {                              //;_;

    /**
     * Note that the REV Robotics Color-Distance incorporates two sensors into one device.
     * It has a light/distance (range) sensor.  It also has an RGB color sensor.
     * The light/distance sensor saturates at around 2" (5cm).  This means that targets that are 2"
     * or closer will display the same value for distance/light detected.
     *
     * Although you configure a single REV Robotics Color-Distance sensor in your configuration file,
     * you can treat the sensor as two separate sensors that share the same name in your op mode.
     *
     * In this example, we represent the detected color by a hue, saturation, and value color
     * model (see https://en.wikipedia.org/wiki/HSL_and_HSV).  We change the background
     * color of the screen to match the detected color.
     *
     * In this example, we  also use the distance sensor to display the distance
     * to the target object.  Note that the distance sensor saturates at around 2" (5 cm).
     *
     */
    ColorSensor sensorColor;
   DistanceSensor sensorDistance;
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

    String orientation = "nothing"; //for guessing factor

    boolean runOnce = true;

    @Override
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

        // get a reference to the color sensor.
        sensorColor = hardwareMap.get(ColorSensor.class, "sensorColor");

        // get a reference to the distance sensor that shares the same name.
        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensorDistance");

        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attentuate the measured values.
        final double SCALE_FACTOR = 255;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);


        // wait for the start button to be pressed.
        waitForStart();


        // loop and read the RGB and distance data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive()) {
            // convert the RGB values to HSV values.
            // multiply by the SCALE_FACTOR.
            // then cast it back to int (SCALE_FACTOR is a double)
            Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                    (int) (sensorColor.green() * SCALE_FACTOR),
                    (int) (sensorColor.blue() * SCALE_FACTOR),
                    hsvValues);

            // send the info back to driver station using telemetry function.
            telemetry.addData("Distance (cm)",
                    String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
            telemetry.addData("Alpha", sensorColor.alpha());
            telemetry.addData("Red  ", sensorColor.red());
            telemetry.addData("Green", sensorColor.green());
            telemetry.addData("Blue ", sensorColor.blue());
            telemetry.addData("Hue", hsvValues[0]);

            // change the background color to match the color detected by the RGB sensor.
            // pass a reference to the hue, saturation, and value array as an argument
            // to the HSVToColor method.
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                }
            });

            while(runOnce) {  //only for the sensing part
                colorSense();
                runOnce = false;
                
//                mineralMove(orientation);
                //move to depo
            }
            telemetry.addData("Info", "Info: " + orientation);
            telemetry.update();
        }
        //go up to 5cm close to the minerals - at that point it will read the same thing
//        How is 2022?



        // Set the panel back to the default color
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        });
    }
    public void colorSense() {
        encoderDrive(100,7.85,7.85,7.85,7.85);
        double[] mineral1 = new double[3];//[0] = red data, [1] = blue data, [2] = green data
        mineral1 = getMineral(mineral1);
        telemetry.addData("Alpha", sensorColor.alpha());
        telemetry.addData("Red  ", sensorColor.red());
        telemetry.addData("Green", sensorColor.green());
        telemetry.addData("Blue ", sensorColor.blue());
       // telemetry.addData("Hue", hsvValues[0]);

        encoderDrive(85, -7.5,7.5,7.5,-7.5);
        wait(5);
        encoderDrive(30, .9,.9,.9,.9);
        double[] mineral2 = new double[3];//[0] = red data, [1] = blue data, [2] = green data
        mineral2 = getMineral(mineral2);
        telemetry.addData("Alpha", sensorColor.alpha());
        telemetry.addData("Red  ", sensorColor.red());
        telemetry.addData("Green", sensorColor.green());
        telemetry.addData("Blue ", sensorColor.blue());
       // telemetry.addData("Hue", hsvValues[0]);

        double samePercentFactor = .24;
        int ptVal = 0;
        if(Math.abs(mineral1[2]-mineral2[2]) < (mineral1[2] + mineral2[2])/2 * samePercentFactor){
            ptVal += 2;
        }
        if(Math.abs(mineral1[0]-mineral2[0]) < (mineral1[2] + mineral2[2])/2 * samePercentFactor){
            ptVal += 1;
        }
        if(Math.abs(mineral1[1]-mineral2[1]) < (mineral1[2] + mineral2[2])/2 * samePercentFactor){
            ptVal += 2;
        }
        if (ptVal >= 3) {
            orientation = "right";
        }
        else {
            int moreVal = 0;
            if (mineral1[2] > mineral2[2] && mineral1[1] > mineral2[2]) {
                orientation = "left";
            }
            else { //presumably, if the second mineral has a significant amount of blue present...
                orientation = "center";
            }
        }

//        if(Math.abs(mineral1[2]-mineral2[2]) < 25 && Math.abs(mineral1[0]-mineral2[0]) < 15 && Math.abs(mineral1[1]-mineral2[1]) < 20){//||(mineral2[1]-mineral1[1]) < 10 && (mineral2[0]-mineral1[0]) < 15)) { //accounting for negative numbers - If both minerals are rather close...
//             orientation = "right";
//        }
//        else {
//            if ((mineral1[2] - 25) > mineral2[2] && mineral1[1] - 20 > mineral2[1]) { //If the center mineral has more blue present...
//                orientation = "left";
//            } else { //presumably, if the second mineral has a significant amount of blue present...
//                orientation = "center";
//            }
//        }
            /* GOLD
            * blue: 22 - 31
            * red: 28 - 39
            * green: "very low"
            * */

            /* SILVER
            * blue: 30 -  72
            * red: 35 - 60
            * green: about blue?
            * */

        telemetry.addData("Info", "Info: " + orientation);
        telemetry.update();
    }
    public double[] getMineral(double[] mineral) {//RGB in array order
        int sensingTime = 26;
        double[] redVals = new double[sensingTime];
        double finalRed = 0;
        double[] greenVals = new double[sensingTime];
        double finalGreen = 0;
        double[] blueVals = new double[sensingTime];
        double finalBlue = 0;

        for(int x = 0; x < sensingTime; x++) {
            redVals[x] = sensorColor.red();
            greenVals[x] = sensorColor.green();
            blueVals[x] = sensorColor.blue();
            wait(1);
        }
        for(int x = sensingTime-1; x >= 0; x--){
            finalRed += redVals[x];
            finalGreen += greenVals[x];
            finalBlue += blueVals[x];
        }
        mineral[0] = (finalRed / sensingTime);
        mineral[1] = (finalGreen / sensingTime);
        mineral[2] = (finalBlue / sensingTime);

        return mineral;
    }
    
    public void mineralMove(String newOrient){ //meant to take in the orientation variable and move the mineral a little
        wait(5);
        if(newOrient == "left"){
            encoderDrive(70, 2,2,2,2);
        }
        else if(newOrient == "center"){
            encoderDrive(100, 7.5,-7.5,-7.5,7.5); //straf's back
            wait(1);
            encoderDrive(50, 2,2.1,2,2); //pushes block
        }
        else if(newOrient == "right"){
            encoderDrive(100, 15,-15,-15,15); //straf's double the distance
            wait(1);
            encoderDrive(50, 2.5,2.6,2.5,2.6); //pushes block
        }
        else{
            telemetry.addData("Error Report", "mineralMove error...");
        }
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
    public void wait(int time) {
        try {
            Thread.sleep(time * 100);//milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
