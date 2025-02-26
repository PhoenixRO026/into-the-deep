package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.lib.units.Duration
import com.lib.units.SleepAction
import com.lib.units.s
import com.qualcomm.robotcore.hardware.Servo
import kotlin.math.abs

class Outtake(
    val extendoServo: Servo,
    val shoulderServo: Servo,
    val elbowServo: Servo,
    val wristServo: Servo,
    val clawServo: Servo
) {
    @Config
    data object OuttakeConfig {
        @JvmField var extendoSpeed = 3.s

        @JvmField var shoulderActionSleepDuration = 2.s
        @JvmField var elbowActionSleepDuration = 2.s
        @JvmField var wristActionSleepDuration = 2.s
        @JvmField var clawActionSleepDuration = 0.5.s
        @JvmField var extendoActionSleepDuration = 2.s

        @JvmField var shoulderNeutralPos = 0.6404
        @JvmField var elbowNeutralPos = 0.5159
        @JvmField var extendoNeutralPos = 0.6
        @JvmField var wristMidPos = 0.5133
        @JvmField var wristUpsideDown = 0.0
        @JvmField var clawOpenPos = 1.0
        @JvmField var clawClosedPos = 0.0

        @JvmField var shoulderTeleInit = shoulderNeutralPos
        @JvmField var elbowTeleInit = elbowNeutralPos
        @JvmField var extendoTeleInit = extendoNeutralPos
        @JvmField var wristTeleInit = wristMidPos
        @JvmField var clawTeleInit = clawClosedPos

        @JvmField var shoulderAutoInit = shoulderNeutralPos
        @JvmField var elbowAutoInit = elbowNeutralPos
        @JvmField var wristAutoInit = wristMidPos
        @JvmField var extendoAutoInit = extendoNeutralPos
        @JvmField var clawAutoInit = clawClosedPos

        @JvmField var shoulderIntakePos = 0.8208
        @JvmField var elbowIntakePos = 0.2916
        @JvmField var extendoIntakePos = 0.5307

        @JvmField var shoulderBasketPos = 0.4045
        @JvmField var elbowBasketPos = 0.7172
        @JvmField var extendoBasketPos = extendoNeutralPos

        @JvmField var shoulderSpecimenPickupPos = 0.3022
        @JvmField var elbowSpecimenPickupPos = 0.6378
        @JvmField var extendoSpecimenPickupPos = extendoNeutralPos

        @JvmField var shoulderBarPos = 0.7474
        @JvmField var elbowBarPos = 0.864
        @JvmField var extendoBarPos = 0.2478

        @JvmField var shoulderOldBarPos = 0.8694
        @JvmField var elbowOldBarPos = 0.894

        @JvmField var extendoMax = extendoBarPos
    }

    var extendoSpeed = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    var extendoPos
        get() = extendoServo.position
        set(value) {
            extendoServo.position = value
            extendoSpeed = 0.0
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

    var clawPos: Double = 0.0
        get() = clawServo.position
        set(value) {
            val clampedVal = value.coerceIn(0.0, 1.0)
            if (clampedVal == field) return
            field = clampedVal
            clawServo.position = field
        }

    fun initTeleop() {
        shoulderPos = OuttakeConfig.shoulderTeleInit
        elbowPos = OuttakeConfig.elbowTeleInit
        clawPos = OuttakeConfig.clawTeleInit
        wristPos = OuttakeConfig.wristTeleInit
        extendoPos = OuttakeConfig.extendoTeleInit
    }

    fun initAuto() {
        shoulderPos = OuttakeConfig.shoulderAutoInit
        elbowPos = OuttakeConfig.elbowAutoInit
        clawPos =  OuttakeConfig.clawAutoInit
        wristPos = OuttakeConfig.wristAutoInit
        extendoPos = OuttakeConfig.extendoAutoInit
    }

    fun update(deltaTime: Duration) {
        extendoPos += extendoSpeed * (deltaTime / OuttakeConfig.extendoSpeed)
    }

    fun extendoToPosAction(pos: Double) = object : Action {
        var init = true
        lateinit var sleepAction: Action
        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                if (pos == extendoPos) return false
                sleepAction = SleepAction(OuttakeConfig.extendoActionSleepDuration * abs(pos - extendoPos))
                extendoPos = pos
            }
            return sleepAction.run(p)
        }
    }

    fun shoulderToPosAction(pos: Double) = object : Action {
        var init = true
        lateinit var sleepAction: Action
        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                if (pos == shoulderPos) return false
                sleepAction = SleepAction(OuttakeConfig.shoulderActionSleepDuration * abs(pos - shoulderPos))
                shoulderPos = pos
            }
            return sleepAction.run(p)
        }
    }

    fun elbowToPosAction(pos: Double) = object : Action {
        var init = true
        lateinit var sleepAction: Action
        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                if (pos == elbowPos) return false
                sleepAction = SleepAction(OuttakeConfig.elbowActionSleepDuration * abs(pos - elbowPos))
                elbowPos = pos
            }
            return sleepAction.run(p)
        }
    }

    fun wristToPosAction(pos: Double) = object : Action {
        var init = true
        lateinit var sleepAction: Action
        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                if (pos == wristPos) return false
                sleepAction = SleepAction(OuttakeConfig.wristActionSleepDuration * abs(pos - wristPos))
                wristPos = pos
            }
            return sleepAction.run(p)
        }
    }

    fun clawToPosAction(pos: Double) = object : Action {
        var init = true
        lateinit var sleepAction: Action
        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                if (pos == clawPos) return false
                sleepAction = SleepAction(OuttakeConfig.clawActionSleepDuration * abs(pos - clawPos))
                clawServo.position = pos
            }
            return sleepAction.run(p)
        }
    }

    fun extendoToMaxInstant() {
        extendoPos = OuttakeConfig.extendoMax
    }

    fun extendoInInstant() {
        extendoPos = OuttakeConfig.extendoNeutralPos
    }

    fun openClawAction() = clawToPosAction(OuttakeConfig.clawOpenPos)
    fun closeClawAction() = clawToPosAction(OuttakeConfig.clawClosedPos)

    fun wristToMidAction() = wristToPosAction(OuttakeConfig.wristMidPos)
    fun wristToMidInstant() {
        wristPos = OuttakeConfig.wristMidPos
    }
    fun wristToUpsideDownInstant() {
        wristPos = OuttakeConfig.wristUpsideDown
    }

    fun shoulderToNeutralAction() = shoulderToPosAction(OuttakeConfig.shoulderNeutralPos)
    fun elbowToNeutralAction() = elbowToPosAction(OuttakeConfig.elbowNeutralPos)
    fun extendoToNeutralAction() = extendoToPosAction(OuttakeConfig.extendoNeutralPos)

    fun shoulderToIntakeAction() = shoulderToPosAction(OuttakeConfig.shoulderIntakePos)
    fun elbowToIntakeAction() = elbowToPosAction(OuttakeConfig.elbowIntakePos)
    fun extendoToIntakeAction() = extendoToPosAction(OuttakeConfig.extendoIntakePos)

    fun shoulderToBarAction() = shoulderToPosAction(OuttakeConfig.shoulderBarPos)
    fun elbowToBarAction() = elbowToPosAction(OuttakeConfig.elbowBarPos)
    fun extendoToBarAction() = extendoToPosAction(OuttakeConfig.extendoBarPos)

    fun shoulderToBasketAction() = shoulderToPosAction(OuttakeConfig.shoulderBasketPos)
    fun elbowToBasketAction() = elbowToPosAction(OuttakeConfig.elbowBasketPos)
    fun extendoToBasketAction() = extendoToPosAction(OuttakeConfig.extendoBasketPos)

    fun shoulderToSpecimenPickupAction() = shoulderToPosAction(OuttakeConfig.shoulderSpecimenPickupPos)
    fun elbowToSpecimenPickupAction() = elbowToPosAction(OuttakeConfig.elbowSpecimenPickupPos)
    fun extendoToSpecimenPickupAction() = extendoToPosAction(OuttakeConfig.extendoSpecimenPickupPos)

    fun armToSpecimenInstant() {
        shoulderPos = OuttakeConfig.shoulderSpecimenPickupPos
        elbowPos = OuttakeConfig.elbowSpecimenPickupPos
        extendoPos = OuttakeConfig.extendoSpecimenPickupPos
    }

    fun armToNeutralAction() = ParallelAction(
        shoulderToNeutralAction(),
        elbowToNeutralAction(),
        wristToMidAction(),
        extendoToNeutralAction()
    )

    fun armToNeutralInstant() {
        shoulderPos = OuttakeConfig.shoulderNeutralPos
        elbowPos = OuttakeConfig.elbowNeutralPos
    }

    fun armToIntakeAction() = ParallelAction(
        shoulderToIntakeAction(),
        elbowToIntakeAction(),
        wristToMidAction(),
        extendoToIntakeAction()
    )

    fun armToBasketInstant() {
        shoulderPos = OuttakeConfig.shoulderBasketPos
        elbowPos = OuttakeConfig.elbowBasketPos
        wristPos = OuttakeConfig.wristMidPos
        extendoPos = OuttakeConfig.extendoBasketPos
    }

    fun armToBarInstant() {
        shoulderPos = OuttakeConfig.shoulderBarPos
        elbowPos = OuttakeConfig.elbowBarPos
        extendoPos = OuttakeConfig.extendoBarPos
    }

    fun armToBasketAction() = ParallelAction(
        shoulderToBasketAction(),
        elbowToBasketAction(),
        wristToMidAction(),
        extendoToBasketAction()
    )

    fun armToOldBarInstant() {
        shoulderPos = OuttakeConfig.shoulderOldBarPos
        elbowPos = OuttakeConfig.elbowOldBarPos
    }
}