package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
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
class RedLeft : LinearOpMode() {
    private val startPose = Pose(-33.4.inch, -61.inch, 90.0.deg)
    private val pivot = Pose(-47.0.inch, -47.0.inch, 90.deg)
    private val  basket = Pose(-55.0.inch, -55.0.inch, 45.0.deg)
    private val  first_yellow = Pose(-47.0.inch, -47.0.inch, 90.0.deg)
    private val  mid_yellow = Pose(-47.0.inch, -47.0.inch, 120.0.deg)
    private val  last_yellow = Pose(-55.0.inch, -47.0.inch, 120.0.deg)

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


        val grabSample: SequentialAction = SequentialAction(
            robot.lift.liftToPosAction(values.lift.liftWaitingPos),
            SleepAction(1.s),
            InstantAction { robot.outtake.clawPos = 1.0 },
            ParallelAction(
                robot.outtake.elbowToPosAction(values.outtake.elbowIntakePos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderIntakePos),
                robot.outtake.extendoToPosAction(values.outtake.extendoIntakePos),
            ),
            SleepAction(1.s),
            robot.lift.liftToPosAction(values.lift.liftIntakePos),
            SleepAction(1.s),
            InstantAction { robot.outtake.clawPos = 0.0 },
            SleepAction(1.s),
        )
        val scoreBasket: SequentialAction = SequentialAction(
            robot.lift.liftToPosAction(values.lift.basketPos),
            SleepAction(1.s),
            ParallelAction(
                robot.outtake.extendoToPosAction(values.outtake.extendoRobotPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderBasketPos),
                robot.outtake.elbowToPosAction(values.outtake.elbowBasketPos)
            ),
            SleepAction(1.s),
            InstantAction { robot.outtake.clawPos = 1.0 },
            SleepAction(1.s),
            ParallelAction(
                robot.outtake.elbowToPosAction(values.outtake.elbowRobotPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderRobotPos)
            ),
            SleepAction(1.s),
            InstantAction { robot.outtake.clawPos = 0.0 },
            robot.lift.liftToPosAction(values.lift.inRobot),
            robot.outtake.extendoToPosAction(values.outtake.extendoRobotPos),
            SleepAction(2.s)
        )

        val getSample: SequentialAction = SequentialAction(
            robot.outtake.extendoToPosAction(values.outtake.extendoRobotPos),
            robot.outtake.elbowToPosAction(values.outtake.elbowRobotPos),
            robot.outtake.shoudlerToPosAction(values.outtake.shoulderRobotPos),
            ParallelAction(
                InstantAction { robot.intake.boxDown() },
                InstantAction { robot.intake.intakeDown() }
            ),
            //SleepAction(5.s),
            InstantAction { robot.intake.sweeperPower = 1.0 },
            robot.intake.extendoToPosAction(values.intake.extendoLim),
            robot.intake.extendoToPosAction(values.intake.extendoInBot),
            InstantAction { robot.intake.sweeperPower = 0.0 },
            ParallelAction(
                InstantAction { robot.intake.boxUp() },
                InstantAction { robot.intake.intakeUp() }
            ),
            SleepAction(2.s),
        )
        ///+2
        ///

        val action = mecanumDrive.actionBuilder(startPose.pose2d).ex()
            //.afterTime(0.0, SequentialAction(getSample, grabSample, scoreBasket))
            .setTangent(-90.0.deg + 180.0.deg)
            .splineTo(pivot.position, pivot.heading)
            .setTangent(-135.0.deg + 180.0.deg)
            .lineToXLinearHeading(basket.position.x, basket.heading)
            .waitSeconds(3.0)
            .setTangent(-90.0.deg + 180.0.deg)
            .splineToLinearHeading(
                Pose(first_yellow.position, first_yellow.heading),
                -90.0.deg + 180.0.deg
            )

            .waitSeconds(3.0)
            .setTangent(-135.0.deg + 180.0.deg)
            .lineToXLinearHeading(basket.position.x, basket.heading)

            .waitSeconds(3.0)
            .setTangent(-135.0.deg + 180.0.deg)
            .lineToXLinearHeading(mid_yellow.position.x, mid_yellow.heading)

            .waitSeconds(3.0)
            .setTangent(45.0.deg + 180.0.deg)
            .lineToXLinearHeading(basket.position.x, basket.heading)

            .waitSeconds(3.0)
            .setTangent(180.0.deg + 180.0.deg)
            .splineToLinearHeading(
                Pose(last_yellow.position, last_yellow.heading),
                -90.0.deg + 180.0.deg
            )

            .waitSeconds(3.0)
            .setTangent(90.0.deg + 180.0.deg)
            .splineToLinearHeading(Pose(basket.position, basket.heading), 45.0.deg + 180.0.deg)
            .waitSeconds(3.0)
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