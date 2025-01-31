package org.firstinspires.ftc.teamcode.tele.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.ftc.DriveViewFactory
import com.acmerobotics.roadrunner.ftc.Encoder
import com.acmerobotics.roadrunner.ftc.MidpointTimer
import com.acmerobotics.roadrunner.ftc.TuningFiles
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import kotlin.math.min

private class MutableSignal(
    val times: MutableList<Double> = mutableListOf(),
    val values: MutableList<Double> = mutableListOf()
)

class ForwardRampLoggerEx(val dvf: DriveViewFactory) : LinearOpMode() {
    companion object {
        @JvmField
        var POWER_PER_SEC = 0.1
        @JvmField
        var POWER_MAX = 0.9
    }

    fun power(seconds: Double) = min(POWER_PER_SEC * seconds, POWER_MAX)

    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        val view = dvf.make(hardwareMap)
        require(view.perpEncs.isNotEmpty()) {
            "Only run this op mode if you're using dead wheels."
        }

        //view.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL)

        val data = object {
            val type = view.type
            val powers = view.motors.map { MutableSignal() }
            val voltages = MutableSignal()
            val forwardEncPositions = view.forwardEncs.map { MutableSignal() }
            val forwardEncVels = view.forwardEncs.map { MutableSignal() }
        }

        waitForStart()

        val t = MidpointTimer()
        while (opModeIsActive()) {
            for (i in view.motors.indices) {
                val power = power(t.seconds())
                view.motors[i].power = power

                val s = data.powers[i]
                s.times.add(t.addSplit())
                s.values.add(power)
            }

            data.voltages.values.add(view.voltageSensor.voltage)
            data.voltages.times.add(t.addSplit())

            //val encTimes = view.resetAndBulkRead(t)

            for (i in view.forwardEncs.indices) {
                recordEncoderData(
                        view.forwardEncs[i],
                        //encTimes,
                        data.forwardEncPositions[i],
                        data.forwardEncVels[i],
                        t
                )
            }

            telemetry.addData("par enc pos", view.parEncs[0].getPositionAndVelocity().position)
            telemetry.addData("par enc vel", view.parEncs[0].getPositionAndVelocity().velocity)
            telemetry.addData("perp enc pos", view.perpEncs[0].getPositionAndVelocity().position)
            telemetry.addData("perp enc vel", view.perpEncs[0].getPositionAndVelocity().velocity)
            telemetry.update()
        }

        for (m in view.motors) {
            m.power = 0.0
        }

        TuningFiles.save(TuningFiles.FileType.FORWARD_RAMP, data)
    }
}

// designed for manual bulk caching
private fun recordEncoderData(e: Encoder, /*ts: Map<SerialNumber, Double>,*/ ps: MutableSignal, vs: MutableSignal, t: MidpointTimer) {
    //val sn = (e.controller as LynxDcMotorController).serialNumber
    val p = e.getPositionAndVelocity()

    ps.times.add(/*ts[sn]!!*/t.addSplit())
    ps.values.add(p.position.toDouble())

    vs.times.add(/*ts[sn]!!*/t.addSplit())
    vs.values.add(p.velocity.toDouble())
}