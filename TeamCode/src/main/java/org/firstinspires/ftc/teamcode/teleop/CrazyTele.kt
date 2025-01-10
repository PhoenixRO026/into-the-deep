package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.now
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Arm
import org.firstinspires.ftc.teamcode.robot.Claw
import org.firstinspires.ftc.teamcode.robot.Intake
import org.firstinspires.ftc.teamcode.robot.Lift
import org.firstinspires.ftc.teamcode.robot.NewDrive


class CrazyTele : LinearOpMode() {
    private val timeKeep = TimeKeep()

    override fun runOpMode() {


        var claw = Claw(hardwareMap)
        var arm = Arm(hardwareMap)
        var drive = NewDrive(hardwareMap, Pose2d(0.0, 0.0, 0.0), NewDrive.Side.NEUTRAL)
        var intake = Intake(hardwareMap)
        var lift = Lift(hardwareMap)


        var previousTime : Double
        var deltaTime : Double
        var now : Double

        waitForStart()

        previousTime = now()

        while (opModeIsActive()){
            now = now()
            deltaTime = now - previousTime
            previousTime = now

            drive.speed = if(gamepad1.left_bumper) 0.5 else 1.0

            drive.drive(
                -gamepad1.left_stick_y.toDouble(),
                gamepad1.left_stick_x.toDouble(),
                gamepad1.right_stick_x.toDouble()
            )

            if(gamepad1.left_trigger >= 0.2){
                intake.extend = -1.0
            }
            else if(gamepad1.right_trigger >= 0.2){
                intake.extend = 1.0
            }
            else{
                intake.extend = 0.0
            }
            if(gamepad1.b){
                drive.resetHeading()
            }

            //clawArm.extend += gamepad2.right_stick_x * deltaTime * 0.001
            lift.powerLift = -gamepad2.right_stick_x
        }
    }

}