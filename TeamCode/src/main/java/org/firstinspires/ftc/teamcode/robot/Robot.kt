package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createServoWithConfig

class Robot(
    val config: RobotConfig
) {
    lateinit var motorRF: DcMotorEx
    lateinit var motorRB: DcMotorEx
    lateinit var motorLF: DcMotorEx
    lateinit var motorLB: DcMotorEx

    lateinit var motorLiftLeft: DcMotorEx
    lateinit var motorLiftRight: DcMotorEx

    lateinit var servoExtendLeft: Servo
    lateinit var servoExtendRight: Servo

    inline var liftPower
        get() = motorLiftLeft.power
        set(value) {
            motorLiftLeft.power = value
            motorLiftRight.power = value
        }

    inline var extendPosition
        get() = servoExtendLeft.position
        set(value) {
            servoExtendLeft.position = value
            servoExtendRight.position = value
        }

    fun init(hardwareMap: HardwareMap) {
        motorRF = hardwareMap.createMotorUsingConfig(config.motorRF)
        motorRB = hardwareMap.createMotorUsingConfig(config.motorRB)
        motorLF = hardwareMap.createMotorUsingConfig(config.motorLF)
        motorLB = hardwareMap.createMotorUsingConfig(config.motorLB)

        motorLiftLeft = hardwareMap.createMotorUsingConfig(config.motorLiftLeft)
        motorLiftRight = hardwareMap.createMotorUsingConfig(config.motorLiftRight)

        servoExtendLeft = hardwareMap.createServoWithConfig(config.servoExtendLeft)
        servoExtendRight = hardwareMap.createServoWithConfig(config.servoExtendRight)
    }
}