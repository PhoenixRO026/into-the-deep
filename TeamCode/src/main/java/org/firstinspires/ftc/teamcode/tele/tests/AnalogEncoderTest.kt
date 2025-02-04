package org.firstinspires.ftc.teamcode.tele.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.AnalogEncoderServo
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigWonder

class AnalogEncoderTest : LinearOpMode() {
    override fun runOpMode() {
        val config = robotHardwareConfigWonder.outtake.servoExtendo
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        val timeKeep = TimeKeep()
        //val servo = AnalogEncoderServo(hardwareMap, config.encoderDeviceName, config.servoDeviceName)

        waitForStart()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()
            //servo.update()

            //servo.power = -gamepad1.left_stick_y.toDouble()

            //telemetry.addData("servo pos", servo.rotations.asRev)
            //telemetry.addData("voltage", servo.voltage)
            //telemetry.addData("change count", servo.changeCount)
            telemetry.addData("fps", 1.s / timeKeep.deltaTime)
            telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
            telemetry.update()
        }
    }
}