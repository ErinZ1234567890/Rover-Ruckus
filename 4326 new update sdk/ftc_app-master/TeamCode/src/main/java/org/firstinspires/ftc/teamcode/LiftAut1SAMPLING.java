package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@Autonomous(name="LiftAut1SAMPLE", group="Autonomous")

public class LiftAut1SAMPLING extends LinearOpMode {
    DcMotor lift;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    Servo marker;
    CRServo intake;
    DcMotor box;
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    int quarterTurn = 7; //mock turn factor that does a quarter turn at .8/.9 power***

    private static final String VUFORIA_KEY = "ATfbkf//////AAABmSdKrt64X0UDrXZRbiIdwVl9iuhdq1WQN1irJAz1O/XAe4vAgTnNCQsLzqtENwAZjOfmIvzpWoO8CD4VW6uZ6gGSYAv8gLSG4Ew+HLqDbKrN+gyhJPkxwiKDFXIHWeSNuGh3UUSKGj++8ggR9vYFTyLqXpvy2uwI+z66wWL3aPUU5KjK0N8oy5+IyddBgKGDHw2QacCqKJvMuL+VOOPNYdwKC3nQ+caRIS4gsJQwQ3FZrgY/oHgfse+vLRdoBKfhV2Pl6d2kqphlXivEWaPcvkOrpkkJvqR7aYwvkkO6Aqlph6YdLRp6okEauD6zly8s4rUqoCKmOd4cEx8TfamSqg/jhc4eRbN0koLdkOWL53nG";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;



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

        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }


        waitForStart();

        if (opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. */
            if (tfod != null) {
                tfod.activate();
            }

            while (opModeIsActive()) {  //just in case
                if (runOnce == false) {
                    //        liftAut();
                    int count  = 0;
                    int pos = 1000;

                    while(count < 10) {
                        if (tfod != null) {
                            // getUpdatedRecognitions() will return null if no new information is available since
                            // the last time that call was made.
                            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                            if (updatedRecognitions != null) {
                                telemetry.addData("# Object Detected", updatedRecognitions.size());
                                if (updatedRecognitions.size() == 3) {
                                    int goldMineralX = -1;
                                    int silverMineral1X = -1;
                                    int silverMineral2X = -1;
                                    for (Recognition recognition : updatedRecognitions) {
                                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                            goldMineralX = (int) recognition.getLeft();
                                        } else if (silverMineral1X == -1) {
                                            silverMineral1X = (int) recognition.getLeft();
                                        } else {
                                            silverMineral2X = (int) recognition.getLeft();
                                        }
                                    }
                                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                            telemetry.addData("Gold Mineral Position", "Left");
                                            pos = 0;
                                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                            telemetry.addData("Gold Mineral Position", "Right");
                                            pos = 1;

                                        } else {
                                            telemetry.addData("Gold Mineral Position", "Center");
                                            pos = 2;

                                        }
                                    }
                                }
                                telemetry.update();
                            }
                        }
                        count++;
                        telemetry.addData("position",pos);
                        telemetry.update();
                        wait(5);

                    }


                    lift.setPower(-.7);
                    wait(26);
                    lift.setPower(0);
                    wait(10);

                    //moves to lander
                    drive(.4, .4, .4, .4);
                    wait(3);
                    drive(.9, .9, -.9, -.9); //lander turn
                    wait(quarterTurn);
                    stopRobot();

                   if(pos == 1) {//right

                       drive(.9, .9, -.9, -.9);
                       wait(quarterTurn/2);
                       drive(.8, .8, .8, .8);
                       wait(6);
                       drive(-.9, -.9, .9, .9);
                       wait(quarterTurn/2);
                       drive(.8, .8, .8, .8);
                       wait(6);
                   } else if(pos == 0) { // left
                       drive(-.9, -.9, .9, .9);
                       wait(quarterTurn/3);
                       drive(.8, .8, .8, .8);
                       wait(7);
                       drive(.9, .9, -.9, -.9);
                       wait(quarterTurn/2);
                       drive(.8, .8, .8, .8);
                       wait(6);

                   }else {
                       drive(.8, .8, .7, .7);
                       wait(12);
                       stopRobot();
                       drive(-.3, -.3, .3, .3);
                       wait(3);
                   }

                    markerAut();

                    drive(.5, .5, -.5, -.5);
                    wait(1);
                    stopRobot();
                    drive(-.6, -.6, -.6, -.6);   //(was)moves backwards
                    wait(10);
                    stopRobot(); //safety ;3

                    stopRobot();
                    runOnce = true;
                } else {
                    telemetry.addData("text", "runOnce is done");
                }
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
    }
    public void liftDown(){
        lift.setPower(-.7); //lift down
        wait(20);
        lift.setPower(0);
        wait(10);
    }

    public void markerAut(){

//        drive(.8,.8,-.8,-.8);
//        wait(quarterTurn - 2); //used to be -1
        drive(-.7,-.7,.7,.7);
        wait(4);
        stopRobot();

        marker.setPosition(1); //shacky shake - used to be 1
        wait(8);
        marker.setPosition(0); //shake - used to be 0
        wait(3);
        marker.setPosition(1); //shacky shake - used to be 1
        wait(3);
        marker.setPosition(0); //shake - used to be 0
        wait(3);
        marker.setPosition(1); //shacky shake shake - used to be 1
        wait(3);
        marker.setPosition(0); //shake - used to be 0
        wait(9);

        stopRobot();
    }

    public void stopRobot(){
        drive(0,0,0,0);
        wait(3);
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
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}