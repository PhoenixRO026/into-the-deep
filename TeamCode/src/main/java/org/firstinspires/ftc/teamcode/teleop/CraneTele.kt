package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.roadrunner.MecanumKinematics
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.PoseVelocity2dDual
import com.acmerobotics.roadrunner.Time
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.now
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.robot.Robot
import kotlin.math.absoluteValue

@TeleOp
class CraneTele : LinearOpMode() {
    private var extendSpeed = 0.0001

    private var previousTime = now()
    private var currentTime = now()
    private inline val deltaTime get() = (currentTime - previousTime) * 1000

    private val mecanumKinematics = MecanumKinematics(1.0)

    private val robot = Robot(robotConfigGherla)

    override fun runOpMode() {
        robot.init(hardwareMap)

        waitForStart()

        robot.extendPosition = 0.7
        robot.liftPower = 0.0

        resetDeltaTime()

        while (opModeIsActive()) {
            resetDeltaTime()

            drive(
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

    private fun drive(forward: Double, left: Double, rotate: Double) {
        val driveVec = PoseVelocity2dDual.constant<Time>(
            PoseVelocity2d(
                Vector2d(
                    forward,
                    left
                ),
                rotate
            ),
            1
        )

        val powers = mecanumKinematics.inverse(driveVec)

        val maxMag = powers.all().map { it.value().absoluteValue }.plusElement(1.0).max()

        robot.motorRF.power = powers.rightFront.value() / maxMag
        robot.motorRB.power = powers.rightBack.value() / maxMag
        robot.motorLF.power = powers.leftFront.value() / maxMag
        robot.motorLB.power = powers.leftBack.value() / maxMag
    }
}