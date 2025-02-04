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

        val startPose = Pose2d(10.0.inch, -57.0.inch, 90.0.deg)
        val submerssible = Pose2d(10.0.inch,-36.0.inch, 90.0.deg)
        val take_specimen = Pose2d(38.0.inch,-50.0.inch, 0.0.deg)
        val parking = Pose(58.0.inch, -55.0.inch, 0.0.deg)
        val wallGrab = Pose(33.0.inch, -53.0.inch, 0.0.deg)
        val scoring = Pose(10.0.inch, -36.0.inch, 0.0.deg)

        val timeKeep = TimeKeep()
        val robot = AutoRobot(hardwareMap, config, values, timeKeep, startPose, telemetry)
        val mecanumDrive = robot.roadRunnerDrive


        val scoreSpecimen : SequentialAction = SequentialAction(
            robot.lift.liftToPosAction(values.lift.secondBar),
            //robot.outtake.elbowToPosAction(values.outtake.elbowSpecimenPos),
            robot.outtake.shoudlerToPosAction(values.outtake.shoulderBarPos),
            robot.outtake.elbowToPosAction(values.outtake.elbowRobotPos),
            robot.lift.liftToPosAction(values.lift.secondBarUp),
            InstantAction{robot.outtake.clawPos = 0.0},
        )

        val toRobot : SequentialAction = SequentialAction(
            robot.outtake.shoudlerToPosAction(values.outtake.shoulderRobotPos),
            robot.outtake.elbowToPosAction(values.outtake.elbowRobotPos),
            robot.lift.liftToPosAction(values.lift.inRobot),
            InstantAction{robot.outtake.clawPos = 1.0}
        )

        val getSample : SequentialAction = SequentialAction(
            InstantAction{ robot.intake.extendoPower = 1.0},
            robot.intake.extendoToPosAction(values.intake.extendoLim),
            robot.intake.extendoToPosAction(values.intake.extendoInBot),
            InstantAction{ robot.intake.extendoPower = 0.0}
        )

        val ejectSample : SequentialAction = SequentialAction(
            robot.intake.extendoToPosAction(values.intake.extendoLim),
            InstantAction{ robot.intake.extendoPower = 1.0},
            SleepAction(1.s),
            robot.intake.extendoToPosAction(values.intake.extendoInBot),
            InstantAction{ robot.intake.extendoPower = 0.0}
        )

        val getSpecimen : SequentialAction = SequentialAction(
            InstantAction{robot.outtake.clawPos = 1.0},
            robot.outtake.shoudlerToPosAction(values.outtake.shoulderSpecimenPos),
            robot.outtake.elbowToPosAction(values.outtake.elbowSpecimenPos),
            InstantAction{ robot.intake.extendoPower = 0.0},
            robot.outtake.shoudlerToPosAction(values.outtake.shoulderRobotPos),
            robot.outtake.elbowToPosAction(values.outtake.elbowRobotPos)
            )


        val action = mecanumDrive.actionBuilder(startPose.pose2d).ex()
            ///PRELOAD
            .setTangent(Math.toRadians(90.0))
            .lineToY(-36.0.inch)
            .afterTime(0.0, scoreSpecimen)
            .afterTime(0.0, toRobot)

            .lineToY(-40.0.inch)
            .setTangent(Math.toRadians(0.0))
            .lineToXLinearHeading(34.0,Math.toRadians(45.0))
            .afterTime(0.0, getSample)

            .turnTo(Math.toRadians(-50.0))
            .afterTime(0.0, ejectSample)
            .setTangent(Math.toRadians(0.0))
            .lineToXLinearHeading(40.0,Math.toRadians(35.0))
            .afterTime(0.0, getSample)
            .turnTo(Math.toRadians(-50.0))
            .afterTime(0.0, ejectSample)
            .setTangent(0.0)
            .lineToXLinearHeading(50.0,Math.toRadians(35.0))
            .afterTime(0.0, getSample)
            .turnTo(Math.toRadians(-90.0))
            .afterTime(0.0, ejectSample)
            .setTangent(Math.toRadians(180.0))

            .strafeTo(wallGrab.position)
            .afterTime(0.0,getSpecimen)
            .strafeTo(scoring.position)
            .afterTime(0.0, scoreSpecimen)
            .lineToY(-40.0.inch)
            .afterTime(0.0, toRobot)

            .strafeTo(wallGrab.position)
            .afterTime(0.0,getSpecimen)
            .strafeTo(scoring.position)
            .afterTime(0.0, scoreSpecimen)
            .lineToY(-40.0.inch)
            .afterTime(0.0, toRobot)

            .strafeTo(wallGrab.position)
            .afterTime(0.0,getSpecimen)
            .strafeTo(scoring.position)
            .afterTime(0.0, scoreSpecimen)
            .lineToY(-40.0.inch)
            .afterTime(0.0, toRobot)
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