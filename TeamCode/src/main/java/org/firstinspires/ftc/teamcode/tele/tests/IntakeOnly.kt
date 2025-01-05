package org.firstinspires.ftc.teamcode.tele.tests

import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.reverseScale
import org.firstinspires.ftc.teamcode.robot.Intake
import org.firstinspires.ftc.teamcode.tele.config.robotConfigWonder

class IntakeOnly : LinearOpMode() {
    override fun runOpMode() {
        val config = testsRobotConfig

        val timeKeep = TimeKeep()
        val intake = Intake(robotConfigWonder.intake)
        intake.init(hardwareMap)

        telemetry.addData("Config name", config.name)
        telemetry.update()

        val boxTiltMaxTravelDuration = 5.s
        val intakeTiltMaxTravelDuration = 5.s

        waitForStart()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()
            val leftStickY = -gamepad1.left_stick_y.toDouble()
            val rightStickY = -gamepad1.right_stick_y.toDouble()

            if (gamepad1.b) {
                intake.resetExtendoPosition()
            }

            intake.extendoPower = leftStickY
            intake.sweeperPower = rightStickY

            intake.boxTilt += timeKeep.deltaTime / boxTiltMaxTravelDuration * when {
                gamepad1.dpad_up -> 1.0
                gamepad1.dpad_down -> -1.0
                else -> 0.0
            }

            intake.intakeTilt += timeKeep.deltaTime / intakeTiltMaxTravelDuration * when {
                gamepad1.y -> 1.0
                gamepad1.a -> -1.0
                else -> 0.0
            }

            telemetry.addData("Config name", config.name)
            telemetry.addData("extendo pos", intake.extendoPosition)
            telemetry.addData("extendo power", intake.extendoPower)
            telemetry.addData("sweeper power", intake.sweeperPower)
            telemetry.addData("box tilt pos", intake.boxTilt)
            telemetry.addData("abs box tilt pos", intake.boxTilt.reverseScale(config.intake.servoBoxTilt.rangeScale))
            telemetry.addData("intake tilt pos", intake.intakeTilt)
            telemetry.addData("abs intake tilt pos", intake.intakeTilt.reverseScale(config.intake.servoBoxTilt.rangeScale))
            telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
            telemetry.addData("fps", 1.s / timeKeep.deltaTime)
            telemetry.addLine("Press B to reset extendo position")
            telemetry.addData("left stick Y", leftStickY)
            telemetry.addData("right stick Y", rightStickY)
            telemetry.update()
        }
    }
}