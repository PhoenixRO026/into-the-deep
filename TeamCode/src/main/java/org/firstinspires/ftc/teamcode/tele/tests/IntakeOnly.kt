package org.firstinspires.ftc.teamcode.tele.tests

import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.reverseScale
import org.firstinspires.ftc.teamcode.robot.Intake

class IntakeOnly : LinearOpMode() {
    override fun runOpMode() {
        val config = testsRobotHardwareConfig
        val values = testsRobotValues

        val timeKeep = TimeKeep()
        val intake = Intake(hardwareMap, config.intake, values.intake, timeKeep)

        telemetry.addData("Config name", config.name)
        telemetry.update()

        waitForStart()

        intake.intakeTiltCurrentPos = 0.5

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()
            val leftStickY = -gamepad1.left_stick_y.toDouble()
            val rightStickY = -gamepad1.right_stick_y.toDouble()

            if (gamepad1.b) {
                intake.resetExtendoPosition()
            }

            intake.extendoPower = leftStickY
            intake.sweeperPower = rightStickY

            intake.boxTiltCurrentPos += timeKeep.deltaTime / values.intake.boxTiltMaxTravelDuration * when {
                gamepad1.dpad_up -> 1.0
                gamepad1.dpad_down -> -1.0
                else -> 0.0
            }

            intake.intakeTiltCurrentPos += timeKeep.deltaTime / values.intake.intakeTiltMaxTravelDuration * when {
                gamepad1.y -> 1.0
                gamepad1.a -> -1.0
                else -> 0.0
            }

            telemetry.addData("Config name", config.name)
            telemetry.addData("extendo pos", intake.extendoPosition)
            telemetry.addData("extendo power", intake.extendoPower)
            telemetry.addData("sweeper power", intake.sweeperPower)
            telemetry.addData("box tilt pos", intake.boxTiltCurrentPos)
            telemetry.addData("abs box tilt pos", intake.boxTiltCurrentPos.reverseScale(config.intake.servoBoxTilt.rangeScale))
            telemetry.addData("intake tilt pos", intake.intakeTiltCurrentPos)
            telemetry.addData("abs intake tilt pos", intake.intakeTiltCurrentPos.reverseScale(config.intake.servoBoxTilt.rangeScale))
            telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
            telemetry.addData("fps", 1.s / timeKeep.deltaTime)
            telemetry.addLine("Press B to reset extendo position")
            telemetry.addData("left stick Y", leftStickY)
            telemetry.addData("right stick Y", rightStickY)
            telemetry.update()
        }
    }
}