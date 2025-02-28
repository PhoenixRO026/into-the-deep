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
class RedRightV4 : LinearOpMode() {
    private val startPose = Pose(20.cm, -61.5.inch, 90.deg)
    private val firstSpecimenBeforePos = Pose(4.inch, -40.inch, 90.deg)
    private val firstSpecimenPos = Pose(-1.5.inch, -30.5.inch, 90.deg)
    private val secondSpecimenPos = Pose(0.inch, -30.5.inch, 90.deg)
    private val thirdSpecimenPos = Pose(1.5.inch, -30.5.inch, 90.deg)
    private val forthSpecimenPos = Pose(2.5.inch, -30.5.inch, 90.deg)
    private val red1Pos = Distance2d(48.inch, -25.5.inch)
    private val red2Pos = Distance2d(58.5.inch, -25.5.inch)
    private val red3Pos = Distance2d(68.5.inch, -25.5.inch)
    private val zonePos = Distance2d(45.1.inch, -67.2.inch)
    private val zonePoze3 = Distance2d(50.inch, -67.2.inch)
    private val firstSamplePos = Distance2d(26.5.inch, -41.inch).headingTowards(red1Pos)
    private val secondSamplePos = Distance2d(33.inch, -38.5.inch).headingTowards(red2Pos)
    private val thirdSamplePos = Distance2d(41.inch, -37.inch).headingTowards(red3Pos)
    private val firstKickPos = Distance2d(30.inch, -50.inch).headingTowards(zonePos)
    private val secondKickPos = Distance2d(34.inch, -50.inch).headingTowards(zonePos)
    private val thirdKickPos = Distance2d(38.inch, -50.inch).headingTowards(zonePos)
    private val takeSpecimenPos = Pose(40.inch, -54.inch, 90.deg)

    override fun runOpMode() {
        initMessage()

        val timeKeep = TimeKeep()
        val robot = Robot(hardwareMap, startPose)
        val drive = robot.drive
        val intake = robot.intake
        val lift = robot.lift
        val outtake = robot.outtake
        robot.resetLiftEncoder()
        robot.initAuto()

        fun firstSampleCycle() = SequentialAction(
            ParallelAction(
                lift.liftToIntakeWaitingAction(),
                outtake.extendoToNeutralAction(),
                robot.armAndLiftToNeutral().delayedBy(1.s),
                intake.extendoToLeftRedSampleAction().delayedBy(1.s),
                drive.actionBuilder(firstSpecimenPos, 1.5.s)
                    .setTangent(-90.deg)
                    .splineToLinearHeading(firstSamplePos, 0.deg)
                    .build().delayedBy(0.1.s)
            ),
            intake.takeSample(Intake.SensorColor.RED),
            ParallelAction(
                intake.tiltUpAction(),
                drive.actionBuilder(firstSamplePos)
                    .turnTo(firstSamplePos.position.headingTowards(zonePos).heading)
                    .build()
            ),
            intake.kickSample()
        )

        fun secondSampleCycle() = SequentialAction(
            ParallelAction(
                intake.extendoToMiddleRedSampleAction(),
                drive.actionBuilder(firstSamplePos, 1.s)
                    .strafeToLinearHeading(secondSamplePos)
                    .build()
            ),
            intake.takeSample(Intake.SensorColor.RED),
            ParallelAction(
                intake.tiltUpAction(),
                drive.actionBuilder(secondSamplePos)
                    .turnTo(secondSamplePos.position.headingTowards(zonePos).heading)
                    .build()
            ),
            intake.kickSample()
        )

        fun thirdSampleCycle() = SequentialAction(
            ParallelAction(
                intake.extendoToRightRedSampleAction(),
                drive.actionBuilder(secondSamplePos, 1.s)
                    .strafeToLinearHeading(thirdSamplePos)
                    .build()
            ),
            intake.takeSample(Intake.SensorColor.RED),
            ParallelAction(
                intake.tiltUpAction(),
                drive.actionBuilder(thirdSamplePos)
                    .turnTo(thirdSamplePos.position.headingTowards(zonePoze3).heading)
                    .build()
            ),
            intake.kickSample()
        )

        fun firstSpecimenCycle() = SequentialAction(
            ParallelAction(
                intake.extendoInAction(),
                outtake.openClawAction(),
                robot.armAndLiftToSpecimen(),
                drive.actionBuilder(thirdSamplePos)
                    .setTangent(-90.deg)
                    .splineToSplineHeading(takeSpecimenPos + 10.cm.y, -90.deg)
                    .lineToY(takeSpecimenPos.position.y)
                    .build()
            ),
            outtake.closeClawAction(),
            ParallelAction(
                lift.liftToBarAction(),
                ParallelAction(
                    outtake.armToBarAction(),
                    outtake.wristToUpsideDownAction(),
                    drive.actionBuilder(takeSpecimenPos)
                        .setTangent(165.deg)
                        .splineToLinearHeading(secondSpecimenPos, 90.deg)
                        //.splineToLinearHeading(secondSpecimenPos, 90.deg)
                        .build()
                ).delayedBy(0.25.s), //SO THE LIFT HAS TIME TO RISE
            ),
            outtake.openClawAction(),
        )

        fun secondSpecimenCycle() = SequentialAction(
            ParallelAction(
                SequentialAction(
                    lift.liftToIntakeWaitingAction(),
                    robot.armAndLiftToSpecimen(),
                ),
                drive.actionBuilder(firstSpecimenPos)
                    .setTangent(-90.deg)
                    .splineToLinearHeading(takeSpecimenPos, -90.deg)
                    //.splineToLinearHeading(takeSpecimenPos, -90.deg)
                    .build().delayedBy(0.25.s) //SO THE LIFT HAS TIME TO DESCEND
            ),
            outtake.closeClawAction(),
            ParallelAction(
                lift.liftToBarAction(),
                ParallelAction(
                    robot.armAndLiftToBar(),
                    outtake.wristToUpsideDownAction(),
                    drive.actionBuilder(takeSpecimenPos)
                        .setTangent(165.deg)
                        .splineToLinearHeading(thirdSpecimenPos, 90.deg)
                        //.splineToSplineHeading(thirdSpecimenPos, 90.deg)
                        .build()
                ).delayedBy(0.25.s), //SO THE LIFT HAS TIME TO RISE
            ),
            outtake.openClawAction(),
        )

        fun thirdSpecimenCycle() = SequentialAction(
            ParallelAction(
                SequentialAction(
                    lift.liftToIntakeWaitingAction(),
                    robot.armAndLiftToSpecimen(),
                ),
                drive.actionBuilder(thirdSpecimenPos)
                    .setTangent(-90.deg)
                    .splineToLinearHeading(takeSpecimenPos, -90.deg)
                    .build().delayedBy(0.25.s) //SO THE LIFT HAS TIME TO DESCEND
            ),
            outtake.closeClawAction(),
            ParallelAction(
                lift.liftToBarAction(),
                ParallelAction(
                    robot.armAndLiftToBar(),
                    outtake.wristToUpsideDownAction(),
                    drive.actionBuilder(takeSpecimenPos)
                        .setTangent(165.deg)
                        .splineToLinearHeading(forthSpecimenPos, 90.deg)
                        .build()
                ).delayedBy(0.25.s), //SO THE LIFT HAS TIME TO RISE
            ),
            outtake.openClawAction(),
            lift.liftToIntakeWaitingAction()
        )

        val action = SequentialAction(
            ParallelAction(
                robot.armAndLiftToBar(),
                drive.actionBuilder(startPose)
                    .strafeToLinearHeading(firstSpecimenPos)
                    .build()
            ),
            outtake.openClawAction(),
            firstSampleCycle(),
            secondSampleCycle(),
            thirdSampleCycle(),
            firstSpecimenCycle(),
            secondSpecimenCycle(),
            thirdSpecimenCycle()
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