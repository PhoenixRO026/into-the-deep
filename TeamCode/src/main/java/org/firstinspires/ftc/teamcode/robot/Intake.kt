package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.lib.units.SleepAction
import org.firstinspires.ftc.teamcode.lib.units.s
import org.firstinspires.ftc.teamcode.robot.Lift.Mode
import kotlin.math.abs

class Intake(hardwareMap: HardwareMap) {
    companion object{
        const val intakeUp = 0.0 //
        const val intakeDown = 0.5 //

        const val boxUp = 0.5
        const val boxDown = 0.74 //
        const val boxMid=0.5 // no more

        const val extendInrobot = 0.5
        const val extendOutrobot = 0.5

        val tiltWait = 0.8
        val boxWait = 0.5
        val extendWait = 0.5

        val controller = PIDController(
            kP = 0.008,
            kI = 0.005,
            kD = 0.0008
        )
        val toleranceTicks = 16
        val kF = 0.16
    }

    private val tiltIntakeServo = hardwareMap.get(Servo::class.java, "servoIntakeTilt")
    private val tiltBoxServo = hardwareMap.get(Servo::class.java, "servoBoxTilt")
    private val extendIntakeMotor : DcMotor = hardwareMap.get(DcMotor::class.java, "motorExtendoIntake")
    private val powerIntakeMotor : DcMotor = hardwareMap.get(DcMotor::class.java, "motorSweeper")

    enum class Mode {
        POWER,
        TARGET
    }

    private var mode = Mode.POWER
    private var targetPositionTicks = 0

    init {
        tiltBoxServo.position = 0.5
        tiltIntakeServo.position = 0.5

        extendIntakeMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        powerIntakeMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        extendIntakeMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        powerIntakeMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        extendIntakeMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        powerIntakeMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    fun ExtendGoToPos(newPos: Int) = SequentialAction(
        object: Action {
            var init = true
            override fun run(p: TelemetryPacket): Boolean {
                if (init) {
                    init = false
5

                    targetPositionTicks = newPos

                    mode = Mode.TARGET
                }

                return busyExtend
            }
        },
        SleepAction(0.1.s)
    )

    val busyExtend get() = abs(extendIntakeMotor.currentPosition - targetPositionTicks) > toleranceTicks

    var extend : Double = 0.0
        get() = extendIntakeMotor.power
        set(value) {
            val clippedValue = value.coerceIn(0.0, 1.0)
            extendIntakeMotor.power = value.toDouble()
            field = clippedValue
        }

    fun updateExtend() {
        val feedback = controller.calculate(extendIntakeMotor.currentPosition.toDouble(), targetPositionTicks.toDouble()) + kF

        if (mode == Mode.TARGET) {
            extendIntakeMotor.power = feedback
            extendIntakeMotor.power = feedback
        }
    }

    var power : Double
        get() = powerIntakeMotor.power
        set(value) {
            if (mode != Mode.POWER) return
            powerIntakeMotor.power = value.toDouble()
        }

    fun powerIntake(newPower : Double) = SequentialAction(
        InstantAction {power = newPower}
    )

    var tiltIntake : Double = intakeUp
        set(value){
            val clippedValue = value.coerceIn(0.0, 1.0)
            tiltIntakeServo.position = clippedValue
            field = clippedValue
        }

    fun tiltToPosIntake(newTilt : Double) = SequentialAction(
        InstantAction { tiltIntake = newTilt},
        SleepAction(tiltWait)
    )

    fun tiltIntakeUp() = tiltToPosIntake(intakeUp)

    fun tiltIntakeDown() = tiltToPosIntake(intakeDown)

    var tiltBox : Double = boxUp
        set(value){
            val clippedValue = value.coerceIn(0.0, 1.0)
            if (clippedValue == tiltBoxServo.position)
                return

            tiltBoxServo.position = clippedValue
            field = clippedValue
        }


    // TODO condition somehow the sequential action to execute only if newTilt != tilBox,
    //  to avoid unnecessary Sleeps
    fun tiltToPosBox(newTilt : Double) = SequentialAction(
        InstantAction { tiltBox = newTilt},
        SleepAction(boxWait)
    )

    fun tiltBoxUp() = tiltToPosBox(boxUp)

    fun tiltBoxMid() = tiltToPosBox(boxMid)

    fun tiltBoxDown() = tiltToPosBox(boxDown)
}