package org.firstinspires.ftc.teamcode.tele.tests

import com.lib.units.rad
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.robot.Drive
import org.firstinspires.ftc.teamcode.tele.config.robotConfigWonder

class DriveOnly : LinearOpMode() {
    override fun runOpMode() {
        val config = testsRobotConfig

        val drive = Drive(robotConfigWonder.drive)
        drive.init(hardwareMap)

        telemetry.addData("Config name", config.name)
        telemetry.update()

        waitForStart()

        while (opModeIsActive()) {
            val leftStickY = -gamepad1.left_stick_y.toDouble()
            val leftStickX = gamepad1.left_stick_x.toDouble()
            val rightStickX = gamepad1.right_stick_x.toDouble()

            if (gamepad1.y) {
                drive.resetFieldCentric()
            }

            drive.driveFieldCentric(
                leftStickY,
                -leftStickX,
                -rightStickX
            )

            telemetry.addData("Config name", config.name)
            telemetry.addData("yaw degs", drive.yaw.rad.asDeg)
            telemetry.addLine("Press Y to reset heading")
            telemetry.addData("left stick Y", leftStickY)
            telemetry.addData("left stick X", leftStickX)
            telemetry.addData("right stick X", rightStickX)
            telemetry.update()
        }
    }
}