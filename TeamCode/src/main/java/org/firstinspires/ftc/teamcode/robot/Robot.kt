package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createServoWithConfig

class Robot(
    private val config: RobotConfig
) {
    private lateinit var motorLiftLeft: DcMotorEx
    private lateinit var motorLiftRight: DcMotorEx

    private lateinit var servoExtendLeft: Servo
    private lateinit var servoExtendRight: Servo

    val drive = Drive(config.drive)

    var liftPower
        get() = motorLiftLeft.power
        set(value) {
            motorLiftLeft.power = value
            motorLiftRight.power = value
        }

    var extendPosition
        get() = servoExtendLeft.position
        set(value) {
            servoExtendLeft.position = value
            servoExtendRight.position = value
        }

    fun init(hardwareMap: HardwareMap) {
        drive.init(hardwareMap)

        motorLiftLeft = hardwareMap.createMotorUsingConfig(config.motorLiftLeft)
        motorLiftRight = hardwareMap.createMotorUsingConfig(config.motorLiftRight)

        servoExtendLeft = hardwareMap.createServoWithConfig(config.servoExtendLeft)
        servoExtendRight = hardwareMap.createServoWithConfig(config.servoExtendRight)
    }
}