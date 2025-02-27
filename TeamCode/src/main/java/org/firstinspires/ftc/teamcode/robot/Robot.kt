package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createServoWithConfig
import org.firstinspires.ftc.teamcode.library.reverseScale
import org.firstinspires.ftc.teamcode.library.scaleTo

class Robot(private val config: RobotConfig) {

    private lateinit var motorLiftLeft: DcMotorEx
    private lateinit var motorLiftRight: DcMotorEx

    private lateinit var servoExtend: Servo

    private lateinit var servoArmLeft: Servo
    private lateinit var servoArmRight: Servo


    val drive = Drive(config.drive)

    /*
    var liftPower
        get() = motorLiftLeft.power
        set(value) {
            motorLiftLeft.power = value
            motorLiftRight.power = value
        }

    private val extendOffset = 0.01
    */
    var extendPosition
        get() = servoExtend.position.scaleTo(0.0..1.0)
        set(value) {
            servoExtend.position = value.scaleTo(0.0..1.0)
        }


    private val armOffset = 0.01

    var armPosition
        get() = servoArmLeft.position.reverseScale(armOffset..1.0)
        set(value) {
            servoArmLeft.position = value.scaleTo(armOffset..1.0)
            servoArmRight.position = value.scaleTo(0.0..(1.0 - armOffset))
        }



    fun init(hardwareMap: HardwareMap) {
        drive.init(hardwareMap)

        //motorLiftLeft = hardwareMap.createMotorUsingConfig(config.motorLiftLeft)
        //motorLiftRight = hardwareMap.createMotorUsingConfig(config.motorLiftRight)

        /*servoExtend = hardwareMap.createServoWithConfig(config.servoExtend)*/

        servoArmLeft = hardwareMap.createServoWithConfig(config.servoArmLeft)
        servoArmRight = hardwareMap.createServoWithConfig(config.servoArmRight)
    }
}