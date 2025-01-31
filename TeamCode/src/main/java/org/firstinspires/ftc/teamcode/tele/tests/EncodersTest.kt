package org.firstinspires.ftc.teamcode.tele.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.ftc.DriveViewFactory
import com.acmerobotics.roadrunner.ftc.MidpointTimer
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

class EncodersTest(val dvf: DriveViewFactory): LinearOpMode() {
    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        val view = dvf.make(hardwareMap)
        require(view.perpEncs.isNotEmpty()) {
            "Only run this op mode if you're using dead wheels."
        }

        view.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL)

        waitForStart()

        val t = MidpointTimer()
        while (opModeIsActive()) {
            val encTimes = view.resetAndBulkRead(t)

            telemetry.addData("par enc pos", view.parEncs[0].getPositionAndVelocity().position)
            telemetry.addData("par enc vel", view.parEncs[0].getPositionAndVelocity().velocity)
            telemetry.addData("perp enc pos", view.perpEncs[0].getPositionAndVelocity().position)
            telemetry.addData("perp enc vel", view.perpEncs[0].getPositionAndVelocity().velocity)
            telemetry.update()
        }
    }
}