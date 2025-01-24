package org.firstinspires.ftc.teamcode.tele.tests

import com.acmerobotics.dashboard.config.Config
import com.lib.units.deg
import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Outtake

class OuttakeExtendoTuning : LinearOpMode() {
    @Config
    data object OuttakeExtendoTuningConfig {
        @JvmField
        var targetDegrees = 400.0
    }

    override fun runOpMode() {
        val config = testsRobotHardwareConfig
        val values = testsRobotValues

        val timeKeep = TimeKeep()
        val outtake = Outtake(hardwareMap, config.outtake, values.outtake, timeKeep)

        telemetry.addData("Config name", config.name)
        telemetry.update()

        outtake.shoulderCurrentPos = 0.5
        outtake.elbowCurrentPos = 0.5
        outtake.wristPosToMiddle()
        outtake.clawPos = 0.0

        waitForStart()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()

            outtake.extendoTargetPos = OuttakeExtendoTuningConfig.targetDegrees.deg

            outtake.update()

            telemetry.addData("Config name", config.name)

            telemetry.addData("extendo pos degrees", outtake.extendoPos.asDeg)
            telemetry.addData("extendo power", outtake.extendoPower)

            telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
            telemetry.addData("fps", 1.s / timeKeep.deltaTime)
            telemetry.update()
        }
    }
}