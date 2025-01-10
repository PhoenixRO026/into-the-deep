package org.firstinspires.ftc.teamcode.tele.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Lift

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
            timeKeep.resetDeltaTime()
            val leftStickY = -gamepad1.left_stick_y.toDouble()

            if (gamepad1.y) {
                lift.resetPosition()
            }

            lift.power = leftStickY

            telemetry.addData("Config name", config.name)
            telemetry.addData("lift pos", lift.position)
            telemetry.addData("lift power", lift.power)
            telemetry.addLine("Press Y to reset position")
            telemetry.addData("left stick Y", leftStickY)
            telemetry.update()
        }
    }
}