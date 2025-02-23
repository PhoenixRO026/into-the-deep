package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.SequentialAction
import com.lib.roadrunner_ext.ex
import com.lib.units.Pose
import com.lib.units.Pose2d
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
class RedRightMovement : LinearOpMode() {
    private val startPose = Pose(0.0.inch, 0.0.inch, 0.0.deg)

    override fun runOpMode() {
        val config = robotHardwareConfigTransilvaniaCollege
        val values = robotValuesTransilvaniaCollege

        val dash = FtcDashboard.getInstance()
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        telemetry.addData("Config name", config.name)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        val startPose = Pose(10.0.inch, -62.0.inch, 90.0.deg)
        val pivot = Pose(35.0.inch,-38.0.inch, 45.0.deg)
        val wallGrab = Pose(35.0.inch, -60.0.inch, 90.0.deg)
        val pivot2 = Pose(35.0.inch, -55.0.inch, 90.0.deg)
        val scoring = Pose(10.0.inch, -38.0.inch, 90.0.deg)

        val timeKeep = TimeKeep()
        val robot = AutoRobot(hardwareMap, config, values, timeKeep, startPose, telemetry)
        val mecanumDrive = robot.roadRunnerDrive



        val action = mecanumDrive.actionBuilder(startPose.pose2d).ex()
            ///PRELOAD
            .waitSeconds(1.s)
            .setTangent(Math.toRadians(90.0))
            .lineToY(-33.0.inch)
            //.afterTime(0.5,InstantAction{robot.outtake.clawPos = 1.0})
            .waitSeconds(1.0.s)

            /// more ig

            .setTangent(Math.toRadians(-90.0))
            .splineToLinearHeading(pivot.pose2d, pivot.heading.asDeg)
            .turnTo(Math.toRadians(-45.0))
            .setTangent(Math.toRadians(0.0))
            .lineToXLinearHeading(40.0,Math.toRadians(35.0))
            .turnTo(Math.toRadians(-50.0))
            .setTangent(0.0)
            .lineToXLinearHeading(50.0,Math.toRadians(35.0))
            .turnTo(Math.toRadians(-90.0))
            .setTangent(Math.toRadians(0.0))
            .splineToLinearHeading(pivot2.pose2d, pivot2.heading.asDeg)
            .setTangent(Math.toRadians(-90.0))
            .lineToY(-60.0.inch)
            .waitSeconds(1.0)
            .strafeTo(scoring.position)
            .waitSeconds(0.5)
            .lineToY(-33.0.inch)
            .strafeTo(wallGrab.position)
            .strafeTo(scoring.position)
            .waitSeconds(0.5)
            .lineToY(-33.0.inch)
            .strafeTo(wallGrab.position)
            .strafeTo(scoring.position)
            .waitSeconds(0.5)
            .lineToY(-33.0.inch)
            .build()

        telemetry.addData("Config name", config.name)
        telemetry.addLine("READY!")
        telemetry.update()

        waitForStart()

        val canvas = Canvas()
        action.preview(canvas)

        var running = true

        while (isStarted && !isStopRequested && running) {
            timeKeep.resetDeltaTime()

            val packet = TelemetryPacket()
            packet.fieldOverlay().operations.addAll(canvas.operations)

            running = action.run(packet)
            dash.sendTelemetryPacket(packet)

            robot.update()

            robot.addTelemetry(telemetry)
            telemetry.update()
        }
    }
}