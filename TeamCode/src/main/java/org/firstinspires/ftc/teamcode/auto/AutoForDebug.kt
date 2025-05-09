package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.RaceAction
import com.acmerobotics.roadrunner.SequentialAction
import com.lib.roadrunner_ext.delayedBy
import com.lib.units.Distance2d
import com.lib.units.Pose
import com.lib.units.SleepAction
import com.lib.units.cm
import com.lib.units.deg
import com.lib.units.inch
import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Intake
import org.firstinspires.ftc.teamcode.robot.Robot

@Autonomous
class  AutoForDebug : LinearOpMode() {
    private val startPose = Pose(20.cm, -59.5.inch, 90.deg)

    override fun runOpMode() {
        initMessage()

        val timeKeep = TimeKeep()
        val robot = Robot(hardwareMap, startPose)
        val drive = robot.drive
        val intake = robot.intake
        val lift = robot.lift
        val outtake = robot.outtake
        robot.initAuto()

        val action = SequentialAction(
            robot.armAndLiftToBar(),
            robot.armAndLiftToSpecimen().delayedBy(2.s),
            SleepAction(2.s),
            robot.armAndLiftToBar(),
            robot.armAndLiftToSpecimen().delayedBy(1.s),
            SleepAction(2.s),
            robot.armAndLiftToBar(),
            robot.armAndLiftToSpecimen().delayedBy(0.5.s),
            SleepAction(2.s),
            robot.armAndLiftToBar(),
            robot.armAndLiftToSpecimen(),
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