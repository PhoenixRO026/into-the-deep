package org.firstinspires.ftc.teamcode.library

import com.lib.units.rev
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import java.util.LinkedList
import kotlin.math.abs

class AnalogEncoderServo(
    hardwareMap: HardwareMap,
    inputName: String,
    servoName: String,
    private val direction: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
) {
    private val analogInput = hardwareMap.get(AnalogInput::class.java, inputName)
    private val servo = hardwareMap.get(CRServo::class.java, servoName)

    init {
        servo.direction = direction
    }

    private var init = true

    private var offset = 0.0

    private var rotCount = 0

    private val voltageQueue = LinkedList<Double>()

    val voltage get() = analogInput.voltage

    var changeCount = 0

    val absPos get() = 1.0 - (analogInput.voltage / 3.3)

    val position get() = (rotCount + absPos - offset) * if (direction == DcMotorSimple.Direction.REVERSE) -1.0 else 1.0

    val rotaions get() = position.rev

    var power by servo::power

    fun reset() {
        offset = rotCount + absPos
    }

    fun update() {
        if (init) {
            init = false
            voltageQueue.addFirst(voltage)
            return
        }
        if (abs(voltage - voltageQueue.last) > abs(voltage + 3.26 - voltageQueue.last)) {
            rotCount--
            voltageQueue.clear()
            changeCount++
        } else if (abs(voltage - voltageQueue.last) > abs(voltage - 3.26 - voltageQueue.last)) {
            rotCount++
            voltageQueue.clear()
            changeCount++
        }
        voltageQueue.addFirst(voltage)
        while (voltageQueue.size > 10) {
            voltageQueue.removeLast()
        }
    }
}