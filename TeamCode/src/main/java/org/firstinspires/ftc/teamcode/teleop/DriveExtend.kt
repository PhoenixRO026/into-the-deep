package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.now
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.robot.Extend
import org.firstinspires.ftc.teamcode.robot.ExtendByServo
import org.firstinspires.ftc.teamcode.robot.NewDrive

@TeleOp
class DriveExtend : LinearOpMode() {
    override fun runOpMode() {
        val drive = NewDrive(hardwareMap, Pose2d(0.0, 0.0, 0.0), NewDrive.Side.RED)
        val extend = Extend(hardwareMap)
        val extendo = ExtendByServo(hardwareMap)

        var previousTime: Double
        var deltaTime : Double
        var now : Double

        waitForStart()

        previousTime = now()

        while (opModeIsActive()){
            now = now()
            deltaTime = now - previousTime
            previousTime = now


            drive.speed = if(gamepad1.left_trigger >= 0.2) 0.5 else 1.0

            drive.drive(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble()
            )

            extend.powerLift = -gamepad2.right_stick_y

            extend.updateLift()

            extendo.extend += gamepad2.right_stick_x * deltaTime * 0.05


            extendo.extendToPos(gamepad2.left_stick_x.toDouble())

            if(gamepad1.y){
                drive.resetHeading()
            }
        }
    }
}
