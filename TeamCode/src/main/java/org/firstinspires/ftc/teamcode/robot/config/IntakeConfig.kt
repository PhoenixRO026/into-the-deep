package org.firstinspires.ftc.teamcode.robot.config

import org.firstinspires.ftc.teamcode.library.config.MotorConfig
import org.firstinspires.ftc.teamcode.library.config.ServoConfig

data class IntakeConfig(
    val motorExtendoIntake: MotorConfig,
    val motorSweeper: MotorConfig,
    val servoBoxTilt: ServoConfig,
    val servoIntakeTilt: ServoConfig
)