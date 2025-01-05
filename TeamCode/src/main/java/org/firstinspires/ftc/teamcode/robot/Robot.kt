package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.robot.config.RobotConfig

class Robot(
    private val config: RobotConfig
) {
    val drive = Drive(config.drive)
    val intake = Intake(config.intake)
    val lift = Lift(config.lift)
    val outtake = Outtake(config.outtake)

    fun init(hardwareMap: HardwareMap) {
        drive.init(hardwareMap)
        intake.init(hardwareMap)
        lift.init(hardwareMap)
        outtake.init(hardwareMap)
    }
}