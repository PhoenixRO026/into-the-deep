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
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader
import org.firstinspires.ftc.teamcode.robot.TeleRobot
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigTransilvaniaCollege
import org.firstinspires.ftc.teamcode.tele.values.robotValuesTransilvaniaCollege
import java.util.*
import kotlin.math.abs

@TeleOp(name = "Sensor: REVColorDistance", group = "Sensor")
class SensorREVColorDistanceBasic : LinearOpMode() {
    private lateinit var sensorColor: ColorSensor
    //private lateinit var sensorDistance: DistanceSensor

    override fun runOpMode() {

        val config = robotHardwareConfigTransilvaniaCollege
        val valuess = robotValuesTransilvaniaCollege

        sensorColor = hardwareMap.get(ColorSensor::class.java, "intakeColorSensor")
        //sensorDistance = hardwareMap.get(DistanceSensor::class.java, "sensor_color_distance")

        val hsvValues = floatArrayOf(0f, 0f, 0f)
        val values = hsvValues
        val SCALE_FACTOR = 255.0
        var interval: Boolean = false
        var minValue: Int = 10000
        var maxValue: Int = 0

        val Button = ButtonReader { gamepad1.x }


        val relativeLayoutId = hardwareMap.appContext.resources.getIdentifier("RelativeLayout", "id", hardwareMap.appContext.packageName)
        val relativeLayout = (hardwareMap.appContext as Activity).findViewById<View>(relativeLayoutId)
        val timeKeep = TimeKeep()
        val robot = TeleRobot(hardwareMap, config, valuess, timeKeep, telemetry)


        waitForStart()



        while (opModeIsActive()) {


            if(gamepad1.dpad_up){
                robot.intake.intakeUp()
            }
            else if(gamepad1.dpad_down){
                robot.intake.intakeDown()
            }

            Color.RGBToHSV(
                (sensorColor.red() * SCALE_FACTOR).toInt(),
                (sensorColor.green() * SCALE_FACTOR).toInt(),
                (sensorColor.blue() * SCALE_FACTOR).toInt(),
                hsvValues
            )

            if (gamepad1.left_trigger >= 0.2){
                if (hsvValues[0] < minValue){
                    minValue = hsvValues[0].toInt()
                }
                if (hsvValues[0] > maxValue){
                    maxValue = hsvValues[0].toInt()
                }

            }

            //telemetry.addData("Distance (cm)", String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)))
            telemetry.addData("red side", robot.intake.shouldStopIntake("RED", hsvValues[0].toInt()))
            telemetry.addData("blue side", robot.intake.shouldStopIntake("BLUE", hsvValues[0].toInt()))
            telemetry.addData("interval", interval.toString())
            telemetry.addData("Alpha", sensorColor.alpha())
            telemetry.addData("Red", sensorColor.red())
            telemetry.addData("Green", sensorColor.green())
            telemetry.addData("Blue", sensorColor.blue())
            telemetry.addData("Hue", hsvValues[0])
            telemetry.addData("MinValue", minValue)
            telemetry.addData("MaxValue", maxValue)

            relativeLayout.post { relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values)) }
            telemetry.update()
        }

        relativeLayout.post { relativeLayout.setBackgroundColor(Color.WHITE) }
    }
}