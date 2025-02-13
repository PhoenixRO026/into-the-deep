package org.firstinspires.ftc.teamcode.library.config

import com.qualcomm.hardware.rev.RevBlinkinLedDriver
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.NormalizedColorSensor

data class RevBlinkinLedConfig(
    val deviceName: String
) {
    fun createBlinkinLed(hardwareMap: HardwareMap): RevBlinkinLedDriver {
        return hardwareMap.get(RevBlinkinLedDriver::class.java, deviceName)
    }
}

fun HardwareMap.createBlinkinLedWithConfig(config: RevBlinkinLedConfig): RevBlinkinLedDriver {
    return config.createBlinkinLed(this)
}