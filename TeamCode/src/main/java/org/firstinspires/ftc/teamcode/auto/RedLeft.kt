package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.SequentialAction
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
class RedLeft : LinearOpMode() {
    private val startPose = Pose(-36.inch, -60.inch, 90.deg)
    private val basketPose = Pose(-52.inch, -52.inch, 45.deg)
    private val firstYellowSample = Distance2d(-44.5.inch, -25.5.inch)
    private val firstYellowPose = Distance2d(-48.inch, -51.inch).headingTowards(firstYellowSample)
    private val secondYellowSample = Distance2d(-57.inch, -25.5.inch)
    private val secondYellowPose = Distance2d(-52.inch, -50.inch).headingTowards(secondYellowSample)
    private val thirdYellowSample = Distance2d(-64.5.inch, -25.5.inch)
    private val thirdYellowPose = Distance2d(-47.inch, -45.inch).headingTowards(thirdYellowSample)
    private val parkPose = Pose(-24.inch, -12.inch, 0.deg)

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
            ParallelAction(
                robot.armAndLiftToIntakeWaiting(),
                intake.extendReadyForSampling(),
                drive.actionBuilder(basketPose)
                    .strafeToLinearHeading(samplePose)
                    .build()
            ),
            intake.takeSample(Intake.SensorColor.YELLOW),
            ParallelAction(
                SequentialAction(
                    intake.bringSampleToIntake(),
                    intake.takeOutSample(),
                    robot.armAndLiftToIntake(),
                    outtake.closeClawAction(),
                    lift.liftToBasketAction()
                ),
                drive.actionBuilder(samplePose)
                    .strafeToLinearHeading(basketPose)
                    .build()
            ),
            outtake.armToBasketAction(),
            outtake.openClawAction(),
            SleepAction(0.2.s),
        )

        val action = SequentialAction(
            ParallelAction(
                lift.liftToBasketAction(),
                drive.actionBuilder(startPose)
                    .strafeToLinearHeading(basketPose)
                    .build()
            ),
            outtake.armToBasketAction(),
            outtake.openClawAction(),
            SleepAction(0.2.s),
            sampleCycle(firstYellowPose),
            sampleCycle(secondYellowPose),
            sampleCycle(thirdYellowPose),
            outtake.armToNeutralAction()
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