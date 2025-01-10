package org.firstinspires.ftc.teamcode.roadrunner.tuning.ex

import com.acmerobotics.roadrunner.ftc.DriveViewFactory
import com.acmerobotics.roadrunner.ftc.MidpointTimer
import com.acmerobotics.roadrunner.ftc.TuningFiles
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.library.pinpoint.GoBildaPinpointDriver
import org.firstinspires.ftc.teamcode.roadrunner.PinpointLocalizer.PinpointConfig
import kotlin.math.min

class AngularRampLoggerEx(val dvf: DriveViewFactory) : LinearOpMode() {
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

        val data = object {
            val type = view.type
            val leftPowers = view.leftMotors.map { MutableSignal() }
            val rightPowers = view.rightMotors.map { MutableSignal() }
            val voltages = MutableSignal()
            val leftEncPositions = mutableListOf<MutableSignal>()
            val rightEncPositions = mutableListOf<MutableSignal>()
            val parEncPositions = mutableListOf(MutableSignal())
            val perpEncPositions = mutableListOf(MutableSignal())
            val leftEncVels = mutableListOf<MutableSignal>()
            val rightEncVels = mutableListOf<MutableSignal>()
            val parEncVels = mutableListOf(MutableSignal())
            val perpEncVels = mutableListOf(MutableSignal())
            val angVels = listOf(MutableSignal(), MutableSignal(), MutableSignal())
        }

        waitForStart()

        val t = MidpointTimer()
        while (opModeIsActive()) {
            for (i in view.leftMotors.indices) {
                val power = -power(t.seconds())
                view.leftMotors[i].power = power

                val s = data.leftPowers[i]
                s.times.add(t.addSplit())
                s.values.add(power)
            }

            for (i in view.rightMotors.indices) {
                val power = power(t.seconds())
                view.rightMotors[i].power = power

                val s = data.rightPowers[i]
                s.times.add(t.addSplit())
                s.values.add(power)
            }

            data.voltages.values.add(view.voltageSensor.voltage)
            data.voltages.times.add(t.addSplit())

            //val encTimes = view.resetAndBulkRead(t)

            /*for (i in view.leftEncs.indices) {
                recordEncoderData(
                        view.leftEncs[i],
                        encTimes,
                        data.leftEncPositions[i],
                        data.leftEncVels[i]
                )
            }

            for (i in view.rightEncs.indices) {
                recordEncoderData(
                        view.rightEncs[i],
                        encTimes,
                        data.rightEncPositions[i],
                        data.rightEncVels[i]
                )
            }

            for (i in view.parEncs.indices) {
                recordEncoderData(
                        view.parEncs[i],
                        encTimes,
                        data.parEncPositions[i],
                        data.parEncVels[i]
                )
            }

            for (i in view.perpEncs.indices) {
                recordEncoderData(
                        view.perpEncs[i],
                        encTimes,
                        data.perpEncPositions[i],
                        data.perpEncVels[i]
                )
            }*/

            odo.update()

            val split = t.addSplit()

            data.perpEncPositions[0].values.add(odo.posY)
            data.perpEncPositions[0].times.add(split)

            data.perpEncVels[0].values.add(odo.velY)
            data.perpEncVels[0].times.add(split)

            data.parEncPositions[0].values.add(odo.posX)
            data.parEncPositions[0].times.add(split)

            data.parEncVels[0].values.add(odo.velX)
            data.parEncVels[0].times.add(split)

            t.addSplit()
            // Use degrees here to work around https://github.com/FIRST-Tech-Challenge/FtcRobotController/issues/1070
            val av = odo.velocity.getHeading(AngleUnit.DEGREES)
            //val av = view.imu.get().getRobotAngularVelocity(AngleUnit.DEGREES)
            val time = t.addSplit()

            data.angVels[0].times.add(time)
            data.angVels[1].times.add(time)
            data.angVels[2].times.add(time)

//            data.angVels[0].values.add(Math.toRadians(av.xRotationRate.toDouble()))
//            data.angVels[1].values.add(Math.toRadians(av.yRotationRate.toDouble()))
//            data.angVels[2].values.add(Math.toRadians(av.zRotationRate.toDouble()))
            data.angVels[0].values.add(Math.toRadians(0.0))
            data.angVels[1].values.add(Math.toRadians(0.0))
            data.angVels[2].values.add(Math.toRadians(av))
        }

        for (m in view.motors) {
            m.power = 0.0
        }

        TuningFiles.save(TuningFiles.FileType.ANGULAR_RAMP, data)
    }
}