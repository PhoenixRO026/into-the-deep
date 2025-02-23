package org.firstinspires.ftc.teamcode.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.robot.Robot

class OuttakeActionsTest: LinearOpMode() {
    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        val outtake = Robot(hardwareMap).outtake

        waitForStart()

        outtake.initTeleop()

        var currentAction: Action? = null

        while (opModeIsActive()) {
            when {
                gamepad1.a -> {
                    currentAction = outtake.armToIntakeAction()
                    telemetry.addLine("Current action: Intake")
                }
                gamepad1.x -> {
                    currentAction = outtake.armToNeutralAction()
                    telemetry.addLine("Current action: Neutral")
                }
                gamepad1.y -> {
                    currentAction = outtake.armToBasketAction()
                    telemetry.addLine("Current action: Basket")
                }
            }

            if (currentAction == null) {
                telemetry.addLine("Current action: None")
            }

            if (currentAction?.run(TelemetryPacket()) != true) {
                currentAction = null
            }

            telemetry.update()
        }
    }
}