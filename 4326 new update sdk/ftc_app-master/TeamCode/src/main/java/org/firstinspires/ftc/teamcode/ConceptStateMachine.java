package org.firstinspires.ftc.teamcode;
//This is a test for state machines, iterative opmode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.StateMachine; //necessary
import org.firstinspires.ftc.teamcode.StateMachine.State; //necessary
import java.util.ArrayList;
import java.util.Locale;


@Autonomous(name="State Machine Test1.2", group="Iterative Opmode")
public class ConceptStateMachine extends OpMode
{

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;

    DistanceSensor sensorDistance = new DistanceSensor() { //this seems unecessary, but trying to get out of that null pointer issue
        @Override
        public double getDistance(DistanceUnit unit) {
            return 0;
        }

        @Override
        public Manufacturer getManufacturer() {
            return null;
        }

        @Override
        public String getDeviceName() {
            return null;
        }

        @Override
        public String getConnectionInfo() {
            return null;
        }

        @Override
        public int getVersion() {
            return 0;
        }

        @Override
        public void resetDeviceConfigurationForOpMode() {

        }

        @Override
        public void close() {

        }
    };

    ArrayList<DcMotor> motors = new ArrayList<DcMotor>();

    static final double COUNTS_PER_MOTOR_REV = 1120;    // eg: Andymark Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    int counter = 0;

    private driveState moveForwardfor10seconds;
    private driveState Lturn;
    private driveState Rturn;
    private driveState strafeLeft;
    private timeState rTurn;
    private timeState lTurn;

    private distanceState senseMove;
    // Move left for time.
    private turnState ts;

    public class driveForward implements StateMachine.State {
        int newleftBackTarget;
        int newrightBackTarget;
        int  newleftFrontTarget;
        int  newrightFrontTarget;

        @Override
        public void start() {
            counter++;

            //Reset the encoders back to zero for the next movement
            rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            telemetry.addData("Path1",  "Running to %7d", newleftBackTarget);
            telemetry.addData("Path2",  "Running at lb %7d rb %7d lf %7d rf %7d",
                    leftBack.getCurrentPosition(),
                    rightBack.getCurrentPosition(),
                    leftFront.getCurrentPosition(),
                    rightFront.getCurrentPosition()
            );
            telemetry.update();

            //Bring them back to using encoders
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //Setting their target to their current encoder value (should be zero) to the amount of inches times the counts per inches

            newleftBackTarget = leftBack.getCurrentPosition() + (int) (20 * COUNTS_PER_INCH);
            newrightBackTarget = rightBack.getCurrentPosition() + (int) (20 * COUNTS_PER_INCH);
             newleftFrontTarget = leftFront.getCurrentPosition() + (int) (20 * COUNTS_PER_INCH);
             newrightFrontTarget = rightFront.getCurrentPosition() + (int) (20 * COUNTS_PER_INCH);


//            //setting our target posiions
//           leftBack.setTargetPosition(newleftBackTarget);
//           rightBack.setTargetPosition(newrightBackTarget);
//           leftFront.setTargetPosition(newleftFrontTarget);
//           rightFront.setTargetPosition(newrightFrontTarget);
//
//           leftBack.setPower(Math.abs(DRIVE_SPEED));
//           leftFront.setPower(Math.abs(DRIVE_SPEED));
//           rightBack.setPower(Math.abs(DRIVE_SPEED));
//           rightFront.setPower(Math.abs(DRIVE_SPEED));
        }

        @Override
        public State update() {

            if(newleftBackTarget > leftBack.getCurrentPosition()) {
                telemetry.addData("Path1",  "Running to %7d", newleftBackTarget);
                telemetry.addData("Path2",  "Running at lb %7d rb %7d lf %7d rf %7d",
                        leftBack.getCurrentPosition(),
                        rightBack.getCurrentPosition(),
                        leftFront.getCurrentPosition(),
                        rightFront.getCurrentPosition()
                        );
                telemetry.update();

          leftBack.setPower(Math.abs(DRIVE_SPEED));
           leftFront.setPower(Math.abs(DRIVE_SPEED));
           rightBack.setPower(Math.abs(DRIVE_SPEED));
           rightFront.setPower(Math.abs(DRIVE_SPEED));

                return this;
            }else {
                rightFront.setPower(0);
                leftFront.setPower(0);
                rightBack.setPower(0);
                leftBack.setPower(0);
                return null;
            }
        }
    }

    public class turnState implements StateMachine.State {

        private ElapsedTime runtime = new ElapsedTime();

        @Override
        public void start() {
            counter++;
            runtime.reset();
        }

        @Override
        public State update() {
            if(runtime.seconds() < 5) {
                rightFront.setPower(-DRIVE_SPEED);
                leftFront.setPower(DRIVE_SPEED);
                rightBack.setPower(-DRIVE_SPEED);
                leftBack.setPower(DRIVE_SPEED);
                return this;
            }else {
                rightFront.setPower(0);
                leftFront.setPower(0);
                rightBack.setPower(0);
                leftBack.setPower(0);
                return null;
            }
        }

    }

    @Override
    public void init() {
        rightFront = hardwareMap.dcMotor.get("right front");
        leftFront = hardwareMap.dcMotor.get("left front");
        rightBack = hardwareMap.dcMotor.get("right back");
        leftBack = hardwareMap.dcMotor.get("left back");
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.REVERSE);

        motors.add(rightFront);
        motors.add(leftFront);
        motors.add(rightBack);
        motors.add(leftBack);

        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensorDistance");


        // Set all motors to zero power
        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

        //declare all the states
        senseMove = new distanceState(2,6, motors);

//        moveForwardfor10seconds = new driveState(20, 5,  motors, "left");
//        strafeLeft = new driveState(20, 5, motors,"left");
//        ts = new turnState();
//        lTurn = new timeState(3,5,motors,"turnLeft");
//        rTurn = lTurn = new timeState(3,5,motors,"turnRight");
//        lTurn.setNextState(rTurn);
//
//        Lturn = new driveState(10,5,motors,"turnLeft");
//        Rturn = new driveState(10,5,motors,"turnRight");
//        Lturn.setNextState(Rturn);
//        Rturn.setNextState(lTurn);


        //setup the transitions
        //strafeLeft.setNextState(moveForwardfor10seconds);
       // simple.setNextState(moveForwardfor10seconds);
        //moveForwardfor10seconds.setNextState(simple);




    }

    @Override
    public void start(){
        machine = new StateMachine(senseMove);

    }


    @Override
    public void loop() {
        machine.update();
        telemetry.addData("Current State", counter);
        telemetry.addData("Distance (cm)",
                String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.INCH)));
        telemetry.update();
    }

    private StateMachine machine;


}
