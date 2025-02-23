package org.firstinspires.ftc.teamcode.tele

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.SequentialAction
import com.lib.units.SleepAction
import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader
import org.firstinspires.ftc.teamcode.robot.FunctionsForTele
import org.firstinspires.ftc.teamcode.robot.TeleRobot
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigTransilvaniaCollege
import org.firstinspires.ftc.teamcode.tele.values.robotValuesTransilvaniaCollege
import kotlin.math.abs
import kotlin.math.absoluteValue

@TeleOp
class CraniTeleExperiment : LinearOpMode() {
    override fun runOpMode() {
        val config = robotHardwareConfigTransilvaniaCollege
        val values = robotValuesTransilvaniaCollege
        var emergencyMode : Int = 0

        val hsvValues = floatArrayOf(0f, 0f, 0f)
        val valuesColor = hsvValues
        val SCALE_FACTOR = 255.0


        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        telemetry.addData("Config name", config.name)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        val timeKeep = TimeKeep()
        val functions = FunctionsForTele(hardwareMap, config, values, timeKeep, telemetry)

        val relativeLayoutId = hardwareMap.appContext.resources.getIdentifier("RelativeLayout", "id", hardwareMap.appContext.packageName)
        val relativeLayout = (hardwareMap.appContext as Activity).findViewById<View>(relativeLayoutId)

        var action: Action? = null

        telemetry.addData("Config name", config.name)
        telemetry.addLine("READY!")
        telemetry.update()

        fun updateAction(){
            action?.let {
                if (!it.run(TelemetryPacket())) {
                    action = null
                }
            }
        }


        functions.robot.lift.targetPosition = values.lift.liftIntakePos
        //robot.outtake.clawPos = 0.0
        //robot.lift.targetPosition = values.lift.liftWaitingPos
        //robot.intake.sweeperPower = 0.0
        //robot.intake.intakeDown()


        //robot.outtake.clawPos = 0.0

        waitForStart()

        functions.robot.outtake.shoulderCurrentPos = values.outtake.shoulderRobotPos
        functions.robot.outtake.elbowCurrentPos = values.outtake.elbowRobotPos
        functions.robot.outtake.wristPosToMiddle()
        functions.robot.outtake.clawPos = 1.0
        functions.robot.intake.intakeTiltCurrentPos = values.intake.intakeUpPos

        val emergencyButton = ButtonReader { gamepad2.ps }

        while (isStarted && !isStopRequested) {
            emergencyButton.readValue()
            timeKeep.resetDeltaTime()
            val pad1LeftStickY = -gamepad1.left_stick_y.toDouble()
            val pad1LeftStickX = gamepad1.left_stick_x.toDouble()
            val pad1RightStickX = gamepad1.right_stick_x.toDouble()
            val pad1Triggers = gamepad1.right_trigger.toDouble() - gamepad1.left_trigger.toDouble()

            val pad2LeftStickY = -gamepad2.left_stick_y.toDouble()
            val pad2LeftStickX = gamepad2.left_stick_x.toDouble()
            val pad2RightStickY = -gamepad2.right_stick_y.toDouble()
            val pad2RightStickX = gamepad2.right_stick_x.toDouble()

            Color.RGBToHSV(
                (functions.robot.intake.intakeColorSensor.red() * SCALE_FACTOR).toInt(),
                (functions.robot.intake.intakeColorSensor.green() * SCALE_FACTOR).toInt(),
                (functions.robot.intake.intakeColorSensor.blue() * SCALE_FACTOR).toInt(),
                hsvValues
            )

            //DRIVE
            if (gamepad1.y)
                functions.robot.drive.resetFieldCentric()

            functions.robot.drive.isSlowMode = gamepad1.right_trigger >= 0.2

            functions.robot.drive.driveFieldCentric(
                pad1LeftStickY,
                -pad1LeftStickX,
                -pad1RightStickX
            )


            if(emergencyMode == 0) {
                //OUTTAKE

                if (gamepad2.left_bumper) {
                    action = functions.liftSample()
                }
                else if (gamepad2.right_bumper){
                    action = functions.getSample()
                }
                else if (gamepad2.b){
                    action = functions.initRobot()
                }
                else if (gamepad2.a){
                    functions.robot.outtake.armTargetToSpecimen()
                }
                else if (gamepad2.y) {
                    functions.robot.outtake.armTargetToBar()
                }

                if (gamepad2.dpad_up){
                    functions.robot.outtake.extendoTargetPos = values.outtake.extendoOutPos
                }

                if (gamepad2.dpad_down){
                    functions.robot.outtake.extendoTargetPos = values.outtake.extendoRobotPos
                }

                functions.robot.outtake.clawPos = gamepad2.right_trigger.toDouble()

                if (gamepad2.dpad_right) {
                    functions.robot.intake.boxUp()
                } else if (gamepad2.dpad_left) {
                    functions.robot.intake.boxDown()
                }
            }

            if (gamepad1.a){
                functions.robot.intake.resetExtendoPosition()
            }

            if(gamepad1.dpad_up){
                functions.robot.intake.intakeUp()
            }
            else if(gamepad1.dpad_down){
                functions.robot.intake.intakeDown()
            }
            else if(gamepad1.dpad_right){
                functions.robot.intake.intakeUp()
            }
            else if(gamepad1.dpad_left){
                functions.robot.intake.intakeDown()
            }

            //INTAKE
            if (gamepad1.left_trigger >= 0.2) {
                functions.robot.intake.sweeperPower = -1.0
            }
            else if (functions.robot.intake.intakeTiltCurrentPos in 0.48..0.5){
                if (functions.robot.intake.shouldStopIntake("BLUE", hsvValues[0], false)){
                    functions.robot.intake.sweeperPower = 0.0
                }
                else {
                    functions.robot.intake.sweeperPower = 1.0
                }
            }
            else{
                if (functions.robot.intake.shouldStopIntake("BLUE", hsvValues[0], false)){
                    functions.robot.intake.sweeperPower = 0.0
                }
                else {
                    functions.robot.intake.sweeperPower = 0.5
                }
            }

            if(gamepad1.right_bumper){
                action = functions.intakeSubmersible()
            }
            else if(gamepad1.left_bumper){
                action = functions.intakeRobot()
            }

            //LIFT
            functions.robot.lift.power = pad2RightStickY

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

                functions.robot.outtake.extendoSpeed = when {
                    pad2LeftStickY >= 0.2 -> 1.0
                    pad2LeftStickY <= -0.2 -> -1.0
                    else -> 0.0
                }

                functions.robot.outtake.shoulderSpeed = when {
                    gamepad2.right_bumper -> -1.0
                    gamepad2.left_bumper -> 1.0
                    else -> 0.0
                }

                functions.robot.outtake.elbowSpeed = when {
                    gamepad2.y -> 1.0
                    gamepad2.a -> -1.0
                    else -> 0.0
                }
                functions.robot.outtake.wristSpeed = when {
                    gamepad2.dpad_left -> -1.0
                    gamepad2.dpad_right -> 1.0
                    else -> 0.0
                }
                functions.robot.outtake.clawPos = gamepad2.right_trigger.toDouble()

                functions.robot.intake.boxTiltSpeed = when {
                    gamepad2.dpad_up -> 1.0
                    gamepad2.dpad_up -> -1.0
                    else -> 0.0
                }


                //LIFT
                functions.robot.lift.power = pad2RightStickY
            }

            /*END OF BACKUP*/

            updateAction()

            functions.robot.update()

            functions.robot.addTelemetry(telemetry)

            telemetry.addData("emergency mode value",emergencyMode)
            telemetry.update()
        }
    }
}