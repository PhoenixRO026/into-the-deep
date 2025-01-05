package org.firstinspires.ftc.teamcode.library.config

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.CRServoImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.PwmControl

data class CRServoConfig(
    val deviceName: String,
    val direction: Direction = Direction.FORWARD,
    val pwmRange: PWMRange = PWMRange.DEFAULT,
) {
    enum class Direction {
        FORWARD,
        REVERSE
    }

    enum class PWMRange {
        DEFAULT,
        MAXIMUM
    }

    private fun toServoDirection() = when (direction) {
        Direction.FORWARD -> DcMotorSimple.Direction.FORWARD
        Direction.REVERSE -> DcMotorSimple.Direction.REVERSE
    }

    fun createCRServo(hardwareMap: HardwareMap): CRServo {
        val servo = hardwareMap.get(CRServoImplEx::class.java, deviceName)
        servo.direction = toServoDirection()
        if (pwmRange == PWMRange.MAXIMUM) {
            servo.pwmRange = PwmControl.PwmRange(500.0, 2500.0)
        }
        return servo
    }
}

fun HardwareMap.createCRServoWithConfig(config: CRServoConfig): CRServo {
    return config.createCRServo(this)
}