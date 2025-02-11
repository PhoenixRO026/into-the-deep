package org.firstinspires.ftc.teamcode.auto

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.lib.roadrunner_ext.ex
import com.lib.units.Pose
import com.lib.units.SleepAction
import com.lib.units.deg
import com.lib.units.inch
import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDriveEx
import org.firstinspires.ftc.teamcode.robot.AutoRobot
import org.firstinspires.ftc.teamcode.robot.TeleRobot
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigTransilvaniaCollege
import org.firstinspires.ftc.teamcode.tele.values.robotValuesTransilvaniaCollege

@Autonomous
class RedNoAuto : LinearOpMode() {

    override fun runOpMode() {
        val startPose = Pose(0.0.inch, 0.0.inch,0.0.deg)
        val config = robotHardwareConfigTransilvaniaCollege
        val values = robotValuesTransilvaniaCollege

        val hsvValues = floatArrayOf(0f, 0f, 0f)
        val valuesColor = hsvValues
        val SCALE_FACTOR = 255.0

        val dash = FtcDashboard.getInstance()
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        telemetry.addData("Config name", config.name)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        val relativeLayoutId = hardwareMap.appContext.resources.getIdentifier("RelativeLayout", "id", hardwareMap.appContext.packageName)
        val relativeLayout = (hardwareMap.appContext as Activity).findViewById<View>(relativeLayoutId)


        val timeKeep = TimeKeep()
        val robot = AutoRobot(hardwareMap, config, values, timeKeep, startPose, telemetry)
        val mecanumDrive = robot.roadRunnerDrive
        val hue = hsvValues[0].toInt()


        robot.intake.intakeUp()
        robot.outtake.shoulderCurrentPos = values.outtake.shoulderRobotPos
        robot.outtake.elbowCurrentPos = values.outtake.elbowRobotPos
        robot.outtake.clawPos = 0.0

        val action = mecanumDrive.actionBuilder(startPose.pose2d).ex()
            .build()

        telemetry.addData("Config name", config.name)
        telemetry.addLine("READY!")
        telemetry.update()

        waitForStart()

        telemetry.update()

        val canvas = Canvas()
        action.preview(canvas)

        var running = true

        while (isStarted && !isStopRequested && running) {
            timeKeep.resetDeltaTime()

            Color.RGBToHSV(
                (robot.intake.intakeColorSensor.red() * SCALE_FACTOR).toInt(),
                (robot.intake.intakeColorSensor.green() * SCALE_FACTOR).toInt(),
                (robot.intake.intakeColorSensor.blue() * SCALE_FACTOR).toInt(),
                hsvValues
            )

            val packet = TelemetryPacket()
            packet.fieldOverlay().operations.addAll(canvas.operations)

            running = action.run(packet)
            dash.sendTelemetryPacket(packet)

            robot.update()

            //robot.addTelemetry(telemetry)
            telemetry.addData("red side", robot.intake.shouldStopIntake("RED", hsvValues[0].toInt()))
            telemetry.addData("blue side", robot.intake.shouldStopIntake("BLUE", hsvValues[0].toInt()))
            telemetry.addData("Red", robot.intake.intakeColorSensor.red())
            telemetry.addData("Green", robot.intake.intakeColorSensor.green())
            telemetry.addData("Blue", robot.intake.intakeColorSensor.blue())
            telemetry.update()
        }
    }
}