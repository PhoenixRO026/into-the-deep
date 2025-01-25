package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ftc.Encoder
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.config.createEncoderUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig
import org.firstinspires.ftc.teamcode.library.controller.PIDController
import org.firstinspires.ftc.teamcode.robot.config.LiftHardwareConfig
import org.firstinspires.ftc.teamcode.robot.values.LiftValues
import kotlin.math.abs

class Lift(
    hardwareMap: HardwareMap,
    config: LiftHardwareConfig,
    private val values: LiftValues,
    timeKeep: TimeKeep
) {
    @Config
    data object LiftConfig {
        @JvmField
        var liftController: PIDController? = null
    }

    enum class MODE {
        RUN_TO_POSITION,
        RAW_POWER
    }

    init {
        LiftConfig.liftController = PIDController(
            kP = 0.0155,
            kD = 0.0005,
            kI = 0.0035,
            timeKeep = timeKeep
        )
    }

    private var currentMode = MODE.RAW_POWER
    
    val motorLiftRight: DcMotorEx = hardwareMap.createMotorUsingConfig(config.motorLiftRight)
    val motorLiftLeft: DcMotorEx = hardwareMap.createMotorUsingConfig(config.motorLiftLeft)
    private val encoder: Encoder = hardwareMap.createEncoderUsingConfig(config.encoder)

    private var offset = 0

    val position get() = encoder.getPositionAndVelocity().position - offset
    var targetPosition = position
        set(value) {
            field = value
            currentMode = MODE.RUN_TO_POSITION
        }

    fun liftToPosAction(pos: Int) = object : Action {
        var init = true

        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                targetPosition = pos
            }

            return abs(targetPosition - position) > 20
        }

    }

    private var _power
        get() = motorLiftRight.power
        set(value) {
            motorLiftRight.power = value
            motorLiftLeft.power = value
        }

    var power
        get() = _power
        set(value) {
            _power = value
            currentMode = MODE.RAW_POWER
        }

    fun resetPosition() {
        offset = encoder.getPositionAndVelocity().position
    }
    
    fun update() {
        if (currentMode == MODE.RUN_TO_POSITION)
            _power = LiftConfig.liftController?.calculate(position.toDouble(), targetPosition.toDouble()) ?: 0.0
    }
}