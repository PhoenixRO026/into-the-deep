package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig

@TeleOp(group = "test")
class DriveDirectionTest : LinearOpMode() {
    override fun runOpMode() {
        val config = robotConfigGherla

        val motorRF = hardwareMap.createMotorUsingConfig(config.motorRF)
        val motorRB = hardwareMap.createMotorUsingConfig(config.motorRB)
        val motorLF = hardwareMap.createMotorUsingConfig(config.motorLF)
        val motorLB = hardwareMap.createMotorUsingConfig(config.motorLB)

        telemetry.addData("Config name", config.configName)
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

            telemetry.addData("Config name", config.configName)
            telemetry.addData("motorRF power", motorRF.power)
            telemetry.addData("motorRB power", motorRB.power)
            telemetry.addData("motorLF power", motorLF.power)
            telemetry.addData("motorLB power", motorLB.power)
            telemetry.update()
        }
    }
}