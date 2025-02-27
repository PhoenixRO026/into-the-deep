package org.firstinspires.ftc.teamcode.robot

import android.graphics.Color
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.RaceAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.ftc.Encoder
import com.lib.units.Duration
import com.lib.units.SleepAction
import com.lib.units.ms
import com.lib.units.s
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.library.controller.PIDController
import kotlin.math.abs

class Intake(
    val extendoMotor: DcMotorEx,
    val sweeperMotor: DcMotorEx,
    val extendoEncoder: Encoder,
    val tiltServo: Servo,
    val colorSensor: NormalizedColorSensor
) {
    @Config
    data object IntakeConfig {
        @JvmField
        var controller = PIDController(
            kP = 0.02,
            kD = 0.0006,
            kI = 0.00,
            stabilityThreshold = 0.2
        )
        @JvmField var targetPosTolerance = 10
        @JvmField var extendoLim = 600

        @JvmField var titlActionSleepDuration = 1.s

        @JvmField var extendoMax = 600
        @JvmField var extendoIn = 2

        @JvmField var tiltUp = 0.1567
        @JvmField var tiltDown = 0.595

        @JvmField var tiltTeleInit = tiltUp
        @JvmField var tiltAutoInit = tiltUp

        @JvmField var extendoLeftRedSample = 600
        @JvmField var extendoMiddleRedSample = 600
        @JvmField var extendoRightRedSample = 600

        @JvmField var sampleToBoxPower = 1.0
    }

    enum class SensorColor {
        YELLOW,
        BLUE,
        RED,
        NONE
    }

    enum class Mode {
        PID,
        RAW_POWER,
        ACTION
    }

    var sensorHue: Float = 0f

    var hsv = floatArrayOf(0f, 0f, 0f)

    val sensorColor get() = when {
        hsv[1] != 0f && sensorHue in 0f..40f -> SensorColor.RED
        hsv[1] != 0f && sensorHue in 200f..280f -> SensorColor.BLUE
        hsv[1] != 0f && sensorHue in 40f..110f -> SensorColor.YELLOW
        else -> SensorColor.NONE
    }

    fun updateHue() {
        val normalizedColors = colorSensor.normalizedColors
        Color.RGBToHSV(
            (normalizedColors.red * 256).toInt(),
            (normalizedColors.green * 256).toInt(),
            (normalizedColors.blue * 256).toInt(),
            hsv
        )
        sensorHue = hsv[0]
    }

    private var extendoOffset = extendoEncoder.getPositionAndVelocity().position

    var extendoMode = Mode.RAW_POWER

    val extendoPosition get() = extendoEncoder.getPositionAndVelocity().position - extendoOffset

    var extendoTargetPosition = extendoPosition
        set(value) {
            field = value
            extendoMode = Mode.PID
        }

    private var _extendoPower by extendoMotor::power

    var extendoPower
        get() = _extendoPower
        set(value) {
            if (extendoMode != Mode.RAW_POWER && value == 0.0) return
            if (value == 0.0 && extendoPosition < IntakeConfig.extendoIn + IntakeConfig.targetPosTolerance) {
                extendoTargetPosition = IntakeConfig.extendoIn
                return
            } else {
                _extendoPower = if (extendoPosition >= IntakeConfig.extendoLim) value.coerceAtMost(0.0) else value
            }
            extendoMode = Mode.RAW_POWER
        }

    var sweeperPower: Double = 0.0
        get() = sweeperMotor.power
        set(value) {
            val clampedVal = value.coerceIn(-1.0, 1.0)
            if (clampedVal == field && clampedVal == 0.0) return
            field = clampedVal
            sweeperMotor.power = field
        }

    var tiltPosition by tiltServo::position

    fun resetExtendoPosition() {
        extendoOffset = extendoEncoder.getPositionAndVelocity().position
    }

    fun initTeleop() {
        tiltPosition = IntakeConfig.tiltTeleInit
    }

    fun initAuto() {
        tiltPosition = IntakeConfig.tiltAutoInit
    }

    fun update(deltaTime: Duration) {
        /*if (extendoMode == Mode.PID && abs(extendoTargetPosition - extendoPosition) <= IntakeConfig.targetPosTolerance) {
            extendoMode = Mode.RAW_POWER
            extendoPower = 0.0
        }*/
        if (extendoMode != Mode.RAW_POWER)
            _extendoPower = IntakeConfig.controller.calculate(extendoPosition.toDouble(), extendoTargetPosition.toDouble(), deltaTime)
    }

    fun extendoToPosAction(pos: Int) = object : Action {
        var init = true

        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                extendoTargetPosition = pos
                extendoMode = Mode.ACTION
            }
            p.addLine("waiting for extendo")
            return abs(extendoTargetPosition - extendoPosition) > IntakeConfig.targetPosTolerance
            /*return if (abs(extendoTargetPosition - extendoPosition) > IntakeConfig.targetPosTolerance) {
                true
            } else {
                extendoMode = Mode.RAW_POWER
                extendoPower = 0.0
                false
            }*/
        }

    }

    fun addTelemetry(telemetry: Telemetry) {
        telemetry.addData("sensor hue", sensorHue)
        telemetry.addData("sensor color", sensorColor)
        telemetry.addData("sweeper power", sweeperPower)
        telemetry.addData("extendo power", extendoPower)
        telemetry.addData("extendoPos", extendoPosition)
        //telemetry.addData("sweeper current", sweeperMotor.getCurrent(CurrentUnit.AMPS))
        //telemetry.addData("extendo current", extendoMotor.getCurrent(CurrentUnit.AMPS))
    }

    fun sweeperOnAction() = InstantAction{ sweeperMotor.power = 1.0 }
    fun sweeperOffAction() = InstantAction{ sweeperMotor.power = 0.0 }
    fun sweeperSpewAction() = InstantAction{ sweeperMotor.power = -1.0 }
    fun sweeperBoxAction() = InstantAction { sweeperMotor.power = IntakeConfig.sampleToBoxPower }

    fun extendoMaxInstant() {
        extendoTargetPosition = IntakeConfig.extendoMax
    }

    fun extendoInInstant() {
        extendoTargetPosition = IntakeConfig.extendoIn
    }

    fun extendoMaxAction() = extendoToPosAction(IntakeConfig.extendoMax)
    fun extendoInAction() = extendoToPosAction(IntakeConfig.extendoIn)

    fun tiltToPosAction(pos: Double): Action {
        val sleepDuration = IntakeConfig.titlActionSleepDuration * abs(pos - tiltPosition)
        return SequentialAction(
            InstantAction { tiltPosition = pos },
            SleepAction(sleepDuration)
        )
    }

    fun tiltUpAction() = tiltToPosAction(IntakeConfig.tiltUp)
    fun tiltDownAction() = tiltToPosAction(IntakeConfig.tiltDown)

    fun tiltUpInstant() {
        tiltPosition = IntakeConfig.tiltUp
    }
    fun tiltDownInstant() {
        tiltPosition = IntakeConfig.tiltDown
    }

    fun waitForColorAction(waitColor: SensorColor, maxTime: Duration = 1.s) = RaceAction(
        Action {
            updateHue()
            it.addLine("Waiting for $waitColor")
            sensorColor != waitColor
        },
        SleepAction(maxTime)
    )

    fun extendReadyForSampling() = ParallelAction(
        extendoMaxAction(),
        tiltUpAction()
    )

    fun takeOutSample() = SequentialAction(
        sweeperBoxAction(),
        waitForColorAction(SensorColor.NONE),
        sweeperOffAction()
    )

    fun takeSample(color: SensorColor) = SequentialAction(
        ParallelAction(
            waitForColorAction(color),
            sweeperOnAction(),
            InstantAction {
                tiltDownInstant()
            }
        ),
        sweeperOffAction()
    )

    fun kickSample() = SequentialAction(
        ParallelAction(
            sweeperSpewAction(),
            waitForColorAction(SensorColor.NONE)
        ),
        sweeperOffAction()
    )

    fun waitFor2Colors(firstColor: SensorColor, secondColor: SensorColor) = RaceAction(
        waitForColorAction(firstColor),
        waitForColorAction(secondColor)
    )

    fun bringSampleToIntake() = SequentialAction(
        ParallelAction(
            tiltUpAction(),
            extendoInAction()
        ),
        takeOutSample()
    )

    fun takeSampleSequenceAction(color: SensorColor) = SequentialAction(
        takeSample(color),
        ParallelAction(
            tiltUpAction(),
            extendoInAction()
        )
    )

    fun extendoToLeftRedSampleAction() = extendoToPosAction(IntakeConfig.extendoLeftRedSample)
    fun extendoToMiddleRedSampleAction() = extendoToPosAction(IntakeConfig.extendoMiddleRedSample)
    fun extendoToRightRedSampleAction() = extendoToPosAction(IntakeConfig.extendoRightRedSample)
}