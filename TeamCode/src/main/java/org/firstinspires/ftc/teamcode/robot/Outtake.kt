package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.SequentialAction
import com.lib.units.Duration
import com.lib.units.SleepAction
import com.lib.units.s
import com.qualcomm.robotcore.hardware.Servo
import kotlin.math.abs

class Outtake(
    val shoulderLeftServo: Servo,
    val shoulderRightServo: Servo,
    val elbowServo: Servo,
    val wristServo: Servo,
    val clawServo: Servo
) {
    @Config
    data object OuttakeConfig {

        @JvmField var shoulderActionSleepDuration = 2.s
        @JvmField var elbowActionSleepDuration = 2.s
        @JvmField var wristActionSleepDuration = 2.s
        @JvmField var clawActionSleepDuration = 0.1.s
        @JvmField var extendoActionSleepDuration = 2.s

        @JvmField var shoulderNeutralPos = 0.5233
        @JvmField var elbowNeutralPos = 1.0
        @JvmField var extendoNeutralPos = 0.27
        @JvmField var wristMidPos = 0.5239
        @JvmField var wristUpsideDown = 0.0
        @JvmField var clawOpenPos = 1.0
        @JvmField var clawClosedPos = 0.0

        @JvmField var shoulderTeleInit = shoulderNeutralPos
        @JvmField var elbowTeleInit = elbowNeutralPos
        @JvmField var extendoTeleInit = extendoNeutralPos
        @JvmField var wristTeleInit = wristMidPos
        @JvmField var clawTeleInit = clawClosedPos

        @JvmField var shoulderAutoInit = 0.5228
        @JvmField var elbowAutoInit = 0.939
        @JvmField var wristAutoInit = wristMidPos
        @JvmField var extendoAutoInit = extendoNeutralPos
        @JvmField var clawAutoInit = clawClosedPos

        @JvmField var shoulderIntakePos = 0.7933
        @JvmField var elbowIntakePos = 0.7256
        @JvmField var extendoIntakePos = extendoNeutralPos

        @JvmField var shoulderBasketPos = 0.3678
        @JvmField var elbowBasketPos = 0.2472
        @JvmField var extendoBasketPos = extendoNeutralPos

        @JvmField var shoulderSpecimenPickupPos = 0.0889
        @JvmField var elbowSpecimenPickupPos = 0.7239
        @JvmField var extendoSpecimenPickupPos = extendoNeutralPos

        @JvmField var shoulderBarPos = 0.7011
        @JvmField var elbowBarPos = 0.0356
        @JvmField var extendoBarPos = 0.0517

        @JvmField var shoulderOldBarPos = 0.8694
        @JvmField var elbowOldBarPos = 0.894

        @JvmField var extendoMax = extendoBarPos
    }

    var shoulderPos
        get() = shoulderLeftServo.position
        set(value) {
            shoulderLeftServo.position = value
            shoulderRightServo.position = value
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

    var clawPos: Double = 0.5
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
    }

    fun initAuto() {
        shoulderPos = OuttakeConfig.shoulderAutoInit
        elbowPos = OuttakeConfig.elbowAutoInit
        clawPos =  OuttakeConfig.clawAutoInit
        wristPos = OuttakeConfig.wristAutoInit
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

    fun openClawAction() = clawToPosAction(OuttakeConfig.clawOpenPos)
    fun closeClawAction() = clawToPosAction(OuttakeConfig.clawClosedPos)

    fun wristToMidAction() = wristToPosAction(OuttakeConfig.wristMidPos)
    fun wristToMidInstant() {
        wristPos = OuttakeConfig.wristMidPos
    }
    fun wristToUpsideDownInstant() {
        wristPos = OuttakeConfig.wristUpsideDown
    }

    fun wristToUpsideDownAction() = wristToPosAction(OuttakeConfig.wristUpsideDown)

    fun shoulderToNeutralAction() = shoulderToPosAction(OuttakeConfig.shoulderNeutralPos)
    fun elbowToNeutralAction() = elbowToPosAction(OuttakeConfig.elbowNeutralPos)

    fun shoulderToIntakeAction() = shoulderToPosAction(OuttakeConfig.shoulderIntakePos)
    fun elbowToIntakeAction() = elbowToPosAction(OuttakeConfig.elbowIntakePos)

    fun shoulderToBarAction() = shoulderToPosAction(OuttakeConfig.shoulderBarPos)
    fun elbowToBarAction() = elbowToPosAction(OuttakeConfig.elbowBarPos)

    fun shoulderToBasketAction() = shoulderToPosAction(OuttakeConfig.shoulderBasketPos)
    fun elbowToBasketAction() = elbowToPosAction(OuttakeConfig.elbowBasketPos)

    fun shoulderToSpecimenPickupAction() = shoulderToPosAction(OuttakeConfig.shoulderSpecimenPickupPos)
    fun elbowToSpecimenPickupAction() = elbowToPosAction(OuttakeConfig.elbowSpecimenPickupPos)
    fun wristToSpecimenPickupAction() = wristToPosAction(OuttakeConfig.wristUpsideDown)


    fun armToSpecimenInstant() {
        shoulderPos = OuttakeConfig.shoulderSpecimenPickupPos
        elbowPos = OuttakeConfig.elbowSpecimenPickupPos
    }

    fun armToSpecimenAction() = ParallelAction(
        shoulderToSpecimenPickupAction(),
        elbowToSpecimenPickupAction(),
        wristToSpecimenPickupAction(),
        openClawAction()
    )

    fun armToNeutralAction() = ParallelAction(
        shoulderToNeutralAction(),
        elbowToNeutralAction(),
        wristToMidAction()
    )

    fun armToNeutralInstant() {
        shoulderPos = OuttakeConfig.shoulderNeutralPos
        elbowPos = OuttakeConfig.elbowNeutralPos
    }

    fun armToIntakeAction() = ParallelAction(
        shoulderToIntakeAction(),
        elbowToIntakeAction(),
        wristToMidAction()
    )

    fun armToBasketInstant() {
        shoulderPos = OuttakeConfig.shoulderBasketPos
        elbowPos = OuttakeConfig.elbowBasketPos
        wristPos = OuttakeConfig.wristMidPos
    }

    fun armToBarInstant() {
        shoulderPos = OuttakeConfig.shoulderBarPos
        elbowPos = OuttakeConfig.elbowBarPos
    }

    fun armToBarAction() = ParallelAction(
        wristToMidAction(),
        shoulderToBarAction(),
        elbowToBarAction(),
    )

    fun armToBasketAction() = ParallelAction(
        shoulderToBasketAction(),
        elbowToBasketAction(),
        wristToMidAction()
    )

    fun armToOldBarInstant() {
        shoulderPos = OuttakeConfig.shoulderOldBarPos
        elbowPos = OuttakeConfig.elbowOldBarPos
    }
}