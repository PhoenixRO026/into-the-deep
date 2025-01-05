package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig
import org.firstinspires.ftc.teamcode.robot.config.LiftConfig

class Lift(
    private val config: LiftConfig
) {
    private lateinit var motorLiftRight: DcMotorEx
    private lateinit var motorLiftLeft: DcMotorEx

    private var offset = 0

    fun init(hardwareMap: HardwareMap) {
        motorLiftRight = hardwareMap.createMotorUsingConfig(config.motorLiftRight)
        motorLiftLeft = hardwareMap.createMotorUsingConfig(config.motorLiftLeft)
    }
    val position get() = motorLiftRight.currentPosition - offset

    var power
        get() = motorLiftRight.power
        set(value) {
            motorLiftRight.power = value
            motorLiftLeft.power = value
        }

    fun resetPosition() {
        offset = motorLiftRight.currentPosition
    }
}