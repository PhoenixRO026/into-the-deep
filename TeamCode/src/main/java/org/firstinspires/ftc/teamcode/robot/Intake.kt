package org.firstinspires.ftc.teamcode.robot

import android.graphics.Color
import android.view.WindowInsets.Side
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.ftc.Encoder
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.config.createColorSensorWithConfig
import org.firstinspires.ftc.teamcode.library.config.createEncoderUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createServoWithConfig
import org.firstinspires.ftc.teamcode.library.controller.PIDController
import org.firstinspires.ftc.teamcode.robot.config.IntakeHardwareConfig
import org.firstinspires.ftc.teamcode.robot.values.IntakeValues
import kotlin.math.abs
import kotlin.math.sign

class Intake(
    hardwareMap: HardwareMap,
    config: IntakeHardwareConfig,
    private val values: IntakeValues,
    private val timeKeep: TimeKeep
) {
    @Config
    data object IntakeConfig {
        @JvmField
        var extendoController: PIDController = PIDController(
            kP = 0.02,
            kD = 0.0,
            kI = 0.004
        )
        @JvmField
        var kF: Double = 0.0
    }

    enum class MODE {
        RUN_TO_POSITION,
        RAW_POWER
    }

    private var extendoMode = MODE.RAW_POWER

    private val motorExtendoIntake: DcMotorEx = hardwareMap.createMotorUsingConfig(config.motorExtendoIntake)
    private val motorSweeper: DcMotorEx = hardwareMap.createMotorUsingConfig(config.motorSweeper)
    private val servoIntakeTilt: Servo = hardwareMap.createServoWithConfig(config.servoIntakeTilt)
    private val servoBoxTilt: Servo = hardwareMap.createServoWithConfig(config.servoBoxTilt)
    private val encoderExtendo: Encoder = hardwareMap.createEncoderUsingConfig(config.encoderExtendo)
    val intakeColorSensor: ColorSensor = hardwareMap.createColorSensorWithConfig(config.intakeColorSensor)

    private var extendoOffset = 0

    val extendoPosition get() = encoderExtendo.getPositionAndVelocity().position - extendoOffset

    var extendoTargetPosition = extendoPosition
        set(value) {
            field = value
            extendoMode = MODE.RUN_TO_POSITION
        }

    fun readColor(hue : Float): String{
        if (hue in 120.0..160.0)
            return "None"
        if (hue in 5.0..35.0)
            return "Red"
        else if (hue in 205.0..235.0)
            return "Blue"
        else if (hue in 60.0..90.0)
            return "Yellow"
        else
            return "HAU BAU"
    }

    fun shouldStopIntake(side: String, hue: Float, onlyColored: Boolean): Boolean{
        var color: String = readColor(hue)
        if (intakeTiltCurrentPos in 0.55..0.58){//down
            if (side == "RED"){
                if (color == "Red")//////////////////red only
                    return true
                if (!onlyColored){
                    if (color == "Yellow")
                        return true
                }
                return false
            }
            if (side == "BLUE") {
                if (color == "Blue")//////////////////blue only
                    return true
                if (!onlyColored) {
                    if (color == "Yellow")
                        return true
                }
                return false
            }
            if (color == "None" || color == "HAU BAU")
                return false
        }
        else{/////////////////////////////////////////up
            if (color == "None" || color == "HAU BAU")
                return true
            else
                return false
        }
        return true
    }

    fun waitUntilRed() = Action {
        val hsvValues = floatArrayOf(0f, 0f, 0f)
        Color.colorToHSV(intakeColorSensor.argb(), hsvValues)
        shouldStopIntake("RED", hsvValues[0], false)
    }

    fun waitUntilNoneRed() = Action {
        val hsvValues = floatArrayOf(0f, 0f, 0f)
        Color.colorToHSV(intakeColorSensor.argb(), hsvValues)
        !shouldStopIntake("RED", hsvValues[0], false)
    }

    fun snatchRedSample() = SequentialAction(
        InstantAction{
            extendoTargetPosition = values.extendoLim
            sweeperPower = 1.0
            intakeDown()
                     },
        /*SequentialAction(
            extendoToPosAction(values.extendoLim),
            InstantAction{
                sweeperPower=1.0
                intakeTiltCurrentPos = values.intakeDownPos
                         },
        ),*/
        waitUntilRed(),
        InstantAction{
            sweeperPower = 0.0
            intakeUp()
        },
        extendoToPosAction(values.extendoInBot)
    )

    fun snatchSpecimen(hsvValues: FloatArray) {
        extendoTargetPosition = values.extendoLim
        while (extendoPosition <= 700){
            if (shouldStopIntake("RED", hsvValues[0], false)) {
                sweeperPower = 0.0
            } else {
                sweeperPower = 1.0
            }
            if (readColor(hsvValues[0])=="Yellow"){
                sweeperPower=0.0
                intakeDown()
                break;
            }
            extendoPower = 0.8
            Color.colorToHSV(intakeColorSensor.argb(), hsvValues)
        }
        sweeperPower = 0.0
        extendoTargetPosition = values.extendoInBot
    }

    fun moveSample() = SequentialAction(
        InstantAction{
            sweeperPower = 1.0
        },
        waitUntilNoneRed(),
        InstantAction{
            sweeperPower = 0.0
        }
    )

    fun kickSample(hsvValues: FloatArray){// have to change for specimen auto
        //intakeUp()
        while (!shouldStopIntake("RED", hsvValues[0], false)){
            if(shouldStopIntake("RED", hsvValues[0], false))
                sweeperPower = 0.0
            else
                sweeperPower = 0.65
            Color.colorToHSV(intakeColorSensor.argb(), hsvValues)
        }
        sweeperPower = 0.0
    }

    fun intakeDown() {
        intakeTiltCurrentPos = values.intakeDownPos
    }

    fun intakeUp() {
    intakeTiltCurrentPos = values.intakeUpPos
    }

    fun boxDown() {
    boxTiltCurrentPos = 0.5633
    }

    fun boxUp() {
    boxTiltCurrentPos = 0.172
    }

    fun extendoToPosAction(pos: Int) = object : Action {
        var init = true

        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                extendoTargetPosition = pos
            }
            return abs(extendoTargetPosition - extendoPosition) > 20
        }
    }

    private var _extendoPower
    get() = motorExtendoIntake.power
    set(value) {
        motorExtendoIntake.power = value
    }

    var extendoPower
        get() = _extendoPower
        set(value) {
            if (extendoMode == MODE.RUN_TO_POSITION && value == 0.0) return
            val newPower = if (extendoPosition >= values.extendoLim) value.coerceAtMost(0.0) else value
            _extendoPower = newPower
            extendoMode = MODE.RAW_POWER
        }

    var sweeperPower by motorSweeper::power

    var boxTiltCurrentPos
        get() = servoBoxTilt.position
        set(value) {
            servoBoxTilt.position = value
            _boxTiltTargetPos = servoBoxTilt.position
            boxTiltSpeed = 0.0
        }

    private var _boxTiltTargetPos = servoBoxTilt.position
        set(value) {
            field = value.coerceIn(0.0, 1.0)
        }

    var boxTiltTargetPos
        get() = _boxTiltTargetPos
        set(value) {
            _boxTiltTargetPos = value
            boxTiltSpeed = 0.0
        }

    var boxTiltSpeed: Double = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    var intakeTiltCurrentPos
        get() = servoIntakeTilt.position
        set(value) {
            servoIntakeTilt.position = value
            _intakeTiltTargetPos = servoIntakeTilt.position
            intakeTiltSpeed = 0.0
        }

    private var _intakeTiltTargetPos = servoIntakeTilt.position
        set(value) {
            field = value.coerceIn(0.0, 1.0)
        }

    var intakeTiltTargetPos
        get() = _intakeTiltTargetPos
        set(value) {
            _intakeTiltTargetPos = value
            intakeTiltSpeed = 0.0
        }

    var intakeTiltSpeed: Double = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    fun resetExtendoPosition() {
        extendoOffset = encoderExtendo.getPositionAndVelocity().position
    }

    fun update() {
        moveBoxTilt()
        moveIntakeTilt()
        if (extendoMode == MODE.RUN_TO_POSITION)
            _extendoPower = IntakeConfig.extendoController.calculate(extendoPosition.toDouble(), extendoTargetPosition.toDouble(), timeKeep.deltaTime) + IntakeConfig.kF
    }

    private fun moveBoxTilt() {
        if (boxTiltSpeed != 0.0) {
            servoBoxTilt.position += timeKeep.deltaTime / values.boxTiltMaxTravelDuration * boxTiltSpeed
            _boxTiltTargetPos = boxTiltCurrentPos
            return
        }
        val error = boxTiltCurrentPos - boxTiltTargetPos
        if (error == 0.0) return
        val step = timeKeep.deltaTime / values.boxTiltMaxTravelDuration
        if (abs(error) < step) {
            servoBoxTilt.position = boxTiltTargetPos
        } else {
            servoBoxTilt.position += error.sign * step
        }
    }

    private fun moveIntakeTilt() {
        if (intakeTiltSpeed != 0.0) {
            servoIntakeTilt.position += timeKeep.deltaTime / values.intakeTiltMaxTravelDuration * intakeTiltSpeed
            _intakeTiltTargetPos = intakeTiltCurrentPos
            return
        }
        val error = intakeTiltCurrentPos - intakeTiltTargetPos
        if (error == 0.0) return
        val step = timeKeep.deltaTime / values.intakeTiltMaxTravelDuration
        if (abs(error) < step) {
            servoIntakeTilt.position = intakeTiltTargetPos
        } else {
            servoIntakeTilt.position += error.sign * step
        }
    }

}