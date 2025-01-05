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

        val startPose = Pose2d(-50.0, -61.0, Math.toRadians(0.0))
        val basket = Pose2d(-55.0,-55.0, Math.toRadians(45.0))
        val first_yellow = Pose2d(-47.0,-47.0, Math.toRadians(90.0))
        val mid_yellow = Pose2d(-47.0,-47.0, Math.toRadians(120.0))
        val last_yellow = Pose2d(-55.0,-47.0, Math.toRadians(120.0))

        val drive = MecanumDrive(hardwareMap, startPose)

        val action = drive.actionBuilder(startPose)
            .waitSeconds(3.0)
            .setTangent(90.0)
            .splineToLinearHeading(Pose2d(first_yellow.position, first_yellow.heading), Math.toRadians(90.0))
            .waitSeconds(3.0)
            .setTangent(Math.toRadians(90.0))
            .setTangent(Math.toRadians(45.0))
            .lineToXLinearHeading(basket.position.x, basket.heading)
            .waitSeconds(3.0)
            .setTangent(Math.toRadians(45.0))
            .lineToXLinearHeading(mid_yellow.position.x, mid_yellow.heading)
            .waitSeconds(3.0)
            .setTangent(Math.toRadians(-135.0))
            .lineToXLinearHeading(basket.position.x, basket.heading)
            .waitSeconds(3.0)
            .setTangent(Math.toRadians(0.0))
            .splineToLinearHeading(Pose2d(last_yellow.position, last_yellow.heading), Math.toRadians(90.0))
            .waitSeconds(3.0)
            .setTangent(Math.toRadians(-90.0))
            .splineToLinearHeading(Pose2d(basket.position, basket.heading), Math.toRadians(-135.0))
            .waitSeconds(3.0)
            .build()

    }
}