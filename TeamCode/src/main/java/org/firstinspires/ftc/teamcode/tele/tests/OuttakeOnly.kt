package org.firstinspires.ftc.teamcode.tele.tests

import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.reverseScale
import org.firstinspires.ftc.teamcode.robot.Outtake

class OuttakeOnly : LinearOpMode() {
    override fun runOpMode() {
        val config = testsRobotHardwareConfig
        val values = testsRobotValues

        val timeKeep = TimeKeep()
        val outtake = Outtake(hardwareMap, config.outtake, values.outtake, timeKeep)

        telemetry.addData("Config name", config.name)
        telemetry.update()

        outtake.shoulderCurrentPos = 0.5
        outtake.elbowCurrentPos = 0.5
        outtake.wristPos = 0.5
        outtake.clawPos = 0.5

        waitForStart()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()
            val leftStickY = -gamepad1.left_stick_y.toDouble()
            val rightStickY = -gamepad1.right_stick_y.toDouble()
            val triggers = (gamepad1.right_trigger - gamepad1.left_trigger).toDouble()

            outtake.extendoPower = triggers

            outtake.shoulderCurrentPos += timeKeep.deltaTime / values.outtake.shoulderMaxTravelDuration * leftStickY

            outtake.elbowCurrentPos += timeKeep.deltaTime / values.outtake.elbowMaxTravelDuration * rightStickY

            outtake.wristPos += timeKeep.deltaTime / values.outtake.wristMaxTravelDuration * when {
                gamepad1.right_bumper -> 1.0
                gamepad1.left_bumper -> -1.0
                else -> 0.0
            }

            outtake.clawPos += timeKeep.deltaTime / values.outtake.clawMaxTravelDuration * when {
                gamepad1.y -> 1.0
                gamepad1.a -> -1.0
                else -> 0.0
            }

            telemetry.addData("Config name", config.name)

            telemetry.addData("extendo pos", outtake.extendoPos)
            telemetry.addData("extendo power", outtake.extendoPower)

            telemetry.addData("shoulder pos", outtake.shoulderCurrentPos)
            telemetry.addData("abs shoulder pos", outtake.shoulderCurrentPos.reverseScale(config.outtake.servoShoulder.rangeScale))

            telemetry.addData("elbow pos", outtake.elbowCurrentPos)
            telemetry.addData("abs elbow pos", outtake.elbowCurrentPos.reverseScale(config.outtake.servoElbow.rangeScale))

            telemetry.addData("wrist pos", outtake.wristPos)
            telemetry.addData("abs wrist pos", outtake.wristPos.reverseScale(config.outtake.servoWrist.rangeScale))

            telemetry.addData("claw pos", outtake.clawPos)
            telemetry.addData("abs claw pos", outtake.clawPos.reverseScale(config.outtake.servoClaw.rangeScale))

            telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
            telemetry.addData("fps", 1.s / timeKeep.deltaTime)
            telemetry.addData("left stick Y", leftStickY)
            telemetry.addData("right stick Y", rightStickY)
            telemetry.addData("triggers", triggers)
            telemetry.update()
        }
    }
}