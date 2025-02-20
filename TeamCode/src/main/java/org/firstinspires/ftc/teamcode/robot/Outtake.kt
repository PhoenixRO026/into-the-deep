package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.SequentialAction
import com.lib.units.Duration
import com.lib.units.SleepAction
import com.lib.units.s
import com.qualcomm.robotcore.hardware.Servo

class Outtake(
    val extendoServo: Servo,
    val shoulderServo: Servo,
    val elbowServo: Servo,
    val wristServo: Servo,
    val clawServo: Servo
) {
    @Config
    data object OuttakeConfig {
        @JvmField var shoulderInit = 0.6404
        @JvmField var elbowInit = 0.5159
        @JvmField var clawInit = 1.0
        @JvmField var extendoSpeed = 3.s
        @JvmField var actionSleepDuration = 1.s
    }

    var extendoSpeed = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    var extendoPos
        get() = extendoServo.position
        set(value) {
            extendoServo.position = value
        }

    var shoulderPos
        get() = shoulderServo.position
        set(value) {
            shoulderServo.position = value
        }

    var elbowPos
        get() = elbowServo.position
        set(value) {
            elbowServo.position = value
        }

    var wristPos
        get() = wristServo.position
        set(value) {
            wristServo.position = value
        }

    var clawPos
        get() = clawServo.position
        set(value) {
            clawServo.position = value
        }

    fun init() {
        shoulderPos = OuttakeConfig.shoulderInit
        elbowPos = OuttakeConfig.elbowInit
        clawPos = OuttakeConfig.clawInit
    }

    fun update(deltaTime: Duration) {
        extendoPos += extendoSpeed * (deltaTime / OuttakeConfig.extendoSpeed)
    }

    fun extendoToPosAction(pos: Double) = SequentialAction(
        InstantAction { extendoPos = pos },
        SleepAction(OuttakeConfig.actionSleepDuration)
    )

    fun shoulderToPosAction(pos: Double) = SequentialAction(
        InstantAction { shoulderPos = pos },
        SleepAction(OuttakeConfig.actionSleepDuration)
    )

    fun elbowToPosAction(pos: Double) = SequentialAction(
        InstantAction { elbowPos = pos },
        SleepAction(OuttakeConfig.actionSleepDuration)
    )

    fun wristToPosAction(pos: Double) = SequentialAction(
        InstantAction { wristPos = pos },
        SleepAction(OuttakeConfig.actionSleepDuration)
    )

    fun clawToPosAction(pos: Double) = SequentialAction(
        InstantAction { clawPos = pos },
        SleepAction(OuttakeConfig.actionSleepDuration)
    )
}