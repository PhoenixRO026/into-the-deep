package org.firstinspires.ftc.teamcode.tele

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.lib.units.deg
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
class MotorsTestTele : LinearOpMode() {
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

            //TESTING THE LIFT MOTORS

            if (gamepad2.x){
                robot.lift.targetPosition = 682
            }
            if (gamepad2.b){
                robot.lift.targetPosition = 306
            }

            //TESTING THE OUTTAKE EXTENDO

            if(gamepad2.y){
                robot.outtake.extendoTargetPos = 435.7.deg
            }
            if (gamepad2.a){
                //robot.outtake.extendo
            }

            //OUTTAKE EXTENDO MANUAL:
            robot.outtake.extendoPower = pad2LeftStickY

            //LIFT MANUAL:
            robot.lift.power = pad2RightStickY

            robot.update()

            robot.addTelemetry(telemetry)
            telemetry.update()
        }
    }
}