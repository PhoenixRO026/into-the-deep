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
import org.firstinspires.ftc.teamcode.robot.TeleRobot
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigTransilvaniaCollege
import org.firstinspires.ftc.teamcode.tele.values.robotValuesTransilvaniaCollege
import kotlin.math.abs
import kotlin.math.absoluteValue

@TeleOp
class CraniTeleRed : LinearOpMode() {
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
        val robot = TeleRobot(hardwareMap, config, values, timeKeep, telemetry)

        val relativeLayoutId = hardwareMap.appContext.resources.getIdentifier("RelativeLayout", "id", hardwareMap.appContext.packageName)
        val relativeLayout = (hardwareMap.appContext as Activity).findViewById<View>(relativeLayoutId)

        var action: Action? = null

        telemetry.addData("Config name", config.name)
        telemetry.addLine("READY!")
        telemetry.update()



        fun getSampleFromIntake() {
            action = SequentialAction(
                InstantAction{robot.outtake.clawPos=1.0},
                robot.lift.liftToPosAction(values.lift.liftWaitingPos),
                ParallelAction(
                    robot.outtake.shoudlerToPosAction(values.outtake.shoulderIntakePos),
                    robot.outtake.elbowToPosAction(values.outtake.elbowIntakePos)
                ),
                //SleepAction(2.0.s),
                robot.lift.liftToPosAction(values.lift.liftIntakePos),
                InstantAction{robot.outtake.clawPos=0.0},
                //SleepAction(2.0.s),
                robot.lift.liftToPosAction(values.lift.liftWaitingPos),
            )
        }


        fun updateAction(){
            action?.let {
                if (!it.run(TelemetryPacket())) {
                    action = null
                }
            }
        }


        robot.lift.targetPosition = values.lift.liftIntakePos
        //robot.outtake.clawPos = 0.0
        //robot.lift.targetPosition = values.lift.liftWaitingPos
        //robot.intake.sweeperPower = 0.0
        //robot.intake.intakeDown()


        //robot.outtake.clawPos = 0.0

        waitForStart()

        robot.outtake.shoulderCurrentPos = 0.6404
        robot.outtake.elbowCurrentPos = 0.5159
        robot.outtake.wristPosToMiddle()
        robot.outtake.clawPos = 1.0

        robot.intake.intakeTiltCurrentPos = 0.5106

        val intakePosButton = ButtonReader { gamepad2.right_bumper}
        val emergencyButton = ButtonReader { gamepad2.ps }

        while (isStarted && !isStopRequested) {
            emergencyButton.readValue()
            intakePosButton.readValue()
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
                (robot.intake.intakeColorSensor.red() * SCALE_FACTOR).toInt(),
                (robot.intake.intakeColorSensor.green() * SCALE_FACTOR).toInt(),
                (robot.intake.intakeColorSensor.blue() * SCALE_FACTOR).toInt(),
                hsvValues
            )

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
                else if (intakePosButton.wasJustPressed()){
                    robot.intake.extendoTargetPosition = 0
                    robot.outtake.clawPos=1.0
                    robot.outtake.extendoTargetPos = values.outtake.extendoIntakePos
                    robot.lift.targetPosition = values.lift.liftWaitingPos
                    robot.outtake.shoulderTargetPos = values.outtake.shoulderIntakePos
                    robot.outtake.elbowTargetPos = values.outtake.elbowIntakePos
                    robot.lift.targetPosition = values.lift.liftIntakePos
                }
                else if (gamepad2.a){
                    robot.outtake.armTargetToSpecimen()
                }
                else if (gamepad2.y) {
                    robot.outtake.elbowTargetPos = 0.894
                    robot.outtake.shoulderTargetPos = 0.8694
                }
                else if (gamepad2.x){
                    robot.outtake.armTargetToBar()
                    robot.lift.targetPosition = 936
                }

                if (gamepad2.dpad_up){
                    robot.outtake.extendoTargetPos = values.outtake.extendoOutPos
                }

                if (gamepad2.dpad_down){
                    robot.outtake.extendoTargetPos = values.outtake.extendoRobotPos
                }
                if (gamepad2.b){
                    robot.outtake.armTargetToRobot()
                }

                //robot.intake.sweeperPower = pad1Triggers

                robot.outtake.clawPos = gamepad2.right_trigger.toDouble()

                if (gamepad2.dpad_right) {
                    robot.outtake.wristTargetPos = 0.493
                } else if (gamepad2.dpad_left) {
                    robot.outtake.wristTargetPos = 0.0
                }
            }

            if (gamepad1.a){
                robot.intake.resetExtendoPosition()
            }

            if(gamepad1.dpad_up){
                robot.intake.intakeUp()
            }
            else if(gamepad1.dpad_down){
                robot.intake.intakeDown()
            }
            //INTAKE
            if (gamepad1.left_trigger >= 0.2) {
                robot.intake.sweeperPower = -1.0
            }
            else if (robot.intake.intakeTiltCurrentPos in 0.48..0.5){
                if (robot.intake.shouldStopIntake("RED", hsvValues[0], false)){
                    robot.intake.sweeperPower = 0.0
                }
                else {
                    robot.intake.sweeperPower = 1.0
                }
            }
            else{
                if (robot.intake.shouldStopIntake("RED", hsvValues[0], false)){
                    robot.intake.sweeperPower = 0.0
                }
                else {
                    robot.intake.sweeperPower = 0.5
                }
            }

            robot.intake.extendoPower =when {
                gamepad1.right_bumper -> 1.0
                gamepad1.left_bumper -> -1.0
                else -> 0.0
            }
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

                robot.outtake.extendoSpeed = when {
                    pad2LeftStickY >= 0.2 -> 1.0
                    pad2LeftStickY <= -0.2 -> -1.0
                    else -> 0.0
                }

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
                robot.lift.power = pad2RightStickY
            }

            /*END OF BACKUP*/

            updateAction()

            robot.update()

            robot.addTelemetry(telemetry)

            telemetry.addData("emergency mode value",emergencyMode)
            telemetry.update()
        }
    }
}