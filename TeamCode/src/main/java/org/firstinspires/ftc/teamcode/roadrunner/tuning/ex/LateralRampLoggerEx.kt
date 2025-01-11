package org.firstinspires.ftc.teamcode.roadrunner.tuning.ex

import com.acmerobotics.roadrunner.ftc.DriveType
import com.acmerobotics.roadrunner.ftc.DriveViewFactory
import com.acmerobotics.roadrunner.ftc.MidpointTimer
import com.acmerobotics.roadrunner.ftc.TuningFiles
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.library.pinpoint.GoBildaPinpointDriver
import org.firstinspires.ftc.teamcode.roadrunner.PinpointLocalizer.PinpointConfig
import kotlin.math.min

class LateralRampLoggerEx(val dvf: DriveViewFactory) : LinearOpMode() {
    companion object {
        @JvmField
        var POWER_PER_SEC = 0.1
        @JvmField
        var POWER_MAX = 0.9
    }

    fun power(seconds: Double) = min(POWER_PER_SEC * seconds, POWER_MAX)

    override fun runOpMode() {
        val view = dvf.make(hardwareMap)
        view.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL)

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

        require(view.type == DriveType.MECANUM) {
            "Only mecanum drives should run this op mode."
        }

        val data = object {
            val type = view.type
            val frontLeftPower = MutableSignal()
            val backLeftPower = MutableSignal()
            val frontRightPower = MutableSignal()
            val backRightPower = MutableSignal()
            val voltages = MutableSignal()
            val perpEncPositions = mutableListOf(MutableSignal())
            val perpEncVels = mutableListOf(MutableSignal())
        }

        waitForStart()

        val t = MidpointTimer()

        fun setMotorPower(m: DcMotorEx, sign: Int, signal: MutableSignal) {
            val power = sign * power(t.seconds())
            m.power = power

            signal.times.add(t.addSplit())
            signal.values.add(power)
        }

        while (opModeIsActive()) {
            setMotorPower(view.leftMotors[0], -1, data.frontLeftPower)
            setMotorPower(view.rightMotors[0], +1, data.frontRightPower)
            setMotorPower(view.leftMotors[1], +1, data.backLeftPower)
            setMotorPower(view.rightMotors[1], -1, data.backRightPower)

            data.voltages.values.add(view.voltageSensor.voltage)
            data.voltages.times.add(t.addSplit())

            odo.update()

            val split = t.addSplit()

            data.perpEncPositions[0].values.add(odo.posY)
            data.perpEncPositions[0].times.add(split)

            data.perpEncVels[0].values.add(odo.velY)
            data.perpEncVels[0].times.add(split)



            //val encTimes = view.resetAndBulkRead(t)

            /*for (i in view.perpEncs.indices) {
                recordEncoderData(
                        view.perpEncs[i],
                        encTimes,
                        data.perpEncPositions[i],
                        data.perpEncVels[i]
                )
            }*/
        }

        for (m in view.motors) {
            m.power = 0.0
        }

        TuningFiles.save(TuningFiles.FileType.LATERAL_RAMP, data)
    }
}