package org.firstinspires.ftc.teamcode.tele

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigWonder
import org.firstinspires.ftc.teamcode.tele.values.robotValuesWonder

@TeleOp
class CraneTele : LinearOpMode() {
    override fun runOpMode() {
        val config = robotHardwareConfigWonder
        val values = robotValuesWonder

        telemetry.addData("Config name", config.name)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        val timeKeep = TimeKeep()
        val robot = Robot(hardwareMap, config, values, timeKeep)

        telemetry.addData("Config name", config.name)
        telemetry.addLine("READY!")
        telemetry.update()

        waitForStart()

        while (isStarted && !isStopRequested) {
            timeKeep.resetDeltaTime()
            val pad1LeftStickY = -gamepad1.left_stick_y.toDouble()
            val pad1LeftStickX = gamepad1.right_stick_x.toDouble()
            val pad1RightStickX = gamepad1.right_stick_x.toDouble()
            val pad1Triggers = gamepad1.right_trigger.toDouble() - gamepad1.left_trigger.toDouble()

            val pad2LeftStickY = -gamepad2.left_stick_y.toDouble()
            val pad2LeftStickX = gamepad2.left_stick_x.toDouble()
            val pad2RightStickY = -gamepad2.right_stick_y.toDouble()
            val pad2RightStickX = gamepad2.right_stick_x.toDouble()

            //DRIVE
            if (gamepad1.y)
                robot.drive.resetFieldCentric()

            robot.drive.isSlowMode = gamepad1.left_bumper

            robot.drive.driveFieldCentric(
                pad1LeftStickY,
                -pad1LeftStickX,
                -pad1RightStickX
            )

            //OUTTAKE
            robot.outtake.extendoPower = pad2RightStickX
            robot.outtake.shoulderSpeed = pad2LeftStickY
            robot.outtake.elbowSpeed = pad2LeftStickX
            robot.outtake.wristPos = when {
                gamepad2.left_bumper -> 0.0
                gamepad2.right_bumper -> 1.0
                else -> 0.5
            }
            robot.outtake.clawPos = gamepad2.right_trigger.toDouble()

            //INTAKE
            robot.intake.extendoPower = pad1Triggers
            robot.intake.sweeperPower = if (gamepad2.x) -1.0 else gamepad2.left_trigger.toDouble()
            robot.intake.boxTiltSpeed = when {
                gamepad2.dpad_up -> 1.0
                gamepad2.dpad_up -> -1.0
                else -> 0.0
            }
            robot.intake.intakeTiltSpeed = when {
                gamepad2.dpad_right -> 1.0
                gamepad2.dpad_left -> -1.0
                else -> 0.0
            }

            //LIFT
            robot.lift.power = pad2RightStickY

            robot.update()

            robot.addTelemetry(telemetry)
            telemetry.update()
        }
    }
}