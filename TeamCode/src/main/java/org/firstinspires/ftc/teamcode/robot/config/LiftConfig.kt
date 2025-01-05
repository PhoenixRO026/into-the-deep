package org.firstinspires.ftc.teamcode.robot.config

import org.firstinspires.ftc.teamcode.library.config.EncoderConfig
import org.firstinspires.ftc.teamcode.library.config.MotorConfig

data class LiftConfig(
    val motorLiftLeft: MotorConfig,
    val motorLiftRight: MotorConfig,
    val encoder: EncoderConfig
)