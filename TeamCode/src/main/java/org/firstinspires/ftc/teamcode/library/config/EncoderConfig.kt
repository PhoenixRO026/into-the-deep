package org.firstinspires.ftc.teamcode.library.config

import com.acmerobotics.roadrunner.ftc.Encoder
import com.acmerobotics.roadrunner.ftc.RawEncoder
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

data class EncoderConfig(
    val deviceName: String,
    val direction: Direction = Direction.FORWARD,
) {
    enum class Direction {
        FORWARD,
        REVERSE
    }

    private fun toEncoderDirection() = when (direction) {
        Direction.FORWARD -> DcMotorSimple.Direction.FORWARD
        Direction.REVERSE -> DcMotorSimple.Direction.REVERSE
    }

    fun createEncoder(hardwareMap: HardwareMap): Encoder {
        val motor = hardwareMap.get(DcMotorEx::class.java, deviceName)
        val encoder = RawEncoder(motor)
        encoder.direction = toEncoderDirection()
        return encoder
    }
}

fun HardwareMap.createEncoderUsingConfig(config: EncoderConfig): Encoder {
    return config.createEncoder(this)
}