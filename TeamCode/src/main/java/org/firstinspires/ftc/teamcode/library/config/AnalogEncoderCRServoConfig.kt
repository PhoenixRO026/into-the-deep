package org.firstinspires.ftc.teamcode.library.config

import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.library.AnalogEncoderServo

data class AnalogEncoderCRServoConfig(
    val servoDeviceName: String,
    val encoderDeviceName: String,
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

    fun createAnalogEncoderCRServo(hardwareMap: HardwareMap): AnalogEncoderServo {
        return AnalogEncoderServo(hardwareMap, encoderDeviceName, servoDeviceName, toServoDirection())
    }
}

fun HardwareMap.createAnalogEncoderCRServ(config: AnalogEncoderCRServoConfig): AnalogEncoderServo {
    return config.createAnalogEncoderCRServo(this)
}