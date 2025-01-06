package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.AnalogEncoderServo
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.config.createAnalogEncoderCRServ
import org.firstinspires.ftc.teamcode.library.config.createServoWithConfig
import org.firstinspires.ftc.teamcode.robot.config.OuttakeHardwareConfig
import org.firstinspires.ftc.teamcode.robot.values.OuttakeValues
import kotlin.math.abs
import kotlin.math.sign

class Outtake(
    hardwareMap: HardwareMap,
    config: OuttakeHardwareConfig,
    private val values: OuttakeValues,
    private val timeKeep: TimeKeep
) {
    private val servoExtendo: AnalogEncoderServo = hardwareMap.createAnalogEncoderCRServ(config.servoExtendo)
    private val servoShoulder: Servo = hardwareMap.createServoWithConfig(config.servoShoulder)
    private val servoElbow: Servo = hardwareMap.createServoWithConfig(config.servoElbow)
    private val servoWrist: Servo = hardwareMap.createServoWithConfig(config.servoWrist)
    private val servoClaw: Servo = hardwareMap.createServoWithConfig(config.servoClaw)

    fun armTargetToBasket() {
        shoulderTargetPos = values.shoulderBasketPos
        elbowTargetPos = values.elbowBasketPos
    }

    fun armTargetToSpecimen() {
        shoulderTargetPos = values.shoulderSpecimenPos
        elbowTargetPos = values.elbowSpecimenPos
    }

    fun armTargetToIntake() {
        shoulderTargetPos = values.shoulderIntakePos
        elbowTargetPos = values.elbowIntakePos
    }

    fun wristPosToMiddle() {
        wristCurrentPos = values.wristMiddlPos
    }

    var extendoPower by servoExtendo::power

    val extendoPos by servoExtendo::position

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

    var wristCurrentPos
        get() = servoWrist.position
        set(value) {
            servoWrist.position = value
            _wristTargetPos = servoWrist.position
            wristSpeed = 0.0
        }

    private var _wristTargetPos = servoWrist.position
        set(value) {
            field = value.coerceIn(0.0, 1.0)
        }
    var wristTargetPos
        get() = _wristTargetPos
        set(value) {
            _wristTargetPos = value
            wristSpeed = 0.0
        }

    var wristSpeed: Double = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    var clawPos by servoClaw::position

    fun update() {
        servoExtendo.update()
        moveShoulder()
        moveElbow()
        moveWrist()
    }

    private fun moveWrist() {
        if (wristSpeed != 0.0) {
            servoWrist.position += timeKeep.deltaTime / values.wristMaxTravelDuration * wristSpeed
            _wristTargetPos = wristCurrentPos
            return
        }
        val error = wristCurrentPos - wristTargetPos
        if (error == 0.0) return
        val step = timeKeep.deltaTime / values.wristMaxTravelDuration
        if (abs(error) < step) {
            servoWrist.position = wristTargetPos
        } else {
            servoWrist.position += error.sign * step
        }
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