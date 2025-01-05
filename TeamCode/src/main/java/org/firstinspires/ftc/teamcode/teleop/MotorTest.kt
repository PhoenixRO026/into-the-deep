package org.firstinspires.ftc.teamcode.teleop.MotorTest

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor

@TeleOp
class MotorTest : LinearOpMode(){
    override fun runOpMode() {
        var motor : DcMotor = hardwareMap.get(DcMotor::class.java, "motor")

        waitForStart()

        while(opModeIsActive()){
            if(gamepad1.y)
                motor.power = 1.0
            else if(gamepad1.a)
                motor.power = -1.0
            else
                motor.power = 0.0

            telemetry.addData("left pos", motor.currentPosition)
            telemetry.addData("left target", motor.targetPosition)
            telemetry.update()
        }
    }


}