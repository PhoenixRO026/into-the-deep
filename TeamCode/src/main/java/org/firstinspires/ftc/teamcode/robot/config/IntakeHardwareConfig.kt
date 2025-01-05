package org.firstinspires.ftc.teamcode.robot.config

import org.firstinspires.ftc.teamcode.library.config.EncoderConfig
import org.firstinspires.ftc.teamcode.library.config.MotorConfig
import org.firstinspires.ftc.teamcode.library.config.ServoConfig

data class IntakeHardwareConfig(
    val motorExtendoIntake: MotorConfig,
    val encoderExtendo: EncoderConfig,
    val motorSweeper: MotorConfig,
    val servoBoxTilt: ServoConfig,
    val servoIntakeTilt: ServoConfig
)