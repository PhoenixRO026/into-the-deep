package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.SequentialAction
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.lib.units.SleepAction
import org.firstinspires.ftc.teamcode.lib.units.s

class ExtendByServo(hardwareMap: HardwareMap) {
    private val extendServo = hardwareMap.get(Servo::class.java, "extendServo")

    init {
        extendServo.position = 0.5
    }

    companion object{
        const val robotExtendPos = 0.5

        val extendWait = 0.5.s
    }

    var extend : Double = robotExtendPos
        set(value){
            val clippedValue = value.coerceIn(0.0, 1.0)
            extendServo.position = clippedValue
            field = clippedValue
        }


    fun extendToPos(newExtend : Double) = SequentialAction(
        InstantAction { extend = newExtend},
        SleepAction(extendWait)
    )
}