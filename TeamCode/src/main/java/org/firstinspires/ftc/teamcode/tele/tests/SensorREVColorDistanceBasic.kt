package org.firstinspires.ftc.teamcode.tele.tests

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DistanceSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import java.util.*

@TeleOp(name = "Sensor: REVColorDistance", group = "Sensor")
class SensorREVColorDistanceBasic : LinearOpMode() {
    private lateinit var sensorColor: ColorSensor
    //private lateinit var sensorDistance: DistanceSensor

    override fun runOpMode() {
        sensorColor = hardwareMap.get(ColorSensor::class.java, "sensor_color_distance")
        //sensorDistance = hardwareMap.get(DistanceSensor::class.java, "sensor_color_distance")

        val hsvValues = floatArrayOf(0f, 0f, 0f)
        val values = hsvValues
        val SCALE_FACTOR = 255.0

        val relativeLayoutId = hardwareMap.appContext.resources.getIdentifier("RelativeLayout", "id", hardwareMap.appContext.packageName)
        val relativeLayout = (hardwareMap.appContext as Activity).findViewById<View>(relativeLayoutId)

        waitForStart()

        while (opModeIsActive()) {
            Color.RGBToHSV(
                (sensorColor.red() * SCALE_FACTOR).toInt(),
                (sensorColor.green() * SCALE_FACTOR).toInt(),
                (sensorColor.blue() * SCALE_FACTOR).toInt(),
                hsvValues
            )

            //telemetry.addData("Distance (cm)", String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)))
            telemetry.addData("Alpha", sensorColor.alpha())
            telemetry.addData("Red", sensorColor.red())
            telemetry.addData("Green", sensorColor.green())
            telemetry.addData("Blue", sensorColor.blue())
            telemetry.addData("Hue", hsvValues[0])

            relativeLayout.post { relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values)) }
            telemetry.update()
        }

        relativeLayout.post { relativeLayout.setBackgroundColor(Color.WHITE) }
    }
}