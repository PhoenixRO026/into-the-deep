package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.SequentialAction
import com.lib.roadrunner_ext.ex
import com.lib.units.Pose
import com.lib.units.SleepAction
import com.lib.units.deg
import com.lib.units.inch
import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDriveEx
import org.firstinspires.ftc.teamcode.robot.AutoRobot
import org.firstinspires.ftc.teamcode.robot.TeleRobot
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigTransilvaniaCollege
import org.firstinspires.ftc.teamcode.tele.values.robotValuesTransilvaniaCollege

@Autonomous
class BlueLeft : LinearOpMode() {
    private val startPose = Pose(50.0.inch, 61.0.inch, 180.0.deg)
    private val  basket = Pose(55.0.inch, 55.0.inch, -135.0.deg)
    private val  first_yellow = Pose(47.0.inch, 47.0.inch, -90.0.deg)
    private val  mid_yellow = Pose(47.0.inch, 47.0.inch, -60.0.deg)
    private val  last_yellow = Pose(55.0.inch, 47.0.inch, -60.0.deg)

    override fun runOpMode() {
        val config = robotHardwareConfigTransilvaniaCollege
        val values = robotValuesTransilvaniaCollege

        val dash = FtcDashboard.getInstance()
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        telemetry.addData("Config name", config.name)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        val timeKeep = TimeKeep()
        val robot = AutoRobot(hardwareMap, config, values, timeKeep, startPose, telemetry)
        val mecanumDrive = robot.roadRunnerDrive

        val action = mecanumDrive.actionBuilder(startPose.pose2d).ex()
            .setTangent(Math.toRadians(-90.0))
            .lineToY(47.0)
            .setTangent(Math.toRadians(-135.0))
            .lineToXLinearHeading(basket.position.x, basket.heading)
            //.waitSeconds(3.0)
            .afterTime(0.0, SequentialAction(
                robot.lift.liftToPosAction(values.lift.basketPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderBasketPos),
                robot.outtake.elbowToPosAction(values.outtake.elbowBasketPos),
                InstantAction{robot.outtake.clawPos = 0.0},
                InstantAction{robot.outtake.clawPos = 1.0},
                robot.outtake.elbowToPosAction(values.outtake.elbowWaitingPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderWaitingPos),
                robot.lift.liftToPosAction(values.lift.inRobot),
            ))
            .setTangent(Math.toRadians(-90.0))
            .splineToLinearHeading(Pose(first_yellow.position, first_yellow.heading), -90.0.deg)
            .afterTime(0.0, SequentialAction(
                robot.intake.extendoToPosAction(values.intake.extendoLimit),
                InstantAction{ robot.intake._extendoPower = 1.0},
                SleepAction(1.s),
                InstantAction{ robot.intake._extendoPower = 0.0},
                robot.intake.extendoToPosAction(values.intake.extendoInBot)
            ))
            //.waitSeconds(3.0)
            .setTangent(Math.toRadians(-135.0))
            .lineToXLinearHeading(basket.position.x, basket.heading)
            .afterTime(0.0, SequentialAction(
                robot.lift.liftToPosAction(values.lift.basketPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderBasketPos),
                robot.outtake.elbowToPosAction(values.outtake.elbowBasketPos),
                InstantAction{robot.outtake.clawPos = 0.0},
                InstantAction{robot.outtake.clawPos = 1.0},
                robot.outtake.elbowToPosAction(values.outtake.elbowWaitingPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderWaitingPos),
                robot.lift.liftToPosAction(values.lift.inRobot),
            ))
            //.waitSeconds(3.0)
            .setTangent(Math.toRadians(-135.0))
            .lineToXLinearHeading(mid_yellow.position.x, mid_yellow.heading)
            .afterTime(0.0, SequentialAction(
                robot.intake.extendoToPosAction(values.intake.extendoLimit),
                InstantAction{ robot.intake._extendoPower = 1.0},
                SleepAction(1.s),
                InstantAction{ robot.intake._extendoPower = 0.0},
                robot.intake.extendoToPosAction(values.intake.extendoInBot)
            ))
            //.waitSeconds(3.0)
            .setTangent(Math.toRadians(45.0))
            .lineToXLinearHeading(basket.position.x, basket.heading)
            .afterTime(0.0, SequentialAction(
                robot.lift.liftToPosAction(values.lift.basketPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderBasketPos),
                robot.outtake.elbowToPosAction(values.outtake.elbowBasketPos),
                InstantAction{robot.outtake.clawPos = 0.0},
                InstantAction{robot.outtake.clawPos = 1.0},
                robot.outtake.elbowToPosAction(values.outtake.elbowWaitingPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderWaitingPos),
                robot.lift.liftToPosAction(values.lift.inRobot),
            ))
            //.waitSeconds(3.0)
            .setTangent(Math.toRadians(180.0))
            .splineToLinearHeading(Pose(last_yellow.position, last_yellow.heading), -90.0.deg)
            .afterTime(0.0, SequentialAction(
                robot.intake.extendoToPosAction(values.intake.extendoLimit),
                InstantAction{ robot.intake._extendoPower = 1.0},
                SleepAction(1.s),
                InstantAction{ robot.intake._extendoPower = 0.0},
                robot.intake.extendoToPosAction(values.intake.extendoInBot)
            ))
            //.waitSeconds(3.0)
            .setTangent(Math.toRadians(90.0))
            .splineToLinearHeading(Pose(basket.position, basket.heading), 45.0.deg)
            .afterTime(0.0, SequentialAction(
                robot.lift.liftToPosAction(values.lift.basketPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderBasketPos),
                robot.outtake.elbowToPosAction(values.outtake.elbowBasketPos),
                InstantAction{robot.outtake.clawPos = 0.0},
                InstantAction{robot.outtake.clawPos = 1.0},
                robot.outtake.elbowToPosAction(values.outtake.elbowWaitingPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderWaitingPos),
                robot.lift.liftToPosAction(values.lift.inRobot),
            ))
            //.waitSeconds(3.0)
            .build()

        telemetry.addData("Config name", config.name)
        telemetry.addLine("READY!")
        telemetry.update()

        waitForStart()

        telemetry.update()

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

            robot.addTelemetry(telemetry)
            telemetry.update()
        }
    }
}