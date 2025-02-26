package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.SequentialAction
import com.lib.roadrunner_ext.delayedBy
import com.lib.units.Distance2d
import com.lib.units.Pose
import com.lib.units.SleepAction
import com.lib.units.deg
import com.lib.units.inch
import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Intake
import org.firstinspires.ftc.teamcode.robot.Robot

@Autonomous
class RedRight : LinearOpMode() {
    private val startPose = Pose(4.inch, -60.inch, 90.deg)
    private val firstSpecimenBeforePos = Pose(4.inch, -40.inch, 90.deg)
    private val firstSpecimenPos = Pose(4.inch, -36.inch, 90.deg)
    private val red1Pos = Distance2d(48.inch, -25.5.inch)
    private val red2Pos = Distance2d(58.5.inch, -25.5.inch)
    private val red3Pos = Distance2d(68.5.inch, -25.5.inch)
    private val zonePos = Distance2d(60.inch, -60.inch)
    private val firstSamplePos = Distance2d(30.inch, -40.inch).headingTowards(red1Pos)
    private val secondSamplePos = Distance2d(34.inch, -40.inch).headingTowards(red2Pos)
    private val thirdSamplePos = Distance2d(38.inch, -40.inch).headingTowards(red3Pos)
    private val firstKickPos = Distance2d(30.inch, -50.inch).headingTowards(zonePos)
    private val secondKickPos = Distance2d(34.inch, -50.inch).headingTowards(zonePos)
    private val thirdKickPos = Distance2d(38.inch, -50.inch).headingTowards(zonePos)
    private val takeSpecimenPos = Pose(34.inch, -56.inch, 90.deg)

    override fun runOpMode() {
        initMessage()

        val timeKeep = TimeKeep()
        val robot = Robot(hardwareMap, startPose)
        val drive = robot.drive
        val intake = robot.intake
        val lift = robot.lift
        val outtake = robot.outtake
        robot.initAuto()

        fun sampleCycle(samplePose: Pose) = SequentialAction(

        )

        val action = SequentialAction(
            ParallelAction(
                robot.armAndLiftToBar(),
                drive.actionBuilder(startPose)
                    .strafeToLinearHeading(firstSpecimenBeforePos)
                    .build()
            ),
            drive.actionBuilder(firstSpecimenBeforePos)
                .strafeToLinearHeading(firstSpecimenPos)
                .build(),
            outtake.openClawAction(),
            ParallelAction(
                robot.armAndLiftToNeutral().delayedBy(1.s),
                drive.actionBuilder(firstSpecimenPos)
                    .strafeToLinearHeading(firstSamplePos)
                    .build()
            )
        )

        action.preview(previewCanvas)

        readyMessage()

        while (opModeInInit()) {
            timeKeep.resetDeltaTime()
            sleep(50)
        }

        var running = true

        while (isStarted && !isStopRequested && running) {
            timeKeep.resetDeltaTime()
            robot.update(timeKeep.deltaTime)

            running = runAction(action)

            robot.addTelemetry(telemetry, timeKeep.deltaTime)
            telemetry.update()
        }
    }

    private val previewCanvas = Canvas()

    private fun runAction(action: Action): Boolean {
        val packet = TelemetryPacket()
        packet.fieldOverlay().operations.addAll(previewCanvas.operations)

        val running = action.run(packet)

        FtcDashboard.getInstance().sendTelemetryPacket(packet)

        return running
    }

    private fun initMessage() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        telemetry.addLine("INITIALIZING")
        telemetry.update()
    }

    private fun readyMessage() {
        telemetry.addLine("READY")
        telemetry.update()
    }
}