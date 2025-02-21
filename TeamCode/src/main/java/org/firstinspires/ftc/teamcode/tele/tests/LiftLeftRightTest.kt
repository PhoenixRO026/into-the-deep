package org.firstinspires.ftc.teamcode.tele.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.library.config.toMotorDirection

class LiftLeftRightTest : LinearOpMode() {
    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        val liftConfig = testsRobotHardwareConfig.lift

        val left = hardwareMap.get(DcMotorEx::class.java, liftConfig.motorLiftLeft.deviceName)
        val right = hardwareMap.get(DcMotorEx::class.java, liftConfig.motorLiftRight.deviceName)

        left.direction = liftConfig.motorLiftLeft.direction.toMotorDirection()
        right.direction = liftConfig.motorLiftRight.direction.toMotorDirection()

        left.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        right.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        waitForStart()

        while (opModeIsActive()) {
            val leftPower = -gamepad1.left_stick_y.toDouble()
            val rightPower = -gamepad1.right_stick_y.toDouble()

            left.power = leftPower
            right.power = rightPower

            telemetry.addData("left power", left.power)
            telemetry.addData("right power", right.power)
            telemetry.update()
        }
    }
}