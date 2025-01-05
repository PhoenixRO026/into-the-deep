package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.robot.Arm
import org.firstinspires.ftc.teamcode.robot.Claw
import org.firstinspires.ftc.teamcode.robot.Functions
import org.firstinspires.ftc.teamcode.robot.Intake
import org.firstinspires.ftc.teamcode.robot.Lift
import org.firstinspires.ftc.teamcode.robot.wallGrab

class RedRightv2 : LinearOpMode(){

    override fun runOpMode() {
        val dash = FtcDashboard.getInstance()

        telemetry = MultipleTelemetry(telemetry, dash.telemetry)

        val startPose = Pose2d(10.0, -57.0, Math.toRadians(90.0))
        val submerssible = Pose2d(10.0,-36.0,Math.toRadians(90.0))
        val take_specimen = Pose2d(38.0,-50.0,Math.toRadians(0.0))

        var claw = Claw(hardwareMap)
        var arm = Arm(hardwareMap)
        var intake = Intake(hardwareMap)
        var lift = Lift(hardwareMap)
        var functions = Functions(hardwareMap)

        val drive = MecanumDrive(hardwareMap, startPose)

        /*
        waitForStart()

        while (isStarted && !isStopRequested) {
            val now = System.currentTimeMillis().ms
            sideDeltaTime = now - previousTime
            previousTime = now

            expansionHub.clearBulkCache()
        }*/

        val action = SequentialAction()
        drive.actionBuilder(startPose)
            .setTangent(Math.toRadians(90.0))
            .lineToY(-40.0)
            .afterTime(0.0, functions.scoreSecondBar())
            .waitSeconds(1.0)
            .setTangent(Math.toRadians(0.0))
            .lineToXLinearHeading(34.0,Math.toRadians(45.0))
            .afterTime(0.0, functions.grab())
            .waitSeconds(1.0)
            .turnTo(Math.toRadians(-50.0))
            .afterTime(0.0, functions.release())
            .waitSeconds(1.0)
            .setTangent(Math.toRadians(0.0))
            .lineToXLinearHeading(40.0,Math.toRadians(35.0))
            .afterTime(0.0, functions.grab())
            .waitSeconds(1.0)
            .turnTo(Math.toRadians(-50.0))
            .afterTime(0.0, functions.release())
            .waitSeconds(1.0)
            .setTangent(0.0)
            .lineToXLinearHeading(50.0,Math.toRadians(35.0))
            .afterTime(0.0, functions.grab())
            .waitSeconds(1.0)
            .turnTo(Math.toRadians(-90.0))
            .setTangent(Math.toRadians(180.0))
            .lineToX(38.0)
            .wallGrab(arm, claw)
            .strafeTo(take_specimen.position)
            .waitSeconds(1.0)
            .afterTime(0.0, functions.scoreSecondBar())
            .strafeTo(submerssible.position)
            .waitSeconds(1.0)
            .wallGrab(arm, claw)
            .strafeTo(take_specimen.position)
            .afterTime(0.0, functions.scoreSecondBar())
            .strafeTo(submerssible.position)
            .wallGrab(arm, claw)
            .strafeTo(take_specimen.position)
            .afterTime(0.0, functions.scoreSecondBar())
            .strafeTo(submerssible.position)
            .build()
    }
}