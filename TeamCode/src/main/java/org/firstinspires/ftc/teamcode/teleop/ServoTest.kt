package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.roadrunner.Line
import com.acmerobotics.roadrunner.now
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.configuration.annotations.ServoType
import org.firstinspires.ftc.teamcode.lib.units.SleepAction
import org.firstinspires.ftc.teamcode.lib.units.s

@TeleOp
class ServoTest : LinearOpMode() {

    override fun runOpMode() {


        /*val servo1 =hardwareMap.get(Servo::class.java, "servoR")
        val servo2 = hardwareMap.get(Servo::class.java, "servoL")
        val servorotate = hardwareMap.get(CRServo::class.java, "rotatie")*/

        val servo = hardwareMap.get(Servo::class.java, "servoTest")
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
            }/*
            else if(gamepad1.x){
                extendServo2.position += 0.1 * deltaTime
           }
            else if(gamepad1.b){
                extendServo2.position -= 0.1 * deltaTime
           }*/

            telemetry.addData("a Pressed", gamepad1.a)
            telemetry.addData("y Pressed", gamepad1.y)
            telemetry.addData("Servo", servo.position)
            telemetry.addData("deltaTime", deltaTime)
            /*telemetry.addData("positionServo2", servo2.position)
            telemetry.addData("positionServo1", servo1.position)
            telemetry.addData("poseDif", servo1.position - servo2.position)*/
            telemetry.update()



        }



    }
}