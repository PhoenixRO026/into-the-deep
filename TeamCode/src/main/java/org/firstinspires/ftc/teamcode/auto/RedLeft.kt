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
import com.lib.units.Distance2d
import com.lib.units.Pose
import com.lib.units.SleepAction
import com.lib.units.deg
import com.lib.units.inch
import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.AutoRobot
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigTransilvaniaCollege
import org.firstinspires.ftc.teamcode.tele.values.robotValuesTransilvaniaCollege

@Autonomous
class RedLeft : LinearOpMode() {
    val startPose = Pose(-37.5.inch, -61.inch, 90.0.deg)
    val basket = Pose(-52.0.inch, -53.0.inch, 45.0.deg)
    val first_yellow = Distance2d(-49.inch, -47.0.inch).headingTowards(Distance2d(-58.inch, -26.0.inch))
    val mid_yellow = Distance2d(-59.0.inch, -47.0.inch).headingTowards(Distance2d(-58.inch, -26.0.inch))
    val last_yellow = Distance2d(-57.0.inch, -43.0.inch).headingTowards(Distance2d(-69.inch, -26.0.inch))

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

        fun initRobot() = SequentialAction(
            InstantAction { robot.outtake.clawPos = 0.0},
            robot.outtake.shoudlerToPosAction(values.outtake.shoulderRobotPos),
            robot.outtake.elbowToPosAction(values.outtake.elbowRobotPos),
            InstantAction{ robot.intake.intakeUp() },
        )

        fun grabSample() = SequentialAction(
            //robot.lift.liftToPosAction(values.lift.liftWaitingPos),
            InstantAction { robot.outtake.clawPos = 1.0 },
            robot.outtake.extendoToPosAction(values.outtake.extendoIntakePos),
            ParallelAction(
                robot.outtake.elbowToPosAction(values.outtake.elbowIntakePos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderIntakePos),
            ),
            SleepAction(0.2.s),
            robot.lift.liftToPosAction(values.lift.liftIntakePos),
            SleepAction(0.4.s),
            InstantAction { robot.outtake.clawPos = 0.0 },
            SleepAction(0.1.s)
        )
        fun scoreBasket() = SequentialAction(
            robot.lift.liftToPosAction(values.lift.basketPos),
            ParallelAction(
                robot.outtake.extendoToPosAction(values.outtake.extendoRobotPos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderBasketPos),
                robot.outtake.elbowToPosAction(values.outtake.elbowBasketPos)
            ),
            SleepAction(0.4.s),
            InstantAction { robot.outtake.clawPos = 1.0 },
            SleepAction(0.4.s),
            InstantAction { robot.outtake.clawPos = 0.0 },
            ParallelAction(
                robot.outtake.elbowToPosAction(values.outtake.elbowIntakePos),
                robot.outtake.shoudlerToPosAction(values.outtake.shoulderIntakePos),
                robot.outtake.extendoToPosAction(values.outtake.extendoIntakePos)
            ),
            SleepAction(0.4.s),
            ParallelAction(
                robot.lift.liftToPosAction(values.lift.liftWaitingPos),
                robot.outtake.extendoToPosAction(values.outtake.extendoIntakePos)
            )
            //robot.lift.liftToPosAction(values.lift.inRobot),
            //robot.outtake.extendoToPosAction(values.outtake.extendoRobotPos),
        )

        fun getSample() = SequentialAction(
            robot.outtake.extendoToPosAction(values.outtake.extendoRobotPos),
            InstantAction { robot.intake.intakeDown()},
            //SleepAction(5.s),
            InstantAction { robot.intake.snatchSpecimen(hsvValues) },
            SleepAction(0.5.s),
            robot.intake.extendoToPosAction(values.intake.extendoInBot),
            InstantAction { robot.intake.intakeUp() },
            SleepAction(0.3.s),
            InstantAction { robot.intake.kickSample(hsvValues)},
            SleepAction(0.3.s),
            InstantAction { robot.intake.sweeperPower = 0.0 }
        )
        ///+2
        ///

        val preloadAction = mecanumDrive.actionBuilder(startPose.pose2d).ex()
            .setTangent(90.0.deg)
            .strafeToLinearHeading(basket.position, basket.heading)

            .afterTime(0.0,scoreBasket())
            .afterTime(0.0,InstantAction{robot.intake.intakeDown()})
            .waitSeconds(4.0.s)

            .setTangent(90.0.deg)
            .strafeToLinearHeading(first_yellow.position, first_yellow.heading)

            .afterTime(0.0, getSample())
            .build()

        val firstYellowScore = mecanumDrive.actionBuilder(first_yellow.pose2d).ex()
            .waitSeconds(2.5.s)
            .setTangent(-90.0.deg)
            .strafeToLinearHeading(basket.position, basket.heading)

            .afterTime(0.0,SequentialAction(grabSample(), scoreBasket()))

            .waitSeconds(4.0.s)
            .setTangent(45.0.deg)
            .strafeToLinearHeading(mid_yellow.position, mid_yellow.heading)

            .afterTime(0.0, getSample())
            .build()

        val midYellowScore = mecanumDrive.actionBuilder(mid_yellow.pose2d).ex()
            .waitSeconds(2.5.s)
            .setTangent(-90.0.deg)
            .strafeToLinearHeading(basket.position, basket.heading)

            .afterTime(0.0,SequentialAction(grabSample(),scoreBasket()))
            .waitSeconds(4.0.s)
            .setTangent(0.0.deg)
            .strafeToLinearHeading(last_yellow.position, last_yellow.heading)

            .afterTime(0.0, getSample())
            .build()

        val lastYellowScore = mecanumDrive.actionBuilder(last_yellow.pose2d).ex()
            .afterTime(0.0, getSample())
            .waitSeconds(2.5.s)
            .setTangent(-90.0.deg)
            .strafeToLinearHeading(basket.position, basket.heading)

            .afterTime(0.0,SequentialAction(grabSample(),scoreBasket()))
            .build()

        val action = SequentialAction(
            preloadAction,
            firstYellowScore,
            midYellowScore,
            lastYellowScore
        )

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

            robot.addTelemetry(telemetry)
            telemetry.addData("red side", robot.intake.shouldStopIntake("RED", hsvValues[0], false))
            telemetry.addData("blue side", robot.intake.shouldStopIntake("BLUE", hsvValues[0], false))
            telemetry.addData("Red", robot.intake.intakeColorSensor.red())
            telemetry.addData("Green", robot.intake.intakeColorSensor.green())
            telemetry.addData("Blue", robot.intake.intakeColorSensor.blue())
            telemetry.update()
        }
    }
}