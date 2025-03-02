package org.firstinspires.ftc.teamcode.robot

import com.lib.units.rad
import com.lib.units.s
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.controlHub
import org.firstinspires.ftc.teamcode.library.expansionHub
import org.firstinspires.ftc.teamcode.roadrunner.Localizer
import org.firstinspires.ftc.teamcode.robot.config.RobotHardwareConfig
import org.firstinspires.ftc.teamcode.robot.values.RobotValues

class TeleRobot(
    hardwareMap: HardwareMap,
    private val config: RobotHardwareConfig,
    values: RobotValues,
    private val timeKeep: TimeKeep,
    telemetry: Telemetry? = null,
) {
    val drive = Drive(hardwareMap, config.drive, values.drive, telemetry)
    private val hubs = hardwareMap.getAll(LynxModule::class.java)

    init {
        hubs.forEach {
            it.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL
        }
    }

    fun update() {
        hubs.forEach {
            it.clearBulkCache()
        }

        drive.update()
    }

    fun addTelemetry(telemetry: Telemetry) {
        telemetry.addData("Config name", config.name)
        telemetry.addLine("PERFORMANCE:")
        telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
        telemetry.addData("fps", 1.s / timeKeep.deltaTime)
        telemetry.addLine("DRIVE:")
        telemetry.addData("yaw degs", drive.yaw.rad.asDeg)
        //telemetry.addData("X coordonates", )")
    }
}