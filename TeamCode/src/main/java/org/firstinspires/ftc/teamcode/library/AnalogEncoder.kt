package org.firstinspires.ftc.teamcode.library

import com.lib.units.rev
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlin.math.abs

class AnalogEncoder(
    hardwareMap: HardwareMap,
    deviceName: String,
) {
    val analogInput = hardwareMap.get(AnalogInput::class.java, deviceName)

    private var init = true

    private var prevAbsPos = absPos

    private var offset = 0.0

    private var rotCount = 0

    val voltage get() = analogInput.voltage

    val absPos get() = analogInput.voltage / 3.3

    val position get() = rotCount + absPos - offset

    val rotaions get() = position.rev

    fun reset() {
        offset = rotCount + absPos
    }

    fun update() {
        if (init) {
            init = false
            prevAbsPos = absPos
            return
        }
        if (abs(absPos - prevAbsPos) > 0.5) {
            if (prevAbsPos > absPos)
                rotCount++
            else
                rotCount--
        }
        prevAbsPos = absPos
    }
}