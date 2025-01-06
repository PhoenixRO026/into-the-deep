package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.ftc.Encoder
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.library.config.createEncoderUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig
import org.firstinspires.ftc.teamcode.robot.config.LiftHardwareConfig
import org.firstinspires.ftc.teamcode.robot.values.LiftValues

class Lift(
    hardwareMap: HardwareMap,
    config: LiftHardwareConfig,
    private val values: LiftValues
) {
    private val motorLiftRight: DcMotorEx = hardwareMap.createMotorUsingConfig(config.motorLiftRight)
    private val motorLiftLeft: DcMotorEx = hardwareMap.createMotorUsingConfig(config.motorLiftLeft)
    private val encoder: Encoder = hardwareMap.createEncoderUsingConfig(config.encoder)

    private var offset = 0

    val position get() = encoder.getPositionAndVelocity().position - offset

    var power
        get() = motorLiftRight.power
        set(value) {
            motorLiftRight.power = value
            motorLiftLeft.power = value
        }

    fun resetPosition() {
        offset = encoder.getPositionAndVelocity().position
    }
}