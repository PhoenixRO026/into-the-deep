package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Drive
import org.firstinspires.ftc.teamcode.teleop.config.robotConfigGherla
import kotlin.math.PI

@TeleOp
class DriveOnlyTele : LinearOpMode() {
    private val timeKeep = TimeKeep()
    private val drive = Drive(robotConfigGherla.drive)

    override fun runOpMode() {
        drive.init(hardwareMap)

        waitForStart()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()

            drive.driveFieldCentric(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble()
            )

            telemetry.addData("robot yaw degrees", drive.yaw / PI * 180)
            telemetry.addData("delta time ms", timeKeep.deltaTime)
            telemetry.addData("fps", 1000.0 / timeKeep.deltaTime)
            telemetry.update()
        }
    }
}