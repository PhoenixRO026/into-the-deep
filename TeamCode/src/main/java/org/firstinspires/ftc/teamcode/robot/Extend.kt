package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.SequentialAction
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.lib.units.SleepAction
import org.firstinspires.ftc.teamcode.lib.units.s
import kotlin.math.abs

class Extend(hardwareMap: HardwareMap) {
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
    //val leftExtendMotor: DcMotor = hardwareMap.get(DcMotor::class.java, "leftExtend")
    //val rightExtendMotor: DcMotor = hardwareMap.get(DcMotor::class.java, "rightExtend")


    enum class Mode {
        POWER,
        TARGET
    }

    private var mode = Mode.POWER
    private var targetPositionTicks = 0

    init {
         //rightLiftMotor.direction = DcMotorSimple.Direction.REVERSE
        //rightExtendMotor.direction = DcMotorSimple.Direction.REVERSE

        leftLiftMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        rightLiftMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        //leftExtendMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        //rightExtendMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        leftLiftMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rightLiftMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        //leftExtendMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        //rightExtendMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        leftLiftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rightLiftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        //leftExtendMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        //rightExtendMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
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
    /*
    fun extendGoToPos(newPos: Int) = SequentialAction(
        object: Action {
            var init = true
            override fun run(p: TelemetryPacket): Boolean {
                if (init) {
                    init = false

                    targetPositionTicks = newPos

                    mode = Mode.TARGET
                }

                return busyExtend
            }
        },
        SleepAction(0.1.s)
    )
    */
    //val busyExtend get() = abs(leftExtendMotor.currentPosition - targetPositionTicks) > toleranceTicks
    val busyLift get() = abs(leftLiftMotor.currentPosition - targetPositionTicks) > toleranceTicks

    var powerLift : Number
        get() = leftLiftMotor.power
        set(value){
            if (mode != Mode.POWER) return

            leftLiftMotor.power = value.toDouble()
            rightLiftMotor.power = value.toDouble()
        }
    /*
    var powerExtend : Number
        get() = leftExtendMotor.power
        set(value){
            if(mode != Mode.POWER)  return

            leftExtendMotor.power = value.toDouble()
            rightExtendMotor.power = value.toDouble()
        }
    */
    fun updateLift() {
        val feedback = controller.calculate(leftLiftMotor.currentPosition.toDouble(), targetPositionTicks.toDouble()) + kF

        if (mode == Mode.TARGET) {
            leftLiftMotor.power = feedback
            rightLiftMotor.power = feedback
        }
    }
    /*
    fun ubdateExtend() {
        val feedback = controller.calculate(leftExtendMotor.currentPosition.toDouble(), targetPositionTicks.toDouble()) + kF

        if(mode == Mode.TARGET) {
            leftExtendMotor.power = feedback
            rightExtendMotor.power = feedback
        }
    }

     */
}