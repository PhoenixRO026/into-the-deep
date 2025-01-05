package org.firstinspires.ftc.teamcode.robot.config

import org.firstinspires.ftc.teamcode.library.config.IMUConfig
import org.firstinspires.ftc.teamcode.library.config.MotorConfig

data class DriveConfig(
    val imu: IMUConfig,

    val motorRF: MotorConfig,
    val motorRB: MotorConfig,
    val motorLF: MotorConfig,
    val motorLB: MotorConfig,
)