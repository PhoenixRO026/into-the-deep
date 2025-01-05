package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Arm (hardwareMap: HardwareMap) {
    private val tiltClawServo = hardwareMap.get(Servo::class.java, "cTilt")
    private val angClawServo = hardwareMap.get(Servo::class.java, "cAngle")
    private val extendClawServo = hardwareMap.get(Servo::class.java, "cExt")

    init {
        tiltClawServo.position = 0.5
        angClawServo.position = clawDown
    }

    companion object{
        const val robotTilt = 0.5
        const val scoreTilt = 0.5

        const val clawUp = 0.5
        const val clawDown = 0.0

        val tiltWait = 0.8
        val angWait = 0.5
        val extendWait = 0.5

        const val extendInrobot = 0.5
        const val extendOutrobot = 0.5
    }

    var tilt : Double = robotTilt
        set(value){
            val clippedValue = value.coerceIn(0.0, 1.0)
            tiltClawServo.position = clippedValue
            field = clippedValue
        }


    fun tiltToPos(newTilt : Double) = SequentialAction(
        InstantAction { tilt = newTilt},
        SleepAction(tiltWait)
    )

    fun tiltToS3core() = tiltToPos(scoreTilt)

    fun tiltToRobot() = tiltToPos(robotTilt)

    fun tiltUp() = tiltToPos(clawUp)

    fun tiltDown() = tiltToPos(clawDown)

    var angle : Double = clawDown
        set(value){
            val clippedValue = value.coerceIn(0.0, 1.0)
            angClawServo.position = clippedValue
            field = clippedValue
        }

    fun rotateToAng(newAng : Double) = SequentialAction(
        InstantAction{ angle = newAng},
        SleepAction(angWait)
    )

    fun rotateUp() = rotateToAng(clawUp)

    fun rotateDown() = rotateToAng(clawDown)

    var extend : Double = extendInrobot
        set(value){
            val clippedValue = value.coerceIn(0.0, 1.0)
            extendClawServo.position = clippedValue
            field = clippedValue
        }

    fun extendToPos(newExtend : Double) = SequentialAction(
        InstantAction { extend = newExtend},
        SleepAction(extendWait)
    )

    fun extendOut() = extendToPos(extendOutrobot)

    fun extendIn() = rotateToAng(extendInrobot)
}