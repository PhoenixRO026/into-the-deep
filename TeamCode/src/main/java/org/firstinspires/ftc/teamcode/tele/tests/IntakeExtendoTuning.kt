package org.firstinspires.ftc.teamcode.tele.tests

import com.acmerobotics.dashboard.config.Config
import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Intake

class IntakeExtendoTuning : LinearOpMode() {
    @Config
    data object IntakeExtendoTuningConfig {
        @JvmField
        var targetPos = 0
    }

    override fun runOpMode() {
        val config = testsRobotHardwareConfig
        val values = testsRobotValues

        val timeKeep = TimeKeep()
        val intake = Intake(hardwareMap, config.intake, values.intake, timeKeep)

        telemetry.addData("Config name", config.name)
        telemetry.update()

        waitForStart()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()

            intake.extendoTargetPosition = IntakeExtendoTuningConfig.targetPos

            intake.update()

            telemetry.addData("Config name", config.name)

            telemetry.addData("extendo pos", intake.extendoPosition)
            telemetry.addData("extendo power", intake.extendoPower)

            telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
            telemetry.addData("fps", 1.s / timeKeep.deltaTime)
            telemetry.update()
        }
    }
}