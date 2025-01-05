package org.firstinspires.ftc.teamcode.robot.config

data class RobotHardwareConfig(
    val name: String,

    val drive: DriveHardwareConfig,
    val lift: LiftHardwareConfig,
    val intake: IntakeHardwareConfig,
    val outtake: OuttakeHardwareConfig
)