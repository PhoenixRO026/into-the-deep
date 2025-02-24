package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ftc.Encoder
import com.lib.units.Duration
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.library.controller.PIDController
import kotlin.math.abs

class Lift(
    val leftMotor: DcMotorEx,
    val rightMotor: DcMotorEx,
    val encoder: Encoder
) {
    @Config
    data object LiftConfig {
        @JvmField
        var controller = PIDController(
            kP = 0.01,
            kD = 0.0005,
            kI = 0.009
        )
        @JvmField
        var kF: Double = 0.18
        @JvmField
        var targetPosTolerance = 10

        @JvmField var basketPos = 2980
        @JvmField var intakePos = 422
        @JvmField var intakeWaitingPos = 568
    }

    enum class Mode {
        PID,
        RAW_POWER
    }

    private var currentMode = Mode.RAW_POWER
    private var offset = encoder.getPositionAndVelocity().position

    val position get() = encoder.getPositionAndVelocity().position - offset

    private var _power
        get() = rightMotor.power
        set(value) {
            rightMotor.power = value
            leftMotor.power = value
        }

    var power
        get() = _power
        set(value) {
            if (currentMode == Mode.PID && value == 0.0) return

            _power = if (value < 0.0) value else value + LiftConfig.kF
            currentMode = Mode.RAW_POWER
        }

    var targetPosition = position
        set(value) {
            field = value
            currentMode = Mode.PID
        }


    fun resetPosition() {
        offset = encoder.getPositionAndVelocity().position
    }

    fun update(deltaTime: Duration) {
        if (currentMode == Mode.PID)
            _power = LiftConfig.controller.calculate(
                position.toDouble(),
                targetPosition.toDouble(),
                deltaTime
            )
    }

    fun liftToPosAction(pos: Int) = object : Action {
        var init = true
        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                targetPosition = pos
            }
            return abs(targetPosition - position) > LiftConfig.targetPosTolerance
        }
    }

    fun liftToBasketAction() = liftToPosAction(LiftConfig.basketPos)
    fun liftToIntakeAction() = liftToPosAction(LiftConfig.intakePos)
    fun liftToIntakeWaitingAction() = liftToPosAction(LiftConfig.intakeWaitingPos)
    fun liftDownAction() = liftToPosAction(0)
}