package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.config.createServoWithConfig
import org.firstinspires.ftc.teamcode.robot.config.OuttakeHardwareConfig
import org.firstinspires.ftc.teamcode.robot.values.OuttakeValues
import kotlin.math.abs
import kotlin.math.sign

class Outtake(
    private val config: OuttakeHardwareConfig,
    private val values: OuttakeValues,
    private val timeKeep: TimeKeep
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

    var extendoCurrentPos
        get() = servoExtendo.position
        set(value) {
            servoExtendo.position = value
            _extendoTargetPos = servoExtendo.position
            extendoSpeed = 0.0
        }

    private var _extendoTargetPos = servoExtendo.position
        set(value) {
            field = value.coerceIn(0.0, 1.0)
        }
    var extendoTargetPos
        get() = _extendoTargetPos
        set(value) {
            _extendoTargetPos = value
            extendoSpeed = 0.0
        }

    var extendoSpeed: Double = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    var shoulderCurrentPos
        get() = servoShoulder.position
        set(value) {
            servoShoulder.position = value
            _shoulderTargetPos = servoShoulder.position
            shoulderSpeed = 0.0
        }

    private var _shoulderTargetPos = servoShoulder.position
        set(value) {
            field = value.coerceIn(0.0, 1.0)
        }
    var shoulderTargetPos
        get() = _shoulderTargetPos
        set(value) {
            _shoulderTargetPos = value
            shoulderSpeed = 0.0
        }

    var shoulderSpeed: Double = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    var elbowCurrentPos
        get() = servoElbow.position
        set(value) {
            servoElbow.position = value
            _elbowTargetPos = servoElbow.position
            elbowSpeed = 0.0
        }

    private var _elbowTargetPos = servoElbow.position
        set(value) {
            field = value.coerceIn(0.0, 1.0)
        }
    var elbowTargetPos
        get() = _elbowTargetPos
        set(value) {
            _elbowTargetPos = value
            elbowSpeed = 0.0
        }

    var elbowSpeed: Double = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    var wristPos by servoWrist::position

    var clawPos by servoClaw::position

    fun update() {
        moveShoulder()
        moveElbow()
        moveExtendo()
    }

    private fun moveShoulder() {
        if (shoulderSpeed != 0.0) {
            servoShoulder.position += timeKeep.deltaTime / values.shoulderMaxTravelDuration * shoulderSpeed
            _shoulderTargetPos = shoulderCurrentPos
            return
        }
        val error = shoulderCurrentPos - shoulderTargetPos
        if (error == 0.0) return
        val step = timeKeep.deltaTime / values.shoulderMaxTravelDuration
        if (abs(error) < step) {
            servoShoulder.position = shoulderTargetPos
        } else {
            servoShoulder.position += error.sign * step
        }
    }

    private fun moveExtendo() {
        if (extendoSpeed != 0.0) {
            servoExtendo.position += timeKeep.deltaTime / values.extendoMaxTravelDuration * extendoSpeed
            _extendoTargetPos = extendoCurrentPos
            return
        }
        val error = extendoCurrentPos - extendoTargetPos
        if (error == 0.0) return
        val step = timeKeep.deltaTime / values.extendoMaxTravelDuration
        if (abs(error) < step) {
            servoExtendo.position = extendoTargetPos
        } else {
            servoExtendo.position += error.sign * step
        }
    }

    private fun moveElbow() {
        if (elbowSpeed != 0.0) {
            servoElbow.position += timeKeep.deltaTime / values.elbowMaxTravelDuration * elbowSpeed
            _elbowTargetPos = elbowCurrentPos
            return
        }
        val error = elbowCurrentPos - elbowTargetPos
        if (error == 0.0) return
        val step = timeKeep.deltaTime / values.elbowMaxTravelDuration
        if (abs(error) < step) {
            servoElbow.position = elbowTargetPos
        } else {
            servoElbow.position += error.sign * step
        }
    }
}