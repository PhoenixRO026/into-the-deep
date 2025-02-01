package org.firstinspires.ftc.teamcode.tele

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader
import org.firstinspires.ftc.teamcode.robot.TeleRobot
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigTransilvaniaCollege
import org.firstinspires.ftc.teamcode.tele.values.robotValuesTransilvaniaCollege
import kotlin.math.abs
import kotlin.math.absoluteValue

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
        val robot = TeleRobot(hardwareMap, config, values, timeKeep, telemetry)

        telemetry.addData("Config name", config.name)
        telemetry.addLine("READY!")
        telemetry.update()

        waitForStart()

        robot.outtake.shoulderCurrentPos = 0.6404
        robot.outtake.elbowCurrentPos = 0.5159
        robot.outtake.wristPosToMiddle()
        robot.outtake.clawPos = 1.0

        robot.intake.intakeTiltCurrentPos = 0.5106

        val emergencyButton = ButtonReader { gamepad2.ps }

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

                if (gamepad2.left_bumper) {
                    robot.outtake.armTargetToBasket()
                }
                else if (gamepad2.right_bumper){
                    //robot.lift.liftToPosAction(338)   // DONT USE ACTIONS IN TELE
                    //robot.lift.targetPosition = 338
                    robot.intake.intakeUp()
                    robot.intake.boxUp()
                    robot.outtake.armTargetToIntake()
                }
                else if (gamepad2.a){
                    robot.outtake.armTargetToRobot()
                }
                else if (gamepad2.left_trigger >= 0.2) {
                    robot.intake.sweeperPower = -gamepad2.left_stick_y.toDouble()

                }
                else if (gamepad2.y) {
                    robot.outtake.armTargetToBar()
                }
                if (gamepad2.b){
                    robot.outtake.armTargetToSpecimen()
                }
                if(gamepad2.left_trigger <= 0.2) {
                    robot.outtake.extendoPower = pad2LeftStickY
                }

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
            robot.intake.extendoPower =when {
                gamepad1.right_bumper -> 1.0
                gamepad1.left_bumper -> -1.0
                else -> 0.0
            }
            //robot.intake.sweeperPower = pad1Triggers

            //LIFT
            robot.lift.power = pad2RightStickY

            //
            ////////////////////////////////////////////////////////////////
            //
            /*EMERGENCY!!!!!!!*/
            //
            ////////////////////////////////////////////////////////////////
            //

            if (emergencyButton.wasJustPressed()) {
                emergencyMode = abs(emergencyMode-1)
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