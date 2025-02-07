package org.firstinspires.ftc.teamcode.library.config

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.NormalizedColorSensor

data class ColorSensorConfig(
    val deviceName: String
) {
    fun createColorSensor(hardwareMap: HardwareMap): NormalizedColorSensor {
        return hardwareMap.get(NormalizedColorSensor::class.java, deviceName)
    }
}

fun HardwareMap.createColorSensorWithConfig(config: ColorSensorConfig): NormalizedColorSensor {
    return config.createColorSensor(this)
}