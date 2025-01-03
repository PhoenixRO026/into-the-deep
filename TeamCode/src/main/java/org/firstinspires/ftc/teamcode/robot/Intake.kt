package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.robot.Arm.Companion

class Intake(hardwareMap: HardwareMap) {
    val motor = hardwareMap.get(DcMotor::class.java, "iMotor")
    private val tiltIntakeServo = hardwareMap.get(Servo::class.java, "iTilt")
    private val tiltBoxServo = hardwareMap.get(Servo::class.java, "iTilt2")
    private val extendIntakeMotor : DcMotor = hardwareMap.get(DcMotor::class.java, "iExt")

    init {
        tiltBoxServo.position = 0.5
        tiltIntakeServo.position = 0.5
    }

    companion object{
        const val intakeUp = 0.5
        const val intakeDown = 0.5

        const val boxUp = 0.5
        const val boxDown = 0.5
        const val boxMid=0.5

        const val extendInrobot = 0.5
        const val extendOutrobot = 0.5

        val tiltWait = 0.8
        val boxWait = 0.5
        val extendWait = 0.5
    }

    var power : Double
        get() = motor.power
        set(value) {
            motor.power = value.toDouble()
        }

    var tiltIntake : Double = intakeUp
        set(value){
            val clippedValue = value.coerceIn(0.0, 1.0)
            tiltIntakeServo.position = clippedValue
            field = clippedValue
        }

    fun tiltToPosIntake(newTilt : Double) = SequentialAction(
        InstantAction { tiltIntake = newTilt},
        SleepAction(tiltWait)
    )

    fun tiltIntakeUp() = tiltToPosIntake(intakeUp)

    fun tiltIntakeDown() = tiltToPosIntake(intakeDown)

    var tiltBox : Double = boxUp
        set(value){
            val clippedValue = value.coerceIn(0.0, 1.0)
            tiltBoxServo.position = clippedValue
            field = clippedValue
        }

    fun tiltToPosBox(newTilt : Double) = SequentialAction(
        InstantAction { tiltBox = newTilt},
        SleepAction(boxWait)
    )

    fun tiltBoxUp() = tiltToPosBox(boxUp)

    fun tiltBoxMid() = tiltToPosBox(boxMid)

    fun tiltBoxDown() = tiltToPosBox(boxDown)
}