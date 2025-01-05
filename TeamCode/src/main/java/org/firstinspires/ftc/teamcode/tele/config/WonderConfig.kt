package org.firstinspires.ftc.teamcode.tele.config

import org.firstinspires.ftc.teamcode.library.config.EncoderConfig
import org.firstinspires.ftc.teamcode.library.config.IMUConfig
import org.firstinspires.ftc.teamcode.library.config.MotorConfig
import org.firstinspires.ftc.teamcode.library.config.ServoConfig
import org.firstinspires.ftc.teamcode.robot.config.DriveConfig
import org.firstinspires.ftc.teamcode.robot.config.IntakeConfig
import org.firstinspires.ftc.teamcode.robot.config.LiftConfig
import org.firstinspires.ftc.teamcode.robot.config.OuttakeConfig
import org.firstinspires.ftc.teamcode.robot.config.RobotConfig

val robotConfigWonder = RobotConfig(
    name = "Wonderland",
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
    ),
    lift = LiftConfig(
        motorLiftRight = MotorConfig(
            deviceName = "motorLiftRight",
            direction = MotorConfig.Direction.FORWARD,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        ),
        motorLiftLeft = MotorConfig(
            deviceName = "motorLiftLeft",
            direction = MotorConfig.Direction.REVERSE,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        ),
        encoder = EncoderConfig(
            deviceName = "motorRF",
            direction = EncoderConfig.Direction.FORWARD
        )
    ),
    intake = IntakeConfig(
        motorExtendoIntake = MotorConfig(
            deviceName = "motorExtendoIntake",
            direction = MotorConfig.Direction.FORWARD,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        ),
        encoderExtendo = EncoderConfig(
            deviceName = "motorRB",
            direction = EncoderConfig.Direction.FORWARD
        ),
        motorSweeper = MotorConfig(
            deviceName = "motorSweeper",
            direction = MotorConfig.Direction.FORWARD,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        ),
        servoIntakeTilt = ServoConfig(
            deviceName = "servoIntakeTilt",
            direction = ServoConfig.Direction.FORWARD,
            pwmRange = ServoConfig.PWMRange.DEFAULT,
            rangeScale = 0.0..1.0
        ),
        servoBoxTilt = ServoConfig(
            deviceName = "servoBoxTilt",
            direction = ServoConfig.Direction.FORWARD,
            pwmRange = ServoConfig.PWMRange.DEFAULT,
            rangeScale = 0.0..1.0
        ),
    ),
    outtake = OuttakeConfig(
        servoExtendo = ServoConfig(
            deviceName = "servoExtendoOuttake",
            direction = ServoConfig.Direction.FORWARD,
            pwmRange = ServoConfig.PWMRange.DEFAULT,
            rangeScale = 0.0..1.0
        ),
        servoShoulder = ServoConfig(
            deviceName = "servoShoulder",
            direction = ServoConfig.Direction.FORWARD,
            pwmRange = ServoConfig.PWMRange.DEFAULT,
            rangeScale = 0.0..1.0
        ),
        servoElbow = ServoConfig(
            deviceName = "servoElbow",
            direction = ServoConfig.Direction.FORWARD,
            pwmRange = ServoConfig.PWMRange.DEFAULT,
            rangeScale = 0.0..1.0
        ),
        servoWrist = ServoConfig(
            deviceName = "servoWrist",
            direction = ServoConfig.Direction.FORWARD,
            pwmRange = ServoConfig.PWMRange.DEFAULT,
            rangeScale = 0.0..1.0
        ),
        servoClaw = ServoConfig(
            deviceName = "servoClaw",
            direction = ServoConfig.Direction.FORWARD,
            pwmRange = ServoConfig.PWMRange.DEFAULT,
            rangeScale = 0.0..1.0
        ),
    )
)