package org.firstinspires.ftc.teamcode.library.config

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.CRServoImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

data class CRServoConfig(
    val deviceName: String,
    val direction: Direction = Direction.FORWARD,
) {
    enum class Direction {
        FORWARD,
        REVERSE
    }

    private fun toServoDirection() = when (direction) {
        Direction.FORWARD -> DcMotorSimple.Direction.FORWARD
        Direction.REVERSE -> DcMotorSimple.Direction.REVERSE
    }

    fun createCRServo(hardwareMap: HardwareMap): CRServo {
        val servo = hardwareMap.get(CRServoImplEx::class.java, deviceName)
        servo.direction = toServoDirection()
        return servo
    }
}

fun HardwareMap.createCRServoWithConfig(config: CRServoConfig): CRServo {
    return config.createCRServo(this)
}