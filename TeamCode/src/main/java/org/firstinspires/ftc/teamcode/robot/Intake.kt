package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.ftc.Encoder
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.config.createEncoderUsingConfig
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
    private lateinit var encoderExtendo: Encoder

    private var extendoOffset = 0

    fun init(hardwareMap: HardwareMap) {
        motorExtendoIntake = hardwareMap.createMotorUsingConfig(config.motorExtendoIntake)
        motorSweeper = hardwareMap.createMotorUsingConfig(config.motorSweeper)
        servoIntakeTilt = hardwareMap.createServoWithConfig(config.servoIntakeTilt)
        servoBoxTilt = hardwareMap.createServoWithConfig(config.servoBoxTilt)
        encoderExtendo = hardwareMap.createEncoderUsingConfig(config.encoderExtendo)
    }

    val extendoPosition get() = encoderExtendo.getPositionAndVelocity().position - extendoOffset

    var extendoPower by motorExtendoIntake::power

    var sweeperPower by motorSweeper::power

    var boxTilt by servoBoxTilt::position

    var intakeTilt by servoIntakeTilt::position

    fun resetExtendoPosition() {
        extendoOffset = encoderExtendo.getPositionAndVelocity().position
    }
}