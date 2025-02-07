package org.firstinspires.ftc.teamcode.library.config

import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.NormalizedColorSensor

data class ColorSensorConfig(
    val deviceName: String
) {
    fun createColorSensor(hardwareMap: HardwareMap): ColorSensor {
        return hardwareMap.get(ColorSensor::class.java, deviceName)
    }
}

fun HardwareMap.createColorSensorWithConfig(config: ColorSensorConfig): ColorSensor {
    return config.createColorSensor(this)
}