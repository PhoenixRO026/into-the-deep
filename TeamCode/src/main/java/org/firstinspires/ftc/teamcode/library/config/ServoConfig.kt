package org.firstinspires.ftc.teamcode.library.config

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.PwmControl
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoImplEx

data class ServoConfig(
    val deviceName: String,
    val direction: Direction = Direction.FORWARD,
    val pwmRange: PWMRange = PWMRange.DEFAULT,
    val rangeScale: ClosedRange<Double> = 0.0..1.0
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
        Direction.FORWARD -> Servo.Direction.FORWARD
        Direction.REVERSE -> Servo.Direction.REVERSE
    }

    fun createServo(hardwareMap: HardwareMap): Servo {
        val servo = hardwareMap.get(ServoImplEx::class.java, deviceName)
        servo.direction = toServoDirection()
        if (pwmRange == PWMRange.MAXIMUM) {
            servo.pwmRange = PwmControl.PwmRange(500.0, 2500.0)
        }
        servo.scaleRange(rangeScale.start, rangeScale.endInclusive)
        return servo
    }
}

fun HardwareMap.createServoWithConfig(config: ServoConfig): Servo {
    return config.createServo(this)
}