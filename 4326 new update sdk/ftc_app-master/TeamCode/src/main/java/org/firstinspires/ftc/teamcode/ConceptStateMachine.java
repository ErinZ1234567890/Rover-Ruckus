package org.firstinspires.ftc.teamcode;
//This is a test for state machines, iterative opmode

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
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

    BNO055IMU               imu_sensor;

    double                  globalAngle;


    ModernRoboticsI2cRangeSensor rangeSensor;

    ArrayList<DcMotor> motors = new ArrayList<DcMotor>();

    static final double COUNTS_PER_MOTOR_REV = 1120;    // eg: Andymark Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = .75;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    int counter = 0;

   //turn states
    private gyroTurnByPID turn180_1;
    private gyroTurnByPID turn45_1;
    private gyroTurnByPID turn45_2;
    private gyroTurnByPID turnneg270_1;

  //move  states
    private driveState forward24_1;
    private driveState forward24_2;
    private driveState forward48_1;
    private driveState backward24_1;
    private driveState forward36_1;




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

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        imu_sensor = hardwareMap.get(BNO055IMU.class, "imu");

        imu_sensor.initialize(parameters);
        //imu_sensor.

        // Set all motors to zero power
        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

        //declare all the states
        turn180_1 = new gyroTurnByPID(90, .5,motors,imu_sensor,  false);
        forward24_1 = new driveState(24, .5, motors, "forward");
        turn45_1 = new gyroTurnByPID(45, .5, motors, imu_sensor, true);
        forward24_2 = new driveState(24, .5, motors, "forward");
        turn45_2  = new gyroTurnByPID(45, .5, motors, imu_sensor,true);
        forward48_1 = new driveState(48, .5, motors, "forward");
        backward24_1 =  new driveState(24, .5, motors, "backwards");
        turnneg270_1  = new gyroTurnByPID(90, .5, motors, imu_sensor,true);
        forward36_1 = new driveState(36, .6, motors, "forward");

        //setup transitions
        turn180_1.setNextState(null);
//        forward24_1.setNextState(turn45_1);
//        turn45_1.setNextState(forward24_2);
//        forward24_2.setNextState(turn45_2);
//        turn45_2.setNextState(forward48_1);
//        forward48_1.setNextState(backward24_1);
//        backward24_1.setNextState(turnneg270_1);
//        turnneg270_1.setNextState(forward36_1);
//        forward36_1.setNextState(null);


    }


    @Override
    public void start(){
        machine = new StateMachine(turn180_1);

    }


    @Override
    public void loop()  {


        machine.update();
        telemetry.addData("raw ultrasonic", rangeSensor.rawUltrasonic());
        telemetry.addData("raw optical", rangeSensor.rawOptical());
        telemetry.addData("cm optical", "%.2f cm", rangeSensor.cmOptical());
        telemetry.addData("cm", "%.2f cm", rangeSensor.getDistance(DistanceUnit.CM));

        telemetry.addData("imu heading:", imu_sensor.getAngularOrientation().firstAngle);

        telemetry.update();
    }

    private StateMachine machine;

    @Override
    public void stop() {
    }

}
