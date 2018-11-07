
package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;

@Autonomous(name="ImuFun", group="Autonomous");

public class ImuTesting extends linearOpMode(){
        DcMotor leftFront;
        DcMotor rightFront;
        DcMotor leftBack;
        DcMotor rightBack;
        DcMotor lift;

    BNO055IMU imu;//The object
    Orientation anglces;//first set of angles
    Orientation lastAngles = new Orientation();

//angles and lastAngles are stored in the same place, use the change in angle to determine the overall rotation with the euler angles
    Acceleration gravity;

    double globalAngle=0;
    double lastAngle;
    @Override public void runOpMode() throws InterruptedException{
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

       // telemetry.addData("Mode","waiting for start");
        telemetry.addData("imu calib status", imu.getCalibrationStatus().toString());
        telemetry.update();
        globalAngle = 0;
        waitForStart();

        while(OpModeIsActive){
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        lift = hardwareMap.dcMotor.get("lift");
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            drivePower(-1,1,-1,1);
            //let's rotate by 90 degrees, then go then stop
        while(!(changeAngle>90&&changeAngle<95){
        drive(-1,1,-1,1);//turn to reach range
        }


           }

           public double getCurrentAngle(){
            return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        }
public void resetAngle()
        {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
        }
public double changeAngle(){
            double change = angles.firstAngle - lastAngles.firstAngles;
            globalAngle+=change;
            lastAngles = angles;
            return globalAngle;
        }
public void drivePower(double power) { //traditional way of movement
        leftFront.setPower(power);
        rightFront.setPower(power);
        leftBack.setPower(power);
        rightBack.setPower(power);
        }
public void drive(double leftFrontPower, double leftBackPower, double rightFrontPower, double rightBackPower)
        {
        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightBackPower);
        rightBack.setPower(rightBackPower);

        }

        }
        

        }