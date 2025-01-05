package org.firstinspires.ftc.teamcode.robot.config

data class RobotConfig(
    val name: String,

    val drive: DriveConfig,
    val lift: LiftConfig,
    val intake: IntakeConfig,
    val outtake: OuttakeConfig
)