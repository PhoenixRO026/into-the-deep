package org.firstinspires.ftc.teamcode.tele.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Lift
import org.firstinspires.ftc.teamcode.tele.tests.LiftTuning.LiftTuningConfig

class LiftOnly : LinearOpMode() {
    override fun runOpMode() {
        val config = testsRobotHardwareConfig
        val values = testsRobotValues

        val timeKeep = TimeKeep()

        val lift = Lift(hardwareMap, config.lift, values.lift, timeKeep)

        telemetry.addData("Config name", config.name)
        telemetry.update()

        waitForStart()

        while (opModeIsActive()) {
            telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
            timeKeep.resetDeltaTime()
            val leftStickY = -gamepad1.left_stick_y.toDouble()

            if (gamepad1.y) {
                lift.resetPosition()
            }

            if (gamepad1.x) {
                lift.targetPosition = 200
            }else if (gamepad1.b) {
                lift.targetPosition = 300
            }else if (gamepad1.a) {
                lift.targetPosition = 400
            }

            lift.power = leftStickY

            fun dirToString(dir: DcMotorSimple.Direction) = when (dir) {
                DcMotorSimple.Direction.FORWARD -> "FORWARD"
                DcMotorSimple.Direction.REVERSE -> "REVERSE"
            }

            telemetry.addData("Config name", config.name)
            telemetry.addData("lift pos", lift.position)
            telemetry.addData("lift target pos", lift.targetPosition)
            telemetry.addData("lift power", lift.power)
            telemetry.addData("left lif dir", dirToString(lift.motorLiftLeft.direction))
            telemetry.addData("right lif dir", dirToString(lift.motorLiftRight.direction))
            telemetry.addLine("Press Y to reset position")
            telemetry.addData("left stick Y", leftStickY)
            telemetry.update()
        }
    }
}