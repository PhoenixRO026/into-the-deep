package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.roadrunner.MecanumKinematics
import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.robot.Arm
import org.firstinspires.ftc.teamcode.robot.Drive
import org.firstinspires.ftc.teamcode.robot.NewDrive
import org.firstinspires.ftc.teamcode.teleop.config.robotConfigGherla

@TeleOp
class BasicDrive : LinearOpMode() {
    override fun runOpMode() {
        val drive = NewDrive(hardwareMap, Pose2d(0.0, 0.0, 0.0), NewDrive.Side.RED)
        val arm = Arm(hardwareMap)

        waitForStart()

        while (opModeIsActive()){
            drive.speed = if(gamepad1.left_trigger >= 0.2) 0.5 else 1.0

            drive.drive(
                -gamepad1.left_stick_y.toDouble(),
                gamepad1.left_stick_x.toDouble(),
                gamepad1.right_stick_x.toDouble()
            )

            if(gamepad1.y){
                drive.resetHeading()
            }

            //ArmDriveRun(gamepad1, arm)
            //ClawDriveRun(gamepad1, claw)
            //DriveDriveRun(gamepad2, drive)

        }
    }
}