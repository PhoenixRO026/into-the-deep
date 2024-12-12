package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Claw
import org.firstinspires.ftc.teamcode.robot.Robot
import org.firstinspires.ftc.teamcode.teleop.config.robotConfigGherla
import kotlin.math.PI

@TeleOp
class CraneDrive : LinearOpMode() {
    private var extendSpeed = 0.0001
    private val timeKeep = TimeKeep()
    private val robot = Robot(robotConfigGherla)

    override fun runOpMode() {
        robot.init(hardwareMap)

        waitForStart()

        var claw = Claw(hardwareMap)

        //robot.extendPosition = 0.7
        //robot.liftPower = 0.0

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()

            robot.drive.driveFieldCentric(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble()
            )

            robot.liftPower = -gamepad2.right_stick_y.toDouble()

            //robot.extendPosition = gamepad2.left_stick_y.toDouble()

            /*if (gamepad2.right_trigger){
                robot.drive.
            }*/
            /*if(gamepad1.y){
                claw.rotateToF()
            }
            else if(gamepad1.x){
                claw.rotateToL()
            }
            else if(gamepad1.b){
                claw.rotateToR()
            }
            else if(gamepad1.a){
                claw.rotateToB()
            }*/
            if(gamepad1.right_trigger >= 0.2){
                claw.openClaw()
            }/*
            if(gamepad1.left_bumper){
                claw.tiltToRobot()
            }
            else if(gamepad1.right_bumper){
                claw.tiltToScore()
            }*/
            if(gamepad1.dpad_right){
                claw.tiltToDown()
            }
            else if(gamepad1.dpad_left) {
                claw.tiltToUp()
            }
            if(gamepad1.dpad_up){
                claw.rotateToUp()
            }
            else if(gamepad1.dpad_down){
                claw.rotateToDown()
            }
            //telemetry.addData("extend pos", robot.extendPosition)
            telemetry.addData("lift power", robot.liftPower)
            telemetry.addData("robot yaw degrees", robot.drive.yaw / PI * 180)
            telemetry.addData("delta time ms", timeKeep.deltaTime)
            telemetry.addData("fps", 1000.0 / timeKeep.deltaTime)
            telemetry.update()
        }
    }
}