package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction

class Claw (hardwareMap: HardwareMap) {
    private val fingerServo = hardwareMap.get(Servo::class.java, "fingers")
    private val clawTiltServo = hardwareMap.get(Servo::class.java, "cTilt")
    private val clawTiltServo2 = hardwareMap.get(Servo::class.java, "cTilt2")
    private val clawRotationServo = hardwareMap.get(Servo::class.java, "clawRotation")
    private val clawAngServo = hardwareMap.get(Servo::class.java, "cAngle")

    init {
        clawTiltServo2.direction = Servo.Direction.REVERSE
        fingerServo.position = closedFingers
        clawRotationServo.position = 0.5
        clawTiltServo.position = 0.5
        clawAngServo.position = clawDown
    }

    companion object{
        const val robotTilt = 0.5
        const val intakeTilt = 0.5
        const val scoreTilt = 0.5

        const val openFingers = 0.353
        const val closedFingers = 0.6

        const val clawAngF = 0.5
        const val clawAngL = 0.5
        const val clawAngR = 0.5
        const val clawAngB = 0.5

        const val clawUp = 0.5
        const val clawDown = 0.0


        val tiltWait = 0.8
        val fingerWait = 0.5
        val rotWait = 0.5
        val angWait = 0.5
    }
    //tilt
    var tilt : Double = robotTilt
        set(value){
            val clippedValue = value.coerceIn(0.0, 1.0)
            clawTiltServo.position = clippedValue
            field = clippedValue
        }


    fun tiltToPos(newTilt : Double) = SequentialAction(
        InstantAction { tilt = newTilt},
        SleepAction(tiltWait)
    )

    fun tiltToScore() = tiltToPos(scoreTilt)

    fun tiltToIntake() = tiltToPos(intakeTilt)

    fun tiltToRobot() = tiltToPos(robotTilt)

    fun tiltToUp() = tiltToPos(clawUp)

    fun tiltToDown() = tiltToPos(clawDown)

    //rotation
    var rotation : Double = clawAngF
        set(value){
            val clippedValue = value.coerceIn(0.0, 1.0)
            clawRotationServo.position = clippedValue
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

    var angle : Double = clawDown
        set(value){
            val clippedValue = value.coerceIn(0.0, 1.0)
            clawAngServo.position = clippedValue
            field = clippedValue
        }

    fun rotateToAng(newAng : Double) = SequentialAction(
        InstantAction{ angle = newAng},
        SleepAction(angWait)
    )

    fun rotateToUp() = rotateToAng(clawUp)

    fun rotateToDown() = rotateToAng(clawDown)

    //finger
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