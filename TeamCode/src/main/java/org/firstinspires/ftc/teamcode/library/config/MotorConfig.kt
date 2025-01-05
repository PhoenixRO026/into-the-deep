package org.firstinspires.ftc.teamcode.library.config

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

data class MotorConfig(
    val deviceName: String,
    val direction: Direction = Direction.FORWARD,
    val runMode: RunMode = RunMode.RUN_WITHOUT_ENCODER,
    val zeroPowerBehavior: ZeroPowerBehavior = ZeroPowerBehavior.BRAKE,
    val resetEncoder: Boolean = true
) {
    enum class Direction {
        FORWARD,
        REVERSE
    }

    enum class RunMode {
        RUN_WITHOUT_ENCODER,
        RUN_USING_ENCODER,
        RUN_TO_POSITION
    }

    enum class ZeroPowerBehavior {
        BRAKE,
        FLOAT
    }

    private fun toMotorDirection() = when (direction) {
        Direction.FORWARD -> DcMotorSimple.Direction.FORWARD
        Direction.REVERSE -> DcMotorSimple.Direction.REVERSE
    }

    private fun toMotorRunMode() = when (runMode) {
        RunMode.RUN_WITHOUT_ENCODER -> DcMotor.RunMode.RUN_WITHOUT_ENCODER
        RunMode.RUN_USING_ENCODER -> DcMotor.RunMode.RUN_USING_ENCODER
        RunMode.RUN_TO_POSITION -> DcMotor.RunMode.RUN_TO_POSITION
    }

    private fun toMotorZeroPowerBehavior() = when (zeroPowerBehavior) {
        ZeroPowerBehavior.BRAKE -> DcMotor.ZeroPowerBehavior.BRAKE
        ZeroPowerBehavior.FLOAT -> DcMotor.ZeroPowerBehavior.FLOAT
    }

    fun createMotor(hardwareMap: HardwareMap): DcMotorEx {
        val motor = hardwareMap.get(DcMotorEx::class.java, deviceName)
        if (resetEncoder)
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = toMotorRunMode()
        motor.zeroPowerBehavior = toMotorZeroPowerBehavior()
        motor.direction = toMotorDirection()
        return motor
    }
}

fun HardwareMap.createMotorUsingConfig(config: MotorConfig): DcMotorEx {
    return config.createMotor(this)
}