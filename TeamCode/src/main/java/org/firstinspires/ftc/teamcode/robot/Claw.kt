package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction

class Claw (hardwareMap: HardwareMap) {
    private val fingerServo = hardwareMap.get(Servo::class.java, "fingers")
    private val rotationClawServo = hardwareMap.get(Servo::class.java, "clawRotation")

    init {
        fingerServo.position = closedFingers
        rotationClawServo.position = 0.5
    }

    companion object{
        const val openFingers = 0.5
        const val closedFingers = 0.7

        const val clawAngF = 0.5
        const val clawAngL = 0.5
        const val clawAngR = 0.5
        const val clawAngB = 0.5

        val fingerWait = 0.5
        val rotWait = 0.5
    }

    var rotation : Double = clawAngF
        set(value){
            val clippedValue = value.coerceIn(0.0, 1.0)
            rotationClawServo.position = clippedValue
            field = clippedValue
        }

    fun rotateToPos(newRot : Double) = SequentialAction(
        InstantAction { rotation = newRot},
        SleepAction(rotWait)
    )

    fun rotateToL() = rotateToPos(clawAngL)

    fun rotateToR() = rotateToPos(clawAngR)

    fun rotateToF() = rotateToPos(clawAngF)

    fun rotateToB() = rotateToPos(clawAngB)

    var finger : Double = 1.0
        set(value){
            val clippedValue = value.coerceIn(0.0, 1.0)
            fingerServo.position = clippedValue
            field = clippedValue
        }

    fun openClaw() = SequentialAction(
        InstantAction{ finger = openFingers },
        SleepAction(fingerWait)
    )

    fun closeClaw() = SequentialAction(
        InstantAction { finger = closedFingers },
        SleepAction(fingerWait)
    )
}