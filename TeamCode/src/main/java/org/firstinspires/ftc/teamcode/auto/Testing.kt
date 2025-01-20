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
import org.firstinspires.ftc.teamcode.robot.Robot
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigTransilvaniaCollege
import org.firstinspires.ftc.teamcode.tele.values.robotValuesTransilvaniaCollege

@Autonomous
class Testing : LinearOpMode() {
    private val startPose = Pose(0.0.inch, 0.0.inch, 0.0.deg)

    override fun runOpMode() {
        val config = robotHardwareConfigTransilvaniaCollege
        val values = robotValuesTransilvaniaCollege

        val dash = FtcDashboard.getInstance()
        telemetry = MultipleTelemetry(telemetry, dash.telemetry)

        telemetry.addData("Config name", config.name)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        val timeKeep = TimeKeep()
        val robot = Robot(hardwareMap, config, values, timeKeep)
        val mecanumDrive = MecanumDriveEx(hardwareMap, startPose.pose2d)
        val pinPointLocalizer = PinpointLocalizer(hardwareMap, startPose.pose2d)
        var currPose = startPose

        val action = mecanumDrive.actionBuilder(startPose.pose2d).ex()
            .turn(1080.deg)
            .lineToX(10.inch)
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

            currPose = pinPointLocalizer.getPose().pose

            robot.addTelemetry(telemetry)
            telemetry.addData("X pos", currPose.position.x)
            telemetry.addData("y pos", currPose.position.y)
            telemetry.addData("angle", currPose.heading)
            telemetry.update()
        }
    }
}