package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createServoWithConfig
import org.firstinspires.ftc.teamcode.robot.config.IntakeConfig

class Intake(
    private val config: IntakeConfig
) {
    private lateinit var motorExtendoIntake: DcMotorEx
    private lateinit var motorSweeper: DcMotorEx
    private lateinit var servoIntakeTilt: Servo
    private lateinit var servoBoxTilt: Servo

    private var extendoOffset = 0

    fun init(hardwareMap: HardwareMap) {
        motorExtendoIntake = hardwareMap.createMotorUsingConfig(config.motorExtendoIntake)
        motorSweeper = hardwareMap.createMotorUsingConfig(config.motorSweeper)
        servoIntakeTilt = hardwareMap.createServoWithConfig(config.servoIntakeTilt)
        servoBoxTilt = hardwareMap.createServoWithConfig(config.servoBoxTilt)
    }

    val extendoPosition get() = motorExtendoIntake.currentPosition - extendoOffset

    var extendoPower by motorExtendoIntake::power

    var sweeperPower by motorSweeper::power

    var boxTilt by servoBoxTilt::position

    var intakeTilt by servoIntakeTilt::position

    fun resetExtendoPosition() {
        extendoOffset = motorExtendoIntake.currentPosition
    }
}