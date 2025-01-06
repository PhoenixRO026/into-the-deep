package org.firstinspires.ftc.teamcode.tele.config

import org.firstinspires.ftc.teamcode.library.config.AnalogEncoderCRServoConfig
import org.firstinspires.ftc.teamcode.library.config.EncoderConfig
import org.firstinspires.ftc.teamcode.library.config.IMUConfig
import org.firstinspires.ftc.teamcode.library.config.MotorConfig
import org.firstinspires.ftc.teamcode.library.config.ServoConfig
import org.firstinspires.ftc.teamcode.robot.config.DriveHardwareConfig
import org.firstinspires.ftc.teamcode.robot.config.IntakeHardwareConfig
import org.firstinspires.ftc.teamcode.robot.config.LiftHardwareConfig
import org.firstinspires.ftc.teamcode.robot.config.OuttakeHardwareConfig
import org.firstinspires.ftc.teamcode.robot.config.RobotHardwareConfig

val robotHardwareConfigWonder = RobotHardwareConfig(
    name = "Wonderland",
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
    ),
    lift = LiftHardwareConfig(
        motorLiftRight = MotorConfig(
            deviceName = "motorLiftRight",
            direction = MotorConfig.Direction.REVERSE,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        ),
        motorLiftLeft = MotorConfig(
            deviceName = "motorLiftLeft",
            direction = MotorConfig.Direction.FORWARD,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        ),
        encoder = EncoderConfig(
            deviceName = "motorRB",
            direction = EncoderConfig.Direction.REVERSE
        )
    ),
    intake = IntakeHardwareConfig(
        motorExtendoIntake = MotorConfig(
            deviceName = "motorExtendoIntake",
            direction = MotorConfig.Direction.FORWARD,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        ),
        encoderExtendo = EncoderConfig(
            deviceName = "motorLF",
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
            direction = ServoConfig.Direction.REVERSE,
            pwmRange = ServoConfig.PWMRange.DEFAULT,
            rangeScale = 0.602..0.775
        ),
        servoBoxTilt = ServoConfig(
            deviceName = "servoBoxTilt",
            direction = ServoConfig.Direction.REVERSE,
            pwmRange = ServoConfig.PWMRange.DEFAULT,
            rangeScale = 0.0..1.0
        ),
    ),
    outtake = OuttakeHardwareConfig(
        servoExtendo = AnalogEncoderCRServoConfig(
            servoDeviceName = "servoExtendoOuttake",
            encoderDeviceName = "encoderExtendoOuttake",
            direction = AnalogEncoderCRServoConfig.Direction.FORWARD,
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
            rangeScale = 0.353..0.6
        ),
    )
)