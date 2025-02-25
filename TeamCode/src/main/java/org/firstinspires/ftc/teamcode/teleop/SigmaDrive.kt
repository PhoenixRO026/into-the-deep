package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader
import org.firstinspires.ftc.teamcode.robot.Robot

@TeleOp
class SigmaDrive: LinearOpMode() {
    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        val timeKeep = TimeKeep()
        val robot = Robot(hardwareMap)

        var currentAction: Action? = null

        val a2Button = ButtonReader { gamepad2.a }
        val b2Button = ButtonReader { gamepad2.b }
        val x2Button = ButtonReader { gamepad2.x }
        val y2Button = ButtonReader { gamepad2.y }
        val buttons = listOf(a2Button, b2Button, x2Button, y2Button)

        telemetry.addLine("Ready")
        telemetry.update()

        while (opModeInInit()) {
            timeKeep.resetDeltaTime()
            sleep(50)
        }

        robot.initTeleop()

        while (isStarted && !isStopRequested) {
            buttons.forEach { it.readValue() }

            if (gamepad1.y)
                robot.drive.resetFieldCentric()

            robot.drive.isSlowMode = gamepad1.right_trigger >= 0.2

            robot.drive.driveFieldCentric(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble()
            )

            robot.outtake.clawPos = gamepad2.right_trigger.toDouble()

            if (a2Button.wasJustPressed()) {
                currentAction = robot.outtake.armToNeutralAction()
            }

            if (b2Button.wasJustPressed()) {
                currentAction = robot.armAndLiftToIntake()
            }

            if (x2Button.wasJustPressed()) {
                currentAction = robot.armAndLiftToIntakeWaiting()
            }

            if (y2Button.wasJustPressed()) {
                currentAction = robot.sampleToBasket()
            }

            currentAction?.let {
                if (!it.run(TelemetryPacket())) {
                    currentAction = null
                }
            }

            timeKeep.resetDeltaTime()
            robot.update(timeKeep.deltaTime)

            robot.addTelemetry(telemetry, timeKeep.deltaTime)
            telemetry.addData("current action", currentAction)
            telemetry.update()
        }
    }
}