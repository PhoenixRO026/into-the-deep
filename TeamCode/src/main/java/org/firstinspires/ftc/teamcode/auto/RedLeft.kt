package org.firstinspires.ftc.teamcode.auto

import android.app.Activity
import android.graphics.Color
import android.view.View
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
    private val  first_yellow = Pose(-49.0.inch, -47.0.inch, 90.0.deg)
    private val  mid_yellow = Pose(-47.0.inch, -47.0.inch, 120.0.deg)
    private val  last_yellow = Pose(-55.0.inch, -47.0.inch, 120.0.deg)

    override fun runOpMode() {
        val config = robotHardwareConfigTransilvaniaCollege
        val values = robotValuesTransilvaniaCollege

        val hsvValues = floatArrayOf(0f, 0f, 0f)
        val valuesColor = hsvValues
        val SCALE_FACTOR = 255.0

        val dash = FtcDashboard.getInstance()
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        telemetry.addData("Config name", config.name)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        val relativeLayoutId = hardwareMap.appContext.resources.getIdentifier("RelativeLayout", "id", hardwareMap.appContext.packageName)
        val relativeLayout = (hardwareMap.appContext as Activity).findViewById<View>(relativeLayoutId)


        val timeKeep = TimeKeep()
        val robot = AutoRobot(hardwareMap, config, values, timeKeep, startPose, telemetry)
        val mecanumDrive = robot.roadRunnerDrive
        val hue = hsvValues[0].toInt()


        robot.outtake.extendoCurrentPos = values.outtake.extendoRobotPos
        robot.intake.intakeUp()
        robot.outtake.shoulderCurrentPos = values.outtake.shoulderRobotPos
        robot.outtake.elbowCurrentPos = values.outtake.elbowRobotPos
        robot.outtake.clawPos = 0.0

        val initRobot: SequentialAction = SequentialAction(
            InstantAction { robot.intake.intakeUp()},
            robot.intake.extendoToPosAction(values.intake.extendoInBot),

            robot.outtake.shoudlerToPosAction(values.outtake.shoulderRobotPos),
            robot.outtake.elbowToPosAction(values.outtake.elbowRobotPos),

            robot.lift.liftToPosAction(values.lift.inRobot),
            robot.outtake.extendoToPosAction(values.outtake.extendoRobotPos),
            InstantAction { robot.outtake.clawPos = 0.0},
        )

        val grabSample: SequentialAction = SequentialAction(
            robot.lift.liftToPosAction(values.lift.liftWaitingPos),
            InstantAction { robot.outtake.clawPos = 1.0 },
            ParallelAction(
                robot.outtake.elbowToPosAction(values.outtake.elbowIntakePos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderIntakePos),
                 robot.outtake.extendoToPosAction(values.outtake.extendoIntakePos),
            ),
            SleepAction(0.2.s),
            robot.lift.liftToPosAction(values.lift.liftIntakePos),
            SleepAction(1.5.s),
            InstantAction { robot.outtake.clawPos = 0.0 },
            SleepAction(0.5.s)
        )
        val scoreBasket: SequentialAction = SequentialAction(
            ParallelAction(robot.outtake.elbowToPosAction(values.outtake.elbowRobotPos),
            robot.outtake.shoudlerToPosAction(values.outtake.shoulderRobotPos),
            InstantAction{robot.outtake.clawPos = 0.0},),
            robot.lift.liftToPosAction(values.lift.basketPos),
            ParallelAction(
                robot.outtake.extendoToPosAction(values.outtake.extendoRobotPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderBasketPos),
                robot.outtake.elbowToPosAction(values.outtake.elbowBasketPos)
            ),
            SleepAction(1.s),
            InstantAction { robot.outtake.clawPos = 1.0 },
            SleepAction(0.5.s),
            ParallelAction(
                robot.outtake.elbowToPosAction(values.outtake.elbowRobotPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderRobotPos)
            ),
            SleepAction(1.5.s),
            InstantAction { robot.outtake.clawPos = 0.0 },
            robot.lift.liftToPosAction(values.lift.inRobot),
            robot.outtake.extendoToPosAction(values.outtake.extendoRobotPos),
        )

        val getSample: SequentialAction = SequentialAction(
            robot.outtake.extendoToPosAction(values.outtake.extendoRobotPos),
            InstantAction { robot.intake.intakeDown()},
            robot.outtake.elbowToPosAction(values.outtake.elbowRobotPos),
            robot.outtake.shoudlerToPosAction(values.outtake.shoulderRobotPos),
            //SleepAction(5.s),
            InstantAction { robot.intake.snatchSpecimen(hsvValues) },
            SleepAction(1.s),
            robot.intake.extendoToPosAction(values.intake.extendoInBot),
            InstantAction { robot.intake.intakeUp() },
            SleepAction(1.s),
            InstantAction { robot.intake.kickSample(hsvValues)},
            SleepAction(1.s),
            InstantAction { robot.intake.sweeperPower = 0.0 }
        )
        ///+2
        ///

        val action = mecanumDrive.actionBuilder(startPose.pose2d).ex()
            .afterTime(0.0,initRobot)
            .waitSeconds(5.s)
            .setTangent(-90.0.deg + 180.0.deg)
            .splineTo(pivot.position, pivot.heading)
            .setTangent(-135.0.deg + 180.0.deg)
            .lineToXLinearHeading(basket.position.x, basket.heading)

            .afterTime(0.0,SequentialAction(scoreBasket))///////////////////////// preload

            .waitSeconds(5.s)
            .afterTime(0.1,ParallelAction(
            robot.outtake.elbowToPosAction(values.outtake.elbowRobotPos),
            robot.outtake.shoudlerToPosAction(values.outtake.shoulderRobotPos)
            ))
            .afterTime(0.1,InstantAction{robot.intake.intakeDown()})

            .setTangent(-90.0.deg + 180.0.deg)
            .splineToLinearHeading(Pose(first_yellow.position, first_yellow.heading), -90.0.deg + 180.0.deg)

            .afterTime(0.0,SequentialAction(getSample))///////////////////////get first yellow
            .waitSeconds(6.s)
            .setTangent(-135.0.deg + 180.0.deg)
            .lineToXLinearHeading(basket.position.x, basket.heading)

            .afterTime(0.0,SequentialAction(grabSample,scoreBasket))//////////////score first yellow
            .waitSeconds(10.s)

            .setTangent(-135.0.deg + 180.0.deg)
            .lineToXLinearHeading(mid_yellow.position.x, mid_yellow.heading)
            .afterTime(0.0,SequentialAction(getSample))///////////////////////get second yellow

            .waitSeconds(6.s)
            .setTangent(45.0.deg + 180.0.deg)
            .lineToXLinearHeading(basket.position.x, basket.heading)

            .afterTime(0.0,SequentialAction(grabSample,scoreBasket))////////////score second yellow
            .waitSeconds(10.s)
            .setTangent(180.0.deg + 180.0.deg)
            .splineToLinearHeading(
                Pose(last_yellow.position, last_yellow.heading),
                -90.0.deg + 180.0.deg
            )

            .afterTime(0.0,SequentialAction(getSample))///////////////get last yellow
            .waitSeconds(6.s)
            .setTangent(90.0.deg + 180.0.deg)
            .splineToLinearHeading(Pose(basket.position, basket.heading), 45.0.deg + 180.0.deg)
            .afterTime(0.0,SequentialAction(grabSample,scoreBasket))//////////////////score last yellow
            .waitSeconds(6.s)
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

            Color.RGBToHSV(
                (robot.intake.intakeColorSensor.red() * SCALE_FACTOR).toInt(),
                (robot.intake.intakeColorSensor.green() * SCALE_FACTOR).toInt(),
                (robot.intake.intakeColorSensor.blue() * SCALE_FACTOR).toInt(),
                hsvValues
            )

            val packet = TelemetryPacket()
            packet.fieldOverlay().operations.addAll(canvas.operations)

            running = action.run(packet)
            dash.sendTelemetryPacket(packet)

            robot.update()

            //robot.addTelemetry(telemetry)
            telemetry.addData("red side", robot.intake.shouldStopIntake("RED", hsvValues[0], false))
            telemetry.addData("blue side", robot.intake.shouldStopIntake("BLUE", hsvValues[0], false))
            telemetry.addData("Red", robot.intake.intakeColorSensor.red())
            telemetry.addData("Green", robot.intake.intakeColorSensor.green())
            telemetry.addData("Blue", robot.intake.intakeColorSensor.blue())
            telemetry.update()
        }
    }
}