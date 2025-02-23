package org.firstinspires.ftc.teamcode.robot

import android.graphics.Color
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ftc.Encoder
import com.lib.units.Duration
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
        @JvmField var tiltTeleInit = 0.5106
    }

    enum class Mode {
        PID,
        RAW_POWER
    }

    var sensorHue: Float = 0f

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
}