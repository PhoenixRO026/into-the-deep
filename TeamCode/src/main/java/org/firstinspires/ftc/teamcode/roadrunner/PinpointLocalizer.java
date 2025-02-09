package org.firstinspires.ftc.teamcode.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.PinpointView;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Objects;

/** @noinspection unused*/
@Config
public final class PinpointLocalizer implements Localizer {
    public static class Params {
        public double parYTicks = 0.0; // y position of the parallel encoder (in tick units)
        public double perpXTicks = 0.0; // x position of the perpendicular encoder (in tick units)
    }

    public static Params PARAMS = new Params();
    public final PinpointView view;

    private final GoBildaPinpointDriver driver;

    private Pose2d txWorldPinpoint;
    private Pose2d txPinpointRobot = new Pose2d(0, 0, 0);

    public PinpointLocalizer(HardwareMap hardwareMap, Pose2d initialPose) {
        // TODO: make sure your config has a Pinpoint device with this name
        //   see https://ftc-docs.firstinspires.org/en/latest/hardware_and_software_configuration/configuring/index.html
        driver = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        //double mmPerTick = 25.4 * inPerTick;
        //driver.setEncoderResolution(1 / mmPerTick);
        //driver.setOffsets(mmPerTick * PARAMS.parYTicks, mmPerTick * PARAMS.perpXTicks);
        //NOTE FROM DRAGOS: we already know the encoder resolution for the gobilda 4 bar pod
        //  so we're gonna skip odo tuning completely, setting the inPerTick from MecanumDrive
        //  to inches per millimeter, and ignoring it here
        double mmPerInch = 25.4;
        driver.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        driver.setOffsets(mmPerInch * PARAMS.parYTicks, mmPerInch * PARAMS.perpXTicks);

        // TODO: reverse encoder directions if needed
        //    driver.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.REVERSED);

        driver.resetPosAndIMU();

        txWorldPinpoint = initialPose;

        view = getView();
    }

    @Override
    public void setPose(Pose2d pose) {
        txWorldPinpoint = pose.times(txPinpointRobot.inverse());
    }

    @Override
    public Pose2d getPose() {
        return txWorldPinpoint.times(txPinpointRobot);
    }

    @Override
    public PoseVelocity2d update() {
        driver.update();
        if (Objects.requireNonNull(driver.getDeviceStatus()) == GoBildaPinpointDriver.DeviceStatus.READY) {
            txPinpointRobot = new Pose2d(driver.getPosX() / 25.4, driver.getPosY() / 25.4, driver.getHeading());
            Vector2d worldVelocity = new Vector2d(driver.getVelX() / 25.4, driver.getVelY() / 25.4);
            Vector2d robotVelocity = Rotation2d.fromDouble(-driver.getHeading()).times(worldVelocity);
            return new PoseVelocity2d(robotVelocity, driver.getHeadingVelocity());
        }
        return new PoseVelocity2d(new Vector2d(0, 0), 0);
    }

    private PinpointView getView() {
        return new PinpointView() {
            @Override
            public void update() {
                driver.update();
            }

            @Override
            public int getParEncoderPosition() {
                return driver.getEncoderX();
            }

            @Override
            public int getPerpEncoderPosition() {
                return driver.getEncoderY();
            }

            @Override
            public float getHeadingVelocity() {
                return (float) driver.getHeadingVelocity();
            }

            //NOTE: not used by tuning
            @Override
            public void setParDirection(@NonNull DcMotorSimple.Direction direction) {}

            @Override
            public void setPerpDirection(@NonNull DcMotorSimple.Direction direction) {}
        };
    }
}
