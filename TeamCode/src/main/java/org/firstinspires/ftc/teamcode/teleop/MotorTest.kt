package org.firstinspires.ftc.teamcode.teleop.MotorTest

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import  org.firstinspires.ftc.teamcode.robot.Extend

@TeleOp
class MotorTest : LinearOpMode(){
    override fun runOpMode() {
        val lift = Extend(hardwareMap)

        waitForStart()

        while(opModeIsActive()){
            lift.powerLift = -gamepad1.right_stick_y
            lift.updateLift()

            telemetry.addData("lift power", lift.powerLift)
            telemetry.addData("left pos", lift.leftLiftMotor.currentPosition)
            telemetry.addData("right pos", lift.rightLiftMotor.currentPosition)
            telemetry.addData("left target", lift.leftLiftMotor.targetPosition)
            telemetry.addData("right target", lift.rightLiftMotor.targetPosition)
            telemetry.update()
        }
    }


}