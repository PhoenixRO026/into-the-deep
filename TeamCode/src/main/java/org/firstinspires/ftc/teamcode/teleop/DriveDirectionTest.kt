package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp(group = "test")
class DriveDirectionTest : LinearOpMode() {
    override fun runOpMode() {
        val motorRF = hardwareMap.get(DcMotorEx::class.java, "motorRF")
        val motorRB = hardwareMap.get(DcMotorEx::class.java, "motorRB")
        val motorLF = hardwareMap.get(DcMotorEx::class.java, "motorLF")
        val motorLB = hardwareMap.get(DcMotorEx::class.java, "motorLB")

        motorLF.direction = DcMotorSimple.Direction.REVERSE
        motorLB.direction = DcMotorSimple.Direction.REVERSE

        telemetry.addData("motorRF direction", motorRF.direction)
        telemetry.addData("motorRB direction", motorRB.direction)
        telemetry.addData("motorLF direction", motorLF.direction)
        telemetry.addData("motorLB direction", motorLB.direction)
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

            telemetry.addData("motorRF power", motorRF.power)
            telemetry.addData("motorRB power", motorRB.power)
            telemetry.addData("motorLF power", motorLF.power)
            telemetry.addData("motorLB power", motorLB.power)
            telemetry.update()
        }
    }
}