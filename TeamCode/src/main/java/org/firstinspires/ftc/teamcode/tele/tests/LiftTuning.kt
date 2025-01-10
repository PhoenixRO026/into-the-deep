package org.firstinspires.ftc.teamcode.tele.tests

import com.acmerobotics.dashboard.config.Config
import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Lift

class LiftTuning : LinearOpMode() {
    @Config
    data object LiftTuningConfig {
        @JvmField
        var targetPos = 0
    }

    override fun runOpMode() {
        val config = testsRobotHardwareConfig
        val values = testsRobotValues

        val timeKeep = TimeKeep()
        val lift = Lift(hardwareMap, config.lift, values.lift, timeKeep)

        telemetry.addData("Config name", config.name)
        telemetry.update()

        waitForStart()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()

            lift.targetPosition = LiftTuningConfig.targetPos

            lift.update()

            telemetry.addData("Config name", config.name)

            telemetry.addData("lift pos", lift.position)
            telemetry.addData("lift power", lift.power)

            telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
            telemetry.addData("fps", 1.s / timeKeep.deltaTime)
            telemetry.update()
        }
    }
}