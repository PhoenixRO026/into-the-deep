package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.ftc.Encoder
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.config.createEncoderUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createServoWithConfig
import org.firstinspires.ftc.teamcode.robot.config.IntakeHardwareConfig
import org.firstinspires.ftc.teamcode.robot.values.IntakeValues
import kotlin.math.abs
import kotlin.math.sign

class Intake(
    hardwareMap: HardwareMap,
    config: IntakeHardwareConfig,
    private val values: IntakeValues,
    private val timeKeep: TimeKeep
) {
    private val motorExtendoIntake: DcMotorEx = hardwareMap.createMotorUsingConfig(config.motorExtendoIntake)
    private val motorSweeper: DcMotorEx = hardwareMap.createMotorUsingConfig(config.motorSweeper)
    private val servoIntakeTilt: Servo = hardwareMap.createServoWithConfig(config.servoIntakeTilt)
    private val servoBoxTilt: Servo = hardwareMap.createServoWithConfig(config.servoBoxTilt)
    private val encoderExtendo: Encoder = hardwareMap.createEncoderUsingConfig(config.encoderExtendo)

    private var extendoOffset = 0

    val extendoPosition get() = encoderExtendo.getPositionAndVelocity().position - extendoOffset

    fun intakeDown() {
        intakeTiltCurrentPos = 0.0
    }

    fun intakeUp() {
        intakeTiltCurrentPos = 1.0
    }

    fun boxDown() {
        boxTiltCurrentPos = 0.0
    }

    fun boxUp() {
        boxTiltCurrentPos = 1.0
    }

    var extendoPower
        get() = motorExtendoIntake.power
        set(value) {
            motorExtendoIntake.power = value
        }

    var sweeperPower by motorSweeper::power

    var boxTiltCurrentPos
        get() = servoBoxTilt.position
        set(value) {
            servoBoxTilt.position = value
            _boxTiltTargetPos = servoBoxTilt.position
            boxTiltSpeed = 0.0
        }

    private var _boxTiltTargetPos = servoBoxTilt.position
        set(value) {
            field = value.coerceIn(0.0, 1.0)
        }
    var boxTiltTargetPos
        get() = _boxTiltTargetPos
        set(value) {
            _boxTiltTargetPos = value
            boxTiltSpeed = 0.0
        }

    var boxTiltSpeed: Double = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    var intakeTiltCurrentPos
        get() = servoIntakeTilt.position
        set(value) {
            servoIntakeTilt.position = value
            _intakeTiltTargetPos = servoIntakeTilt.position
            intakeTiltSpeed = 0.0
        }

    private var _intakeTiltTargetPos = servoIntakeTilt.position
        set(value) {
            field = value.coerceIn(0.0, 1.0)
        }
    var intakeTiltTargetPos
        get() = _intakeTiltTargetPos
        set(value) {
            _intakeTiltTargetPos = value
            intakeTiltSpeed = 0.0
        }

    var intakeTiltSpeed: Double = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    fun resetExtendoPosition() {
        extendoOffset = encoderExtendo.getPositionAndVelocity().position
    }

    fun update() {
        moveBoxTilt()
        moveIntakeTilt()
    }

    private fun moveBoxTilt() {
        if (boxTiltSpeed != 0.0) {
            servoBoxTilt.position += timeKeep.deltaTime / values.boxTiltMaxTravelDuration * boxTiltSpeed
            _boxTiltTargetPos = boxTiltCurrentPos
            return
        }
        val error = boxTiltCurrentPos - boxTiltTargetPos
        if (error == 0.0) return
        val step = timeKeep.deltaTime / values.boxTiltMaxTravelDuration
        if (abs(error) < step) {
            servoBoxTilt.position = boxTiltTargetPos
        } else {
            servoBoxTilt.position += error.sign * step
        }
    }

    private fun moveIntakeTilt() {
        if (intakeTiltSpeed != 0.0) {
            servoIntakeTilt.position += timeKeep.deltaTime / values.intakeTiltMaxTravelDuration * intakeTiltSpeed
            _intakeTiltTargetPos = intakeTiltCurrentPos
            return
        }
        val error = intakeTiltCurrentPos - intakeTiltTargetPos
        if (error == 0.0) return
        val step = timeKeep.deltaTime / values.intakeTiltMaxTravelDuration
        if (abs(error) < step) {
            servoIntakeTilt.position = intakeTiltTargetPos
        } else {
            servoIntakeTilt.position += error.sign * step
        }
    }
}