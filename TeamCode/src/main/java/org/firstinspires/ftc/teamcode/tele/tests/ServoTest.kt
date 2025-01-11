package org.firstinspires.ftc.teamcode.tele.tests

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.now
import com.lib.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class ServoTest : LinearOpMode() {

    override fun runOpMode() {
        /*

        //get our analog input from the hardwareMap
    AnalogInput analogInput = hardwareMap.get(AnalogInput.class, "myanaloginput");

    // get the voltage of our analog line
    // divide by 3.3 (the max voltage) to get a value between 0 and 1
    // multiply by 360 to convert it to 0 to 360 degrees
    double position = analogInput.getVoltage() / 3.3 * 360;
         */

        /*val servo1 =hardwareMap.get(Servo::class.java, "servoR")
        val servo2 = hardwareMap.get(Servo::class.java, "servoL")
        val servorotate = hardwareMap.get(CRServo::class.java, "rotatie")*/

        val servo = hardwareMap.get(Servo::class.java, "servoIntakeTilt")
        //val analogInput = hardwareMap.get(AnalogInput::class.java, "pos")
        //val extendServo2 = hardwareMap.get(Servo::class.java, "cTilt2")
        /*
        servo1.direction = Servo.Direction.REVERSE

        */
        var wait = 10.0.s
        var previousTime: Double
        var deltaTime : Double
        var now : Double


        waitForStart()
        previousTime = now()

        servo.position = 0.5

        val p = TelemetryPacket()
        var ok : Boolean = false

        while (opModeIsActive()){
            now = now()
            deltaTime = now - previousTime
            previousTime = now
            /*
            if(servo.position >= 0.1 && servo.position <= 0.9){
                if (!ok){
                    servo.position += 0.1 * deltaTime
                }
                else if(ok){
                    servo.position -= 0.1 * deltaTime
                }
            }
            else if(servo.position < 0.1){
                ok = false
                servo.position += 0.1 * deltaTime
            }
            else if (servo.position > 0.9){
                ok = true
                servo.position -= 0.1 * deltaTime
            }*/

            if(gamepad1.a) {
                servo.position += 0.1 * deltaTime
            }
            else if(gamepad1.y) {
                servo.position -= 0.1 * deltaTime
            }
            /*else if (gamepad1.dpad_down){
                servo3.position += 0.1 * deltaTime
            }
            else if (gamepad1.dpad_up){
                servo3.position -= 0.1 * deltaTime
            }*/
            /*
            else if(gamepad1.x){
                extendServo2.position += 0.1 * deltaTime
           }
            else if(gamepad1.b){
                extendServo2.position -= 0.1 * deltaTime
           }*/
            //val position = analogInput.voltage / 3.3 * 360
            //ztelemetry.addData("position", position)
            telemetry.addData("a Pressed", gamepad1.a)
            telemetry.addData("y Pressed", gamepad1.y)
            telemetry.addData("x Pressed", gamepad1.x)
            telemetry.addData("b Pressed", gamepad1.b)
            telemetry.addData("Servo", servo.position)
            telemetry.addData("deltaTime", deltaTime)
            /*telemetry.addData("positionServo2", servo2.position)
            telemetry.addData("positionServo1", servo1.position)
            telemetry.addData("poseDif", servo1.position - servo2.position)*/
            telemetry.update()
        }
    }
}