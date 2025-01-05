package org.firstinspires.ftc.teamcode.tele.tests

import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.reverseScale
import org.firstinspires.ftc.teamcode.robot.Outtake
import org.firstinspires.ftc.teamcode.tele.config.robotConfigWonder

class OuttakeOnly : LinearOpMode() {
    override fun runOpMode() {
        val config = testsRobotConfig

        val timeKeep = TimeKeep()
        val outtake = Outtake(robotConfigWonder.outtake)
        outtake.init(hardwareMap)

        telemetry.addData("Config name", config.name)
        telemetry.update()

        val extendoMaxTravelDuration = 5.s
        val shoulderMaxTravelDuration = 5.s
        val elbowMaxTravelDuration = 5.s
        val wristMaxTravelDuration = 5.s
        val clawMaxTravelDuration = 5.s

        waitForStart()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()
            val leftStickY = -gamepad1.left_stick_y.toDouble()
            val rightStickY = -gamepad1.right_stick_y.toDouble()
            val triggers = (gamepad1.right_trigger - gamepad1.left_trigger).toDouble()

            outtake.extendoPos += timeKeep.deltaTime / extendoMaxTravelDuration * triggers

            outtake.shoulderPos += timeKeep.deltaTime / shoulderMaxTravelDuration * leftStickY

            outtake.elbowPos += timeKeep.deltaTime / elbowMaxTravelDuration * rightStickY

            outtake.wristPos += timeKeep.deltaTime / wristMaxTravelDuration * when {
                gamepad1.right_bumper -> 1.0
                gamepad1.left_bumper -> -1.0
                else -> 0.0
            }

            outtake.clawPos += timeKeep.deltaTime / clawMaxTravelDuration * when {
                gamepad1.y -> 1.0
                gamepad1.a -> -1.0
                else -> 0.0
            }

            telemetry.addData("Config name", config.name)

            telemetry.addData("extendo pos", outtake.extendoPos)
            telemetry.addData("abs extendo pos", outtake.extendoPos.reverseScale(config.outtake.servoExtendo.rangeScale))

            telemetry.addData("shoulder pos", outtake.shoulderPos)
            telemetry.addData("abs shoulder pos", outtake.shoulderPos.reverseScale(config.outtake.servoShoulder.rangeScale))

            telemetry.addData("elbow pos", outtake.elbowPos)
            telemetry.addData("abs elbow pos", outtake.elbowPos.reverseScale(config.outtake.servoElbow.rangeScale))

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