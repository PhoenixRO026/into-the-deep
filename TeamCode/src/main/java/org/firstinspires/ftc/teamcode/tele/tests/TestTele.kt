package org.firstinspires.ftc.teamcode.tele.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.SequentialAction
import com.lib.units.SleepAction
import com.lib.units.deg
import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.TeleRobot
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigTransilvaniaCollege
import org.firstinspires.ftc.teamcode.tele.values.robotValuesTransilvaniaCollege
import kotlin.math.abs

@TeleOp
class TestTele : LinearOpMode() {
    override fun runOpMode() {
        val config = robotHardwareConfigTransilvaniaCollege
        val values = robotValuesTransilvaniaCollege
        var emergencyMode : Int = 0

        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        telemetry.addData("Config name", config.name)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        val timeKeep = TimeKeep()
        val robot = TeleRobot(hardwareMap, config, values, timeKeep)

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

            var launchAction: Action? = null

            fun launch() {
                launchAction = SequentialAction(
                    /*
                    robot.lift.targetPosition = 1000,
                    robot.intake.intakeUp(),
                    robot.intake.boxUp(),
                    robot.outtake.extendoTargetPos = values.outtake.extendoIntakePos,

                    robot.outtake.armTargetToIntake(),

                    robot.lift.targetPosition = 300,
*/
                robot.lift.liftToPosAction(1000),
                    SleepAction(2.0.s),
                    robot.lift.liftToPosAction(1000)

                )

            }

            //val pad2LeftBumper = gamepad2.left_bumper

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
                if (gamepad2.right_bumper){
                    //robot.lift.liftToPosAction(338)   // DONT USE ACTIONS IN TELE
                    //robot.lift.targetPosition = 338



                    //robot.outtake.clawPos = 0.0
                }
            }

            //
            ////////////////////////////////////////////////////////////////
            //
            /*EMERGENCY!!!!!!!*/
            //
            ////////////////////////////////////////////////////////////////
            //

            /*END OF BACKUP*/

            robot.update()

            robot.addTelemetry(telemetry)
            telemetry.addData("x pressed", gamepad2.x)
            telemetry.update()
        }
    }
}