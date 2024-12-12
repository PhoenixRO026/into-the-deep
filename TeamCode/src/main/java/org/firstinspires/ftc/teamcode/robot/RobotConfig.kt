package org.firstinspires.ftc.teamcode.robot

import org.firstinspires.ftc.teamcode.library.config.MotorConfig
import org.firstinspires.ftc.teamcode.library.config.ServoConfig

data class RobotConfig(
    val name: String,

    val drive: DriveConfig,

    //val motorLiftLeft: MotorConfig,
    //val motorLiftRight: MotorConfig,

    //val servoExtend: ServoConfig,

    val servoArmLeft: ServoConfig,

    val servoArmRight: ServoConfig
)