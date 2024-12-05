package org.firstinspires.ftc.teamcode.teleop.config

import org.firstinspires.ftc.teamcode.library.config.MotorConfig
import org.firstinspires.ftc.teamcode.library.config.ServoConfig
import org.firstinspires.ftc.teamcode.robot.RobotConfig

val robotConfigGherla = RobotConfig(
    configName = "Config Gherla",
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
    servoExtendLeft = ServoConfig(
        deviceName = "extendLeft",
        direction = ServoConfig.Direction.REVERSE
    ),
    servoExtendRight = ServoConfig(
        deviceName = "extendRight",
        direction = ServoConfig.Direction.FORWARD
    )
)