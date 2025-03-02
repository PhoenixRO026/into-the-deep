package org.firstinspires.ftc.teamcode.tele.config

import org.firstinspires.ftc.teamcode.library.config.AnalogEncoderCRServoConfig
import org.firstinspires.ftc.teamcode.library.config.ColorSensorConfig
import org.firstinspires.ftc.teamcode.library.config.EncoderConfig
import org.firstinspires.ftc.teamcode.library.config.IMUConfig
import org.firstinspires.ftc.teamcode.library.config.MotorConfig
import org.firstinspires.ftc.teamcode.library.config.ServoConfig
import org.firstinspires.ftc.teamcode.robot.config.DriveHardwareConfig
import org.firstinspires.ftc.teamcode.robot.config.IntakeHardwareConfig
import org.firstinspires.ftc.teamcode.robot.config.LiftHardwareConfig
import org.firstinspires.ftc.teamcode.robot.config.OuttakeHardwareConfig
import org.firstinspires.ftc.teamcode.robot.config.RobotHardwareConfig

val robotHardwareConfigTransilvaniaCollege = RobotHardwareConfig(
    name = "TransilvaniaCollege",
    drive = DriveHardwareConfig(
        imu = IMUConfig(
            deviceName = "imu",
            logoDirection = IMUConfig.LogoDirection.LEFT,
            usbDirection = IMUConfig.USBDirection.UP,
            resetYaw = true
        ),
        motorRF = MotorConfig(
            deviceName = "motorRF",
            direction = MotorConfig.Direction.FORWARD,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        ),
        motorRB = MotorConfig(
            deviceName = "motorRB",
            direction = MotorConfig.Direction.FORWARD,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        ),
        motorLF = MotorConfig(
            deviceName = "motorLF",
            direction = MotorConfig.Direction.REVERSE,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        ),
        motorLB = MotorConfig(
            deviceName = "motorLB",
            direction = MotorConfig.Direction.REVERSE,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        ),
        parEncoder = EncoderConfig(
            deviceName = "motorRF",
            direction = EncoderConfig.Direction.FORWARD
        ),
        perpEncoder = EncoderConfig(
            deviceName = "motorLB",
            direction = EncoderConfig.Direction.FORWARD
        )
    )
)