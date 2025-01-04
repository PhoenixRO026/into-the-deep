package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.SequentialAction
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.lib.units.SleepAction
import org.firstinspires.ftc.teamcode.lib.units.s
import kotlin.math.abs

class Lift (hardwareMap: HardwareMap) {
    companion object {

        val controller = PIDController(
            kP = 0.008,
            kI = 0.005,
            kD = 0.0008
        )
        val toleranceTicks = 16
        val kF = 0.16
    }
    val leftLiftMotor: DcMotor = hardwareMap.get(DcMotor::class.java, "leftLift")
    val rightLiftMotor: DcMotor = hardwareMap.get(DcMotor::class.java, "rightLift")

    enum class Mode {
        POWER,
        TARGET
    }

    private var mode = Mode.POWER
    private var targetPositionTicks = 0

    init {
        leftLiftMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        rightLiftMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        leftLiftMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rightLiftMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        leftLiftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rightLiftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    fun liftGoToPos(newPos: Int) = SequentialAction(
        object: Action {
            var init = true
            override fun run(p: TelemetryPacket): Boolean {
                if (init) {
                    init = false

                    targetPositionTicks = newPos

                    mode = Mode.TARGET
                }

                return busyLift
            }
        },
        SleepAction(0.1.s)
    )

    val busyLift get() = abs(leftLiftMotor.currentPosition - targetPositionTicks) > Extend.toleranceTicks

    var powerLift : Number
        get() = leftLiftMotor.power
        set(value){
            if (mode != Mode.POWER) return

            leftLiftMotor.power = value.toDouble()
            rightLiftMotor.power = value.toDouble()
        }
    fun updateLift() {
        val feedback = org.firstinspires.ftc.teamcode.robot.Extend.controller.calculate(leftLiftMotor.currentPosition.toDouble(), targetPositionTicks.toDouble()) + org.firstinspires.ftc.teamcode.robot.Extend.kF

        if (mode == Mode.TARGET) {
            leftLiftMotor.power = feedback
            rightLiftMotor.power = feedback
        }
    }
}