package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.roadrunner.now
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.robot.Robot

@TeleOp
class CraneTele : LinearOpMode() {
    private var extendSpeed = 0.0001

    private var previousTime = now()
    private var currentTime = now()
    private inline val deltaTime get() = (currentTime - previousTime) * 1000

    private val robot = Robot(robotConfigGherla)

    override fun runOpMode() {
        robot.init(hardwareMap)

        waitForStart()

        robot.extendPosition = 0.7
        robot.liftPower = 0.0

        resetDeltaTime()

        while (opModeIsActive()) {
            resetDeltaTime()

            robot.drive(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble()
            )

            robot.liftPower = -gamepad2.right_stick_y.toDouble()

            if (gamepad2.dpad_up) {
                robot.extendPosition += extendSpeed * deltaTime
            } else if (gamepad2.dpad_down) {
                robot.extendPosition -= extendSpeed * deltaTime
            }

            telemetry.addData("extend pos", robot.extendPosition)
            telemetry.addData("lift power", robot.liftPower)
            telemetry.addData("delta time ms", deltaTime)
            telemetry.addData("fps", 1000.0 / deltaTime)
            telemetry.update()
        }
    }

    private fun resetDeltaTime() {
        previousTime = currentTime
        currentTime = now()
    }
}