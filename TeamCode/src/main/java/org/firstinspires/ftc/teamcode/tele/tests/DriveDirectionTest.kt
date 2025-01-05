package org.firstinspires.ftc.teamcode.tele.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig

class DriveDirectionTest : LinearOpMode() {
    override fun runOpMode() {
        val config = testsRobotHardwareConfig

        val motorRF = hardwareMap.createMotorUsingConfig(config.drive.motorRF)
        val motorRB = hardwareMap.createMotorUsingConfig(config.drive.motorRB)
        val motorLF = hardwareMap.createMotorUsingConfig(config.drive.motorLF)
        val motorLB = hardwareMap.createMotorUsingConfig(config.drive.motorLB)

        telemetry.addData("Config name", config.name)
        telemetry.update()

        waitForStart()

        while (opModeIsActive()) {
            val lf = if (gamepad1.x) 1.0 else 0.0
            val rf = if (gamepad1.y) 1.0 else 0.0
            val lb = if (gamepad1.a) 1.0 else 0.0
            val rb = if (gamepad1.b) 1.0 else 0.0

            motorRF.power = rf
            motorRB.power = rb
            motorLF.power = lf
            motorLB.power = lb

            telemetry.addData("Config name", config.name)
            telemetry.addData("motorRF power", motorRF.power)
            telemetry.addData("motorRB power", motorRB.power)
            telemetry.addData("motorLF power", motorLF.power)
            telemetry.addData("motorLB power", motorLB.power)
            telemetry.update()
        }
    }
}