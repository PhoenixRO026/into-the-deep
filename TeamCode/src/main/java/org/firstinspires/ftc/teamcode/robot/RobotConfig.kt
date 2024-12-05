package org.firstinspires.ftc.teamcode.robot

import org.firstinspires.ftc.teamcode.library.config.IMUConfig
import org.firstinspires.ftc.teamcode.library.config.MotorConfig
import org.firstinspires.ftc.teamcode.library.config.ServoConfig

data class RobotConfig(
    val configName: String,

    val imu: IMUConfig,

    val motorRF: MotorConfig,
    val motorRB: MotorConfig,
    val motorLF: MotorConfig,
    val motorLB: MotorConfig,

    val motorLiftLeft: MotorConfig,
    val motorLiftRight: MotorConfig,

    val servoExtendLeft: ServoConfig,
    val servoExtendRight: ServoConfig
)