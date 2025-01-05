package org.firstinspires.ftc.teamcode.robot.config

import org.firstinspires.ftc.teamcode.library.config.AnalogEncoderCRServoConfig
import org.firstinspires.ftc.teamcode.library.config.ServoConfig

data class OuttakeHardwareConfig(
    val servoExtendo: AnalogEncoderCRServoConfig,
    val servoShoulder: ServoConfig,
    val servoElbow: ServoConfig,
    val servoWrist: ServoConfig,
    val servoClaw: ServoConfig
)