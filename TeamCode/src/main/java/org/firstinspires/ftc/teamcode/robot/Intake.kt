package org.firstinspires.ftc.teamcode.robot

import android.graphics.Color
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.ftc.Encoder
import com.lib.units.Duration
import com.lib.units.SleepAction
import com.lib.units.s
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import com.qualcomm.robotcore.hardware.Servo
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
            kD = 0.0,
            kI = 0.004
        )
        @JvmField var targetPosTolerance = 10
        @JvmField var extendoLim = 600

        @JvmField var titlActionSleepDuration = 1.s

        @JvmField var extendoMax = 600
        @JvmField var extendoIn = 0

        @JvmField var tiltUp = 0.04
        @JvmField var tiltDown = 0.49

        @JvmField var tiltTeleInit = 0.5106
        @JvmField var tiltAutoInit = tiltUp
    }

    enum class SensorColor {
        YELLOW,
        BLUE,
        RED,
        NONE
    }

    enum class Mode {
        PID,
        RAW_POWER
    }

    var sensorHue: Float = 0f

    val sensorColor get() = when {
        sensorHue in 5f..35f -> SensorColor.RED
        sensorHue in 205f..235f -> SensorColor.BLUE
        sensorHue in 60f..90f -> SensorColor.YELLOW
        else -> SensorColor.NONE
    }

    fun updateHue() {
        val normalizedColors = colorSensor.normalizedColors
        val hsv = floatArrayOf(0f, 0f, 0f)
        Color.RGBToHSV(
            (normalizedColors.red * 256).toInt(),
            (normalizedColors.green * 256).toInt(),
            (normalizedColors.blue * 256).toInt(),
            hsv
        )
        sensorHue = hsv[0]
    }

    private var extendoOffset = extendoEncoder.getPositionAndVelocity().position

    private var extendoMode = Mode.RAW_POWER

    val extendoPosition = extendoEncoder.getPositionAndVelocity().position - extendoOffset

    var extendoTargetPosition = extendoPosition
        set(value) {
            field = value
            extendoMode = Mode.PID
        }

    private var _extendoPower by extendoMotor::power

    var extendoPower
        get() = _extendoPower
        set(value) {
            if (extendoMode == Mode.PID && value == 0.0) return
            _extendoPower = if (extendoPosition >= IntakeConfig.extendoLim) value.coerceAtMost(0.0) else value
            extendoMode = Mode.RAW_POWER
        }

    var sweeperPower by sweeperMotor::power

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
        if (extendoMode == Mode.PID)
            _extendoPower = IntakeConfig.controller.calculate(extendoPosition.toDouble(), extendoTargetPosition.toDouble(), deltaTime)
    }

    fun extendoToPosAction(pos: Int) = object : Action {
        var init = true

        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                extendoTargetPosition = pos
            }

            return abs(extendoTargetPosition - extendoPosition) > IntakeConfig.targetPosTolerance
        }

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

    fun waitForColorAction(waitColor: SensorColor) = Action {
        updateHue()
        sensorColor != waitColor
    }
}