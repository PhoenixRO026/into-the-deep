package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.SequentialAction
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader
import org.firstinspires.ftc.teamcode.robot.Intake
import org.firstinspires.ftc.teamcode.robot.Robot

abstract class SigmaDrive: LinearOpMode() {
    enum class Color {
        RED,
        BLUE
    }

    abstract val color: Color

    private var currentAction: Action? = null

    private val timeKeep = TimeKeep()
    private val movementTimekeep = TimeKeep()
    private val systemsTimekeep = TimeKeep()
    private val actionsTimekeep = TimeKeep()
    private val updateTimekeep = TimeKeep()
    private val telemetryTimkeep = TimeKeep()
    private val betweenLoopTimeKeep = TimeKeep()

    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        timeKeep.resetDeltaTime()
        val robot = Robot(hardwareMap)

        val rightBumper2Button = ButtonReader { gamepad2.right_bumper }

        telemetry.addLine("Ready")
        telemetry.update()

        while (opModeInInit()) {
            timeKeep.resetDeltaTime()
            sleep(50)
        }

        robot.initTeleop()

        while (isStarted && !isStopRequested) {
            betweenLoopTimeKeep.resetDeltaTime()
            rightBumper2Button.readValue()

            movementTimekeep.resetDeltaTime()
            movement(robot)
            movementTimekeep.resetDeltaTime()

            systemsTimekeep.resetDeltaTime()
            normalSystems(robot, rightBumper2Button)
            systemsTimekeep.resetDeltaTime()

            actionsTimekeep.resetDeltaTime()
            runActions()
            actionsTimekeep.resetDeltaTime()

            timeKeep.resetDeltaTime()
            updateTimekeep.resetDeltaTime()
            robot.update(timeKeep.deltaTime)
            updateTimekeep.resetDeltaTime()

            val telemetryMs = telemetryTimkeep.deltaTime
            telemetryTimkeep.resetDeltaTime()
            robot.addTelemetry(telemetry, timeKeep.deltaTime)
            telemetry.addData("current action", currentAction)
            telemetry.addData("movement ms", movementTimekeep.deltaTime.asMs)
            telemetry.addData("systems ms", systemsTimekeep.deltaTime.asMs)
            telemetry.addData("actions ms", actionsTimekeep.deltaTime.asMs)
            telemetry.addData("update ms", updateTimekeep.deltaTime.asMs)
            telemetry.addData("telemetry ms", telemetryMs)
            telemetry.addData("between loop ms", betweenLoopTimeKeep.deltaTime.asMs)
            telemetry.update()
            telemetryTimkeep.resetDeltaTime()
            betweenLoopTimeKeep.resetDeltaTime()
        }
    }

    private fun normalSystems(robot: Robot, actionButton: ButtonReader) {
        robot.outtake.clawPos = gamepad2.right_trigger.toDouble()

        robot.lift.power = -gamepad2.right_stick_y.toDouble()

        if (gamepad2.left_bumper) {
            robot.outtake.armToBasketInstant()
        }

        if (actionButton.wasJustPressed()) {
            currentAction = ParallelAction(
                SequentialAction(
                    robot.lift.liftToIntakeWaitingAction(),
                    InstantAction {
                        robot.intake.extendoTargetPosition = Intake.IntakeConfig.extendoIn
                        robot.intake.extendoMode = Intake.Mode.ACTION
                    },
                    robot.armAndLiftToIntake(),
                    robot.outtake.closeClawAction(),
                    InstantAction {
                        robot.intake.extendoMode = Intake.Mode.RAW_POWER
                        robot.intake.extendoPower = 0.0
                    }
                ),
                currentAction ?: Action { false }
            )
        }

        if (gamepad2.touchpad) {
            currentAction = robot.turnOffAction()
        }

        if (gamepad2.dpad_up) {
            robot.outtake.extendoToMaxInstant()
        }

        if (gamepad2.dpad_down) {
            robot.outtake.extendoInInstant()
        }

        if (gamepad2.dpad_right) {
            robot.outtake.wristToMidInstant()
        }

        if (gamepad2.dpad_left) {
            robot.outtake.wristToUpsideDownInstant()
        }

        if (gamepad2.a) {
            robot.outtake.armToSpecimenInstant()
        }

        if (gamepad2.b) {
            robot.outtake.armToNeutralInstant()
        }

        if (gamepad2.x) {
            robot.robotToBarInstant()
        }

        if (gamepad2.y) {
            robot.outtake.armToOldBarInstant()
        }

        if (gamepad1.a) {
            robot.intake.resetExtendoPosition()
        }

        if (gamepad1.dpad_up) {
            robot.intake.tiltUpInstant()
        }

        if (gamepad1.dpad_down) {
            robot.intake.tiltDownInstant()
        }

        if (gamepad1.x) {
            currentAction = ParallelAction(
                SequentialAction(
                    ParallelAction(
                        SequentialAction(
                            robot.lift.liftToIntakeWaitingAction(),
                            robot.outtake.armToIntakeAction()
                        ),
                        robot.intake.takeSampleSequenceAction(Intake.SensorColor.YELLOW),
                    ),
                    robot.intake.takeOutSample()
                ),
                currentAction ?: Action { false }
            )
        }

        if (gamepad1.b) {
            currentAction = ParallelAction(
                robot.intake.takeSampleSequenceAction(when (color) {
                    Color.RED -> Intake.SensorColor.RED
                    Color.BLUE -> Intake.SensorColor.BLUE
                }),
                currentAction ?: Action { false }
            )
        }

        if (gamepad1.left_bumper) {
            robot.intake.extendoPower = -1.0
        } else if (gamepad1.right_bumper) {
            robot.intake.extendoPower = 1.0
        } else {
            robot.intake.extendoPower = 0.0
        }

        if (gamepad2.left_trigger != 0f) {
            robot.intake.sweeperPower = gamepad2.left_trigger.toDouble()
        } else {
            robot.intake.sweeperPower = -gamepad1.left_trigger.toDouble()
        }

    }

    private fun movement(robot: Robot) {
        if (gamepad1.y)
            robot.drive.resetFieldCentric()

        robot.drive.isSlowMode = gamepad1.right_trigger >= 0.2

        robot.drive.driveFieldCentric(
            -gamepad1.left_stick_y.toDouble(),
            -gamepad1.left_stick_x.toDouble(),
            -gamepad1.right_stick_x.toDouble()
        )
    }

    private fun runActions() {
        currentAction?.let {
            if (!it.run(TelemetryPacket())) {
                currentAction = null
            }
        }
    }
}