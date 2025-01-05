package org.firstinspires.ftc.teamcode.library.config

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.teamcode.library.config.IMUConfig.LogoDirection
import org.firstinspires.ftc.teamcode.library.config.IMUConfig.USBDirection

data class IMUConfig(
    val deviceName: String,
    val logoDirection: LogoDirection,
    val usbDirection: USBDirection,
    val resetYaw: Boolean = true
) {
    enum class LogoDirection {
        UP,
        DOWN,
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }

    enum class USBDirection {
        UP,
        DOWN,
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }

    private fun toHubOrientation() = RevHubOrientationOnRobot(logoDirection.toRevLogoDirection(), usbDirection.toRevUSBDirection())

    private fun toRevParameters() = IMU.Parameters(toHubOrientation())

    fun createIMU(hardwareMap: HardwareMap): IMU {
        val imu = hardwareMap.get(IMU::class.java, deviceName)
        imu.initialize(toRevParameters())
        if (resetYaw)
            imu.resetYaw()
        return imu
    }
}

fun HardwareMap.createIMUUsingConfig(config: IMUConfig): IMU {
    return config.createIMU(this)
}

fun USBDirection.toRevUSBDirection() = when (this) {
    USBDirection.UP -> RevHubOrientationOnRobot.UsbFacingDirection.UP
    USBDirection.DOWN -> RevHubOrientationOnRobot.UsbFacingDirection.DOWN
    USBDirection.FORWARD -> RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
    USBDirection.BACKWARD -> RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
    USBDirection.LEFT -> RevHubOrientationOnRobot.UsbFacingDirection.LEFT
    USBDirection.RIGHT -> RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
}

fun LogoDirection.toRevLogoDirection() = when (this) {
    LogoDirection.UP -> RevHubOrientationOnRobot.LogoFacingDirection.UP
    LogoDirection.DOWN -> RevHubOrientationOnRobot.LogoFacingDirection.DOWN
    LogoDirection.FORWARD -> RevHubOrientationOnRobot.LogoFacingDirection.FORWARD
    LogoDirection.BACKWARD -> RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD
    LogoDirection.LEFT -> RevHubOrientationOnRobot.LogoFacingDirection.LEFT
    LogoDirection.RIGHT -> RevHubOrientationOnRobot.LogoFacingDirection.RIGHT
}