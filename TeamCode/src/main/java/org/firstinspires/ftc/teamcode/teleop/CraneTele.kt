package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot
import org.firstinspires.ftc.teamcode.teleop.config.robotConfigGherla

@TeleOp
class CraneTele : LinearOpMode() {
    private var extendSpeed = 0.0001
    private val timeKeep = TimeKeep()
    private val robot = Robot(robotConfigGherla)

    override fun runOpMode() {
        robot.init(hardwareMap)

        val imu = hardwareMap.get(IMU::class.java, "imu")

        val logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP
        val usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT

        val orientation = RevHubOrientationOnRobot(logoDirection, usbDirection)

        val parameters = IMU.Parameters(orientation)

        imu.initialize(parameters)

        imu.resetYaw()

        waitForStart()

        robot.extendPosition = 0.7
        robot.liftPower = 0.0

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()

            robot.drive(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble()
            )

            robot.liftPower = -gamepad2.right_stick_y.toDouble()

            if (gamepad2.dpad_up) {
                robot.extendPosition += extendSpeed * timeKeep.deltaTime
            } else if (gamepad2.dpad_down) {
                robot.extendPosition -= extendSpeed * timeKeep.deltaTime
            }

            telemetry.addData("extend pos", robot.extendPosition)
            telemetry.addData("lift power", robot.liftPower)
            telemetry.addData("delta time ms", timeKeep.deltaTime)
            telemetry.addData("fps", 1000.0 / timeKeep.deltaTime)
            telemetry.update()
        }
    }
}