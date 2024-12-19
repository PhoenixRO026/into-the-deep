package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

@Autonomous
class RedLeft : LinearOpMode() {
    override fun runOpMode() {
        val dash =FtcDashboard.getInstance()

        telemetry = MultipleTelemetry(telemetry, dash.telemetry)

        val startPose = Pose2d(12.0, -62.0, Math.toRadians(-90.0))

        val drive = MecanumDrive(hardwareMap, startPose)

        val action = drive.actionBuilder(startPose)
            .setTangent(Math.toRadians(-90.0))

    }
}