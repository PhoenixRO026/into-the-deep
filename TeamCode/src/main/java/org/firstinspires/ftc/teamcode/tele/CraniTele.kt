package org.firstinspires.ftc.teamcode.tele

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.SequentialAction
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigTransilvaniaCollege
import org.firstinspires.ftc.teamcode.tele.values.robotValuesTransilvaniaCollege
import kotlin.math.abs

@TeleOp
class CraniTele : LinearOpMode() {
    override fun runOpMode() {
        val config = robotHardwareConfigTransilvaniaCollege
        val values = robotValuesTransilvaniaCollege
        var emergencyMode : Int = 0

        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        telemetry.addData("Config name", config.name)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        val timeKeep = TimeKeep()
        val robot = Robot(hardwareMap, config, values, timeKeep)

        telemetry.addData("Config name", config.name)
        telemetry.addLine("READY!")
        telemetry.update()

        waitForStart()

        robot.outtake.shoulderCurrentPos = 0.6404
        robot.outtake.elbowCurrentPos = 0.5159
        robot.outtake.wristPosToMiddle()
        robot.outtake.clawPos = 1.0

        robot.intake.intakeTiltCurrentPos = 0.5106

        while (isStarted && !isStopRequested) {
            timeKeep.resetDeltaTime()
            val pad1LeftStickY = -gamepad1.left_stick_y.toDouble()
            val pad1LeftStickX = gamepad1.left_stick_x.toDouble()
            val pad1RightStickX = gamepad1.right_stick_x.toDouble()
            val pad1Triggers = gamepad1.right_trigger.toDouble() - gamepad1.left_trigger.toDouble()

            val pad2LeftStickY = -gamepad2.left_stick_y.toDouble()
            val pad2LeftStickX = gamepad2.left_stick_x.toDouble()
            val pad2RightStickY = -gamepad2.right_stick_y.toDouble()
            val pad2RightStickX = gamepad2.right_stick_x.toDouble()

            //DRIVE
            if (gamepad1.y)
                robot.drive.resetFieldCentric()

            robot.drive.isSlowMode = gamepad1.right_trigger >= 0.2

            robot.drive.driveFieldCentric(
                pad1LeftStickY,
                -pad1LeftStickX,
                -pad1RightStickX
            )


            if(emergencyMode == 0) {
                //OUTTAKE
                if (gamepad2.right_bumper) {
                    robot.outtake.armTargetToSpecimen()
                } else if (gamepad2.left_bumper) {
                    //robot.lift.liftToPosAction(338)   // DONT USE ACTIONS IN TELE
                    robot.lift.targetPosition = 338
                    robot.intake.intakeUp()
                    robot.intake.boxUp()
                    robot.outtake.armTargetToIntake()
                } else if (gamepad2.left_trigger >= 0.3) {
                    robot.outtake.armTargetToRobot()
                }
                robot.outtake.extendoPower = pad2LeftStickY

                robot.outtake.clawPos = gamepad2.right_trigger.toDouble()

                if (gamepad2.dpad_right) {
                    robot.intake.boxUp()
                } else if (gamepad2.dpad_left) {
                    robot.intake.boxDown()
                }
            }
            if(gamepad1.dpad_up){
                robot.intake.intakeUp()
            }
            else if(gamepad1.dpad_down){
                robot.intake.intakeDown()
            }
            //INTAKE
            robot.intake.extendoPower = pad1Triggers
            robot.intake.sweeperPower = when {
                gamepad1.right_bumper -> 1.0
                gamepad1.left_bumper -> -1.0
                else -> 0.0
            }
            //LIFT
            robot.lift.power = pad2RightStickY + 0.15

            //
            ////////////////////////////////////////////////////////////////
            //
            /*EMERGENCY!!!!!!!*/
            //
            ////////////////////////////////////////////////////////////////
            //

            if (gamepad2.ps) {
                emergencyMode = abs(emergencyMode-1)
                sleep(100)
            }

            if (emergencyMode == 1){
                //OUTTAKE

                robot.outtake.extendoPower = pad2LeftStickY

                robot.outtake.shoulderSpeed = when {
                    gamepad2.right_bumper -> -1.0
                    gamepad2.left_bumper -> 1.0
                    else -> 0.0
                }

                robot.outtake.elbowSpeed = when {
                    gamepad2.y -> 1.0
                    gamepad2.a -> -1.0
                    else -> 0.0
                }
                robot.outtake.wristSpeed = when {
                    gamepad2.dpad_left -> -1.0
                    gamepad2.dpad_right -> 1.0
                    else -> 0.0
                }
                robot.outtake.clawPos = gamepad2.right_trigger.toDouble()

                robot.intake.boxTiltSpeed = when {
                    gamepad2.dpad_up -> 1.0
                    gamepad2.dpad_up -> -1.0
                    else -> 0.0
                }

                //LIFT
                robot.lift.power = pad2RightStickY + 0.15
            }

            /*END OF BACKUP*/

            robot.update()

            robot.addTelemetry(telemetry)
            telemetry.update()
        }
    }
}