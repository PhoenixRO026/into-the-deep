package org.firstinspires.ftc.teamcode.tele

import com.lib.units.rad
import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot
import org.firstinspires.ftc.teamcode.tele.config.robotConfigWonder

@TeleOp
class CraneTele : LinearOpMode() {
    override fun runOpMode() {
        val config = robotConfigWonder

        telemetry.addData("Config name", config.name)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        val robot = Robot(robotConfigWonder)
        robot.init(hardwareMap)

        val timeKeep = TimeKeep()

        val extendoMaxTravelDuration = 5.s
        val shoulderMaxTravelDuration = 5.s
        val elbowMaxTravelDuration = 5.s
        val wristMaxTravelDuration = 5.s
        val clawMaxTravelDuration = 5.s

        telemetry.addData("Config name", config.name)
        telemetry.addLine("READY!")
        telemetry.update()

        waitForStart()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()
            val pad1LeftStickY = -gamepad1.left_stick_y.toDouble()
            val pad1LeftStickX = gamepad1.right_stick_x.toDouble()
            val pad1RightStickX = gamepad1.right_stick_x.toDouble()

            //DRIVE
            if (gamepad1.y)
                robot.drive.resetFieldCentric()

            robot.drive.isSlowMode = gamepad1.left_bumper

            robot.drive.driveFieldCentric(
                pad1LeftStickY,
                -pad1LeftStickX,
                -pad1RightStickX
            )


            /*robot.outtake.extendoPos += timeKeep.deltaTime / extendoMaxTravelDuration * triggers

            robot.outtake.shoulderPos += timeKeep.deltaTime / shoulderMaxTravelDuration * leftStickY

            robot.outtake.elbowPos += timeKeep.deltaTime / elbowMaxTravelDuration * rightStickY*/

            robot.outtake.wristPos += timeKeep.deltaTime / wristMaxTravelDuration * when {
                gamepad1.right_bumper -> 1.0
                gamepad1.left_bumper -> -1.0
                else -> 0.0
            }

            robot.outtake.clawPos += timeKeep.deltaTime / clawMaxTravelDuration * when {
                gamepad1.y -> 1.0
                gamepad1.a -> -1.0
                else -> 0.0
            }

            telemetry.addData("Config name", config.name)
            telemetry.addLine("PERFORMANCE:")
            telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
            telemetry.addData("fps", 1.s / timeKeep.deltaTime)
            telemetry.addLine("DRIVE:")
            telemetry.addData("yaw degs", robot.drive.yaw.rad.asDeg)
            telemetry.addLine("OUTTAKE:")
            telemetry.addData("extendo pos", robot.outtake.extendoPos)
            telemetry.addData("shoulder pos", robot.outtake.shoulderPos)
            telemetry.addData("elbow pos", robot.outtake.elbowPos)
            telemetry.addData("wrist pos", robot.outtake.wristPos)
            telemetry.addData("claw pos", robot.outtake.clawPos)


            telemetry.update()
        }
    }
}