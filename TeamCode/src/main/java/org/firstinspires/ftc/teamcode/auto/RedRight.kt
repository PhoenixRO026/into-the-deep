package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

@Autonomous
class RedRight : LinearOpMode(){

    override fun runOpMode() {
        val dash = FtcDashboard.getInstance()

        telemetry = MultipleTelemetry(telemetry, dash.telemetry)

        val startPose = Pose2d(10.0, -57.0, Math.toRadians(90.0))
        val submerssible = Pose2d(10.0,-36.0,Math.toRadians(90.0))
        val take_specimen = Pose2d(38.0,-44.0,Math.toRadians(0.0))

        val drive = MecanumDrive(hardwareMap, startPose)

        val action = drive.actionBuilder(startPose)
            .setTangent(Math.toRadians(90.0))
            .lineToY(-36.0)
            .strafeTo(take_specimen.position)
            .lineToX(48.0)
            .waitSeconds(1.0)
            .setTangent(Math.toRadians(0.0))
            .lineToX(58.5)
            .waitSeconds(1.0)
            .setTangent(Math.toRadians(0.0))
            .lineToX(57.0)
            .turnTo(Math.toRadians(55.0))
            .waitSeconds(1.0)
            .turnTo(Math.toRadians(-90.0))
            .waitSeconds(1.0)
            .setTangent(Math.toRadians(180.0))
            .lineToX(38.0)
            .waitSeconds(1.0)
            .strafeTo(submerssible.position)
            .waitSeconds(1.0)
            .strafeTo(take_specimen.position)
            .waitSeconds(1.0)
            .strafeTo(submerssible.position)
            .waitSeconds(1.0)
            .strafeTo(take_specimen.position)
            .waitSeconds(1.0)
            .strafeTo(submerssible.position)
            .waitSeconds(1.0)
            .build()
    }
}