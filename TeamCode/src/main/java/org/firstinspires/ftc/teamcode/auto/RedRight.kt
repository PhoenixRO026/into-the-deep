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
import com.lib.units.deg
import com.lib.units.inch
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDriveEx
import org.firstinspires.ftc.teamcode.robot.Intake
import org.firstinspires.ftc.teamcode.robot.Robot
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigWonder
import org.firstinspires.ftc.teamcode.tele.values.robotValuesWonder

@Autonomous
class RedRight : LinearOpMode() {
    private val startPose = Pose(0.0.inch, 0.0.inch, 0.0.deg)

    override fun runOpMode() {
        val config = robotHardwareConfigWonder
        val values = robotValuesWonder

        val dash = FtcDashboard.getInstance()
        telemetry = MultipleTelemetry(telemetry, dash.telemetry)

        telemetry.addData("Config name", config.name)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        val startPose = Pose2d(10.0.inch, -57.0.inch, 90.0.deg)
        val submerssible = Pose2d(10.0.inch,-36.0.inch, 90.0.deg)
        val take_specimen = Pose2d(38.0.inch,-50.0.inch, 0.0.deg)

        val timeKeep = TimeKeep()
        val robot = Robot(hardwareMap, config, values, timeKeep)
        val mecanumDrive = MecanumDriveEx(hardwareMap, startPose.pose2d)

        //val values_Drive =

        //val second_bar = 1

        val action = mecanumDrive.actionBuilder(startPose.pose2d).ex()
            .setTangent(Math.toRadians(90.0))
            .lineToY(-40.0)
            .afterTime(0.0,SequentialAction(
                robot.lift.liftToPosAction(values.lift.secondBar),
                //robot.outtake.elbowToPosAction(values.outtake.elbowSpecimenPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderSpecimenPos),
                InstantAction{robot.outtake.clawPos = 0.0}),
                )
            .lineToY(-45.0)
            .afterTime(0.0,SequentialAction(
                robot.outtake.elbowToPosAction(values.outtake.shoulderWaitingPos),
                robot.lift.liftToPosAction(values.lift.inRobot),
            ))


            //.afterTime(0.0, functions.scoreSecondBar())

            .waitSeconds(1.0)
            .setTangent(Math.toRadians(0.0))
            .lineToXLinearHeading(34.0,Math.toRadians(45.0))
            //.afterTime(0.0, functions.grab())
            .waitSeconds(1.0)
            .turnTo(Math.toRadians(-50.0))
            //.afterTime(0.0, functions.release())
            .waitSeconds(1.0)
            .setTangent(Math.toRadians(0.0))
            .lineToXLinearHeading(40.0,Math.toRadians(35.0))
            //.afterTime(0.0, functions.grab())
            .waitSeconds(1.0)
            .turnTo(Math.toRadians(-50.0))
            //.afterTime(0.0, functions.release())
            .waitSeconds(1.0)
            .setTangent(0.0)
            .lineToXLinearHeading(50.0,Math.toRadians(35.0))
            //.afterTime(0.0, functions.grab())
            .waitSeconds(1.0)
            .turnTo(Math.toRadians(-90.0))
            .setTangent(Math.toRadians(180.0))
            .lineToX(38.0)
            //.wallGrab(arm, claw)
            .strafeTo(take_specimen.position)
            .waitSeconds(1.0)
            //.afterTime(0.0, functions.scoreSecondBar())
            .strafeTo(submerssible.position)
            .waitSeconds(1.0)
            //.wallGrab(arm, claw)
            .strafeTo(take_specimen.position)
            //.afterTime(0.0, functions.scoreSecondBar())
            .strafeTo(submerssible.position)
            //.wallGrab(arm, claw)
            .strafeTo(take_specimen.position)
            //.afterTime(0.0, functions.scoreSecondBar())
            .strafeTo(submerssible.position)
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