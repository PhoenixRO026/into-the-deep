package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.config.createServoWithConfig
import org.firstinspires.ftc.teamcode.robot.config.OuttakeConfig

class Outtake(
    private val config: OuttakeConfig
) {
    private lateinit var servoExtendo: Servo
    private lateinit var servoShoulder: Servo
    private lateinit var servoElbow: Servo
    private lateinit var servoWrist: Servo
    private lateinit var servoClaw: Servo

    fun init(hardwareMap: HardwareMap) {
        servoExtendo = hardwareMap.createServoWithConfig(config.servoExtendo)
        servoShoulder = hardwareMap.createServoWithConfig(config.servoShoulder)
        servoElbow = hardwareMap.createServoWithConfig(config.servoElbow)
        servoWrist = hardwareMap.createServoWithConfig(config.servoWrist)
        servoClaw = hardwareMap.createServoWithConfig(config.servoClaw)
    }

    var extendoPos by servoExtendo::position
    var shoulderPos by servoShoulder::position
    var elbowPos by servoElbow::position
    var wristPos by servoWrist::position
    var clawPos by servoClaw::position
}