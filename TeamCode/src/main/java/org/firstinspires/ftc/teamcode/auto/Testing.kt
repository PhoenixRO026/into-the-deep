package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.lib.roadrunner_ext.ex
import com.lib.units.Pose
import com.lib.units.deg
import com.lib.units.inch
import com.lib.units.pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDriveEx
import org.firstinspires.ftc.teamcode.roadrunner.PinpointLocalizer
import org.firstinspires.ftc.teamcode.robot.AutoRobot
import org.firstinspires.ftc.teamcode.robot.TeleRobot
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigTransilvaniaCollege
import org.firstinspires.ftc.teamcode.tele.values.robotValuesTransilvaniaCollege

@Autonomous
class Testing : LinearOpMode() {
    private val startPose = Pose(0.0.inch, 0.0.inch, 0.0.deg)
    private val newPose = Pose(12.0.inch, 25.0.inch, 0.0.deg)

    override fun runOpMode() {
        val config = robotHardwareConfigTransilvaniaCollege
        val values = robotValuesTransilvaniaCollege

        val dash = FtcDashboard.getInstance()
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        telemetry.addData("Config name", config.name)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        val timeKeep = TimeKeep()
        val robot = AutoRobot(hardwareMap, config, values, timeKeep, startPose, telemetry)
        val mecanumDrive = robot.roadRunnerDrive
        var currPose = startPose

        val action = mecanumDrive.actionBuilder(startPose.pose2d).ex()
            .setTangent(0.0)
            .turn(1080.deg)
            .setTangent(0.0)
            .splineTo(newPose.position, newPose.heading)
            .setTangent(0.0)
            .turn(180.deg)
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

            currPose = mecanumDrive.localizer.pose.pose

            robot.addTelemetry(telemetry)
            telemetry.addData("X pos", currPose.position.x)
            telemetry.addData("y pos", currPose.position.y)
            telemetry.addData("angle", currPose.heading)
            telemetry.update()
        }
    }
}