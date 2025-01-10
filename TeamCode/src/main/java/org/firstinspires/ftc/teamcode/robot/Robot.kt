package org.firstinspires.ftc.teamcode.robot

import com.lib.units.rad
import com.lib.units.s
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.config.RobotHardwareConfig
import org.firstinspires.ftc.teamcode.robot.values.RobotValues

class Robot(
    hardwareMap: HardwareMap,
    private val config: RobotHardwareConfig,
    values: RobotValues,
    private val timeKeep: TimeKeep
) {
    val drive = Drive(hardwareMap, config.drive, values.drive)
    val intake = Intake(hardwareMap, config.intake, values.intake, timeKeep)
    val lift = Lift(hardwareMap, config.lift, values.lift, timeKeep)
    val outtake = Outtake(hardwareMap, config.outtake, values.outtake, timeKeep)

    fun update() {
        intake.update()
        outtake.update()
        lift.update()
    }

    fun addTelemetry(telemetry: Telemetry) {
        telemetry.addData("Config name", config.name)
        telemetry.addLine("PERFORMANCE:")
        telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
        telemetry.addData("fps", 1.s / timeKeep.deltaTime)
        telemetry.addLine("DRIVE:")
        telemetry.addData("yaw degs", drive.yaw.rad.asDeg)
        telemetry.addLine("OUTTAKE:")
        telemetry.addData("outtake extendo pos", outtake.extendoPos)
        telemetry.addData("outtake extendo power", outtake.extendoPower)
        telemetry.addData("shoulder pos", outtake.shoulderCurrentPos)
        telemetry.addData("elbow pos", outtake.elbowCurrentPos)
        telemetry.addData("wrist pos", outtake.wristCurrentPos)
        telemetry.addData("wrist speed", outtake.wristSpeed)
        telemetry.addData("claw pos", outtake.clawPos)
        telemetry.addLine("INTAKE:")
        telemetry.addData("intake extendo power", intake.extendoPower)
        telemetry.addData("intake extendo pos", intake.extendoPosition)
        telemetry.addData("sweeper power", intake.sweeperPower)
        telemetry.addData("box tilt pos", intake.boxTiltCurrentPos)
        telemetry.addData("intake tilt pos", intake.intakeTiltCurrentPos)
        telemetry.addLine("LIFT:")
        telemetry.addData("lift power", lift.power)
        telemetry.addData("lift pos", lift.position)
    }
}