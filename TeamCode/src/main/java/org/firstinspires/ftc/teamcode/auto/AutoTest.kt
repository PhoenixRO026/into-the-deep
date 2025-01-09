package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.SequentialAction
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.lib.units.SleepAction
import org.firstinspires.ftc.teamcode.lib.units.Time
import org.firstinspires.ftc.teamcode.lib.units.ms
import org.firstinspires.ftc.teamcode.lib.units.s
import org.firstinspires.ftc.teamcode.robot.Arm
import org.firstinspires.ftc.teamcode.robot.Claw

@Autonomous

class AutoTest : LinearOpMode() {


    override fun runOpMode() {
        val claw = Claw(hardwareMap)
        var arm = Arm(hardwareMap)
        var deltaTime: Time
        var sideDeltaTime = 20.ms
        var previousTime = System.currentTimeMillis().ms

        waitForStart()

        //claw.finger= 0.3
/*
        while (isStarted && !isStopRequested) {
            val now = System.currentTimeMillis().ms
            sideDeltaTime = now - previousTime
            previousTime = now
        }*/

        val action = SequentialAction(
            claw.openClaw(),
            SleepAction(2.s),
            claw.closeClaw(),
            SleepAction(2.s)
        )

        var running = true

        val c = Canvas()
        action.preview(c)

        while (isStarted && !isStopRequested && running) {
            val now = System.currentTimeMillis().ms
            deltaTime = now - previousTime
            previousTime = now

            //controlHub.clearBulkCache()

            val p = TelemetryPacket()
            p.fieldOverlay().operations.addAll(c.operations)

            running = action.run(TelemetryPacket())

        }
    }
}