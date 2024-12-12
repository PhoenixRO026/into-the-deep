package org.firstinspires.ftc.teamcode.teleop.config

import org.firstinspires.ftc.teamcode.library.config.IMUConfig
import org.firstinspires.ftc.teamcode.library.config.MotorConfig
import org.firstinspires.ftc.teamcode.library.config.ServoConfig
import org.firstinspires.ftc.teamcode.robot.DriveConfig
import org.firstinspires.ftc.teamcode.robot.RobotConfig

val robotConfigGherla = RobotConfig(
    name = "Config Gherla",
    drive = DriveConfig(
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
        )
    ),/*
    motorLiftLeft = MotorConfig(
        deviceName = "liftLeft",
        direction = MotorConfig.Direction.FORWARD,
        runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
        zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
        resetEncoder = true
    ),
    motorLiftRight = MotorConfig(
        deviceName = "liftRight",
        direction = MotorConfig.Direction.REVERSE,
        runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
        zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
        resetEncoder = true
    ),
    servoExtend = ServoConfig(
        deviceName = "extend",
        direction = ServoConfig.Direction.FORWARD,
        range = ServoConfig.Range.DEFAULT
    ),*/
    servoArmLeft = ServoConfig(
        deviceName = "armLeft",
        direction = ServoConfig.Direction.REVERSE,
        range = ServoConfig.Range.DEFAULT
    ),
    servoArmRight = ServoConfig(
        deviceName = "armRight",
        direction = ServoConfig.Direction.FORWARD,
        range = ServoConfig.Range.DEFAULT
    )
)