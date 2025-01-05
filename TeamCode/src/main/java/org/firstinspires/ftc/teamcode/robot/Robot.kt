package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.config.RobotHardwareConfig
import org.firstinspires.ftc.teamcode.robot.values.RobotValues

class Robot(
    config: RobotHardwareConfig,
    values: RobotValues,
    timeKeep: TimeKeep
) {
    val drive = Drive(config.drive, values.drive)
    val intake = Intake(config.intake, values.intake, timeKeep)
    val lift = Lift(config.lift, values.lift)
    val outtake = Outtake(config.outtake, values.outtake, timeKeep)

    fun init(hardwareMap: HardwareMap) {
        drive.init(hardwareMap)
        intake.init(hardwareMap)
        lift.init(hardwareMap)
        outtake.init(hardwareMap)
    }

    fun update() {
        intake.update()
        outtake.update()
    }
}