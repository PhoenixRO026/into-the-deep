package org.firstinspires.ftc.teamcode.robot.config

import org.firstinspires.ftc.teamcode.library.config.ServoConfig

data class OuttakeConfig(
    val servoExtendo: ServoConfig,
    val servoShoulder: ServoConfig,
    val servoElbow: ServoConfig,
    val servoWrist: ServoConfig,
    val servoClaw: ServoConfig
)