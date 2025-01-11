package org.firstinspires.ftc.teamcode.roadrunner.tuning.ex

import com.acmerobotics.roadrunner.ftc.DriveViewFactory
import com.acmerobotics.roadrunner.ftc.Encoder
import com.acmerobotics.roadrunner.ftc.MidpointTimer
import com.acmerobotics.roadrunner.ftc.TuningFiles
import com.qualcomm.hardware.lynx.LynxDcMotorController
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.SerialNumber
import org.firstinspires.ftc.teamcode.library.pinpoint.GoBildaPinpointDriver
import org.firstinspires.ftc.teamcode.roadrunner.PinpointLocalizer.PinpointConfig
import kotlin.math.min

class MutableSignal(
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
        val view = dvf.make(hardwareMap)

        val odo: GoBildaPinpointDriver = hardwareMap.get(GoBildaPinpointDriver::class.java, "odo")

        odo.setOffsets(
            PinpointConfig.xOffset,
            PinpointConfig.yOffset
        )
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
        odo.setEncoderDirections(
            GoBildaPinpointDriver.EncoderDirection.FORWARD,
            GoBildaPinpointDriver.EncoderDirection.FORWARD
        )
        odo.resetPosAndIMU()

        /*require(view.perpEncs.isNotEmpty()) {
            "Only run this op mode if you're using dead wheels."
        }*/

        view.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL)

        val data = object {
            val type = view.type
            val powers = view.motors.map { MutableSignal() }
            val voltages = MutableSignal()
            val forwardEncPositions = mutableListOf(MutableSignal())
            val forwardEncVels = mutableListOf(MutableSignal())
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

            odo.update()

            val split = t.addSplit()

            data.forwardEncPositions[0].values.add(odo.posX)
            data.forwardEncPositions[0].times.add(split)

            data.forwardEncVels[0].values.add(odo.velX)
            data.forwardEncVels[0].times.add(split)

            /*for (i in view.forwardEncs.indices) {
                recordEncoderData(
                    view.forwardEncs[i],
                    encTimes,
                    data.forwardEncPositions[i],
                    data.forwardEncVels[i]
                )
            }*/
        }

        for (m in view.motors) {
            m.power = 0.0
        }

        TuningFiles.save(TuningFiles.FileType.FORWARD_RAMP, data)
    }
}

// designed for manual bulk caching
private fun recordEncoderData(e: Encoder, ts: Map<SerialNumber, Double>, ps: MutableSignal, vs: MutableSignal) {
    val sn = (e.controller as LynxDcMotorController).serialNumber
    val p = e.getPositionAndVelocity()

    ps.times.add(ts[sn]!!)
    ps.values.add(p.position.toDouble())

    vs.times.add(ts[sn]!!)
    vs.values.add(p.velocity.toDouble())
}