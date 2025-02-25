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
class RedRight : LinearOpMode() {
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
        val submerssible = Pose(10.0.inch,-38.0.inch, 90.0.deg)
        val take_specimen = Pose(38.0.inch,-50.0.inch, 0.0.deg)
        val parking = Pose(58.0.inch, -55.0.inch, 0.0.deg)
        val wallGrab = Pose(33.0.inch, -53.0.inch, 0.0.deg)
        val scoring = Pose(10.0.inch, -36.0.inch, 0.0.deg)

        val timeKeep = TimeKeep()
        val robot = AutoRobot(hardwareMap, config, values, timeKeep, startPose, telemetry)
        val mecanumDrive = robot.roadRunnerDrive

        robot.outtake.extendoCurrentPos = values.outtake.extendoRobotPos
        robot.intake.intakeUp()
        robot.outtake.shoulderCurrentPos = values.outtake.shoulderRobotPos
        robot.outtake.elbowCurrentPos = values.outtake.elbowRobotPos
        robot.outtake.clawPos = 0.0

        val getSpecimen : SequentialAction = SequentialAction(
            robot.lift.liftToPosAction(values.lift.inRobot),
            InstantAction {robot.outtake.clawPos = 1.0},
            robot.outtake.shoudlerToPosAction(values.outtake.shoulderSpecimenPos),
            robot.outtake.elbowToPosAction(values.outtake.elbowSpecimenPos),
            SleepAction(1.0.s),
            InstantAction {robot.outtake.clawPos = 0.0},
        )

        val scoreSpecimen = SequentialAction(
            robot.lift.liftToPosAction(values.lift.secondBar),
            robot.outtake.shoudlerToPosAction(values.outtake.shoulderBarPos),
            robot.outtake.elbowToPosAction(values.outtake.elbowBarPos),
            robot.outtake.extendoToPosAction(values.outtake.extendoOutPos),
            SleepAction(0.5.s),
            InstantAction {robot.outtake.clawPos = 1.0}
        )


        val action = mecanumDrive.actionBuilder(startPose.pose2d).ex()
            ///PRELOAD
            .afterTime(0.0, scoreSpecimen)
            .waitSeconds(3.5.s)
            .setTangent(Math.toRadians(90.0))
            .lineToY(-30.0.inch)
            .afterTime(0.5,InstantAction{robot.outtake.clawPos = 1.0})
            .waitSeconds(5.0.s)

            /// more ig
/*
            .setTangent(Math.toRadians(-90.0))
            .splineToLinearHeading(pivot.pose2d, pivot.heading.asDeg)
            .turnTo(Math.toRadians(-45.0))
            .setTangent(Math.toRadians(0.0))
            .lineToXLinearHeading(40.0,Math.toRadians(35.0))
            .turnTo(Math.toRadians(-50.0))
            .setTangent(0.0)
            .lineToXLinearHeading(50.0,Math.toRadians(35.0))
            .turnTo(Math.toRadians(-90.0))
            .setTangent(Math.toRadians(180.0))
            .strafeTo(wallGrab.position)
            .strafeTo(scoring.position)
            .lineToY(-40.0.inch)
            .strafeTo(wallGrab.position)
            .strafeTo(scoring.position)
            .lineToY(-40.0.inch)
            .strafeTo(wallGrab.position)
            .strafeTo(scoring.position)
            .lineToY(-40.0.inch)*/

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