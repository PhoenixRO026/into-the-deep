package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.lib.units.SleepAction
import org.firstinspires.ftc.teamcode.lib.units.Time
import org.firstinspires.ftc.teamcode.lib.units.ms
import org.firstinspires.ftc.teamcode.lib.units.s
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.robot.Arm
import org.firstinspires.ftc.teamcode.robot.Claw

@Autonomous

class AutoTest : LinearOpMode() {


    override fun runOpMode() {
        var claw = Claw(hardwareMap)
        var arm = Arm(hardwareMap)
        var deltaTime: Time
        var sideDeltaTime = 20.ms
        var previousTime = System.currentTimeMillis().ms
        var auto = RedLeftv2()

        val startPose = Pose2d(-50.0, -61.0, Math.toRadians(0.0))
        val drive = MecanumDrive(hardwareMap, startPose)

        waitForStart()

        //claw.finger= 0.3
/*
        while (isStarted && !isStopRequested) {
            val now = System.currentTimeMillis().ms
            sideDeltaTime = now - previousTime
            previousTime = now
        }*/

        val action = drive.actionBuilder(startPose)
            //.lineToX()


        var running = true

        //val c = Canvas()
        //action.preview(c)

        while (isStarted && !isStopRequested && running) {
            val now = System.currentTimeMillis().ms
            deltaTime = now - previousTime
            previousTime = now

            //controlHub.clearBulkCache()

            //val p = TelemetryPacket()
            //p.fieldOverlay().operations.addAll(c.operations)

            //running = action.run(TelemetryPacket())

        }
    }
}