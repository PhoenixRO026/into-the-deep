package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.roadrunner.Line
import com.acmerobotics.roadrunner.now
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.configuration.annotations.ServoType

@TeleOp
class ServoTest : LinearOpMode() {

    override fun runOpMode() {
        /*val servo1 =hardwareMap.get(Servo::class.java, "servoR")
        val servo2 = hardwareMap.get(Servo::class.java, "servoL")
        val servorotate = hardwareMap.get(CRServo::class.java, "rotatie")*/

        val clawRotationServo = hardwareMap.get(Servo::class.java, "clawRotationServo")
        val fingerServo = hardwareMap.get(Servo::class.java, "fingerServo")
        val servoExtend = hardwareMap.get(CRServo::class.java, "servoExtend")
        /*
        servo1.direction = Servo.Direction.REVERSE

        servo1.position = 0.5
        servo2.position = 0.5*/

        /*
        * poz cos brat: 0.4839
        *
        * */


        var previousTime: Double
        var deltaTime : Double
        var now : Double
        waitForStart()
        previousTime = now()

        clawRotationServo.position = 0.5
        fingerServo.position = 0.5

        while (opModeIsActive()){
            now = now()
            deltaTime = now - previousTime
            previousTime = now


           if(gamepad1.a) {
               clawRotationServo.position += 0.1 * deltaTime
            }
            else if(gamepad1.y) {
               clawRotationServo.position -= 0.1 * deltaTime
            }
            else if(gamepad1.b){
               fingerServo.position -= 0.1 * deltaTime
            }
            else if(gamepad1.x){
               fingerServo.position += 0.1 * deltaTime
            }
            else if(gamepad1.dpad_up){
                servoExtend.power = 1.0
           }
            telemetry.addData("x Pressed", gamepad1.a)
            telemetry.addData("Servo", clawRotationServo.position)
            telemetry.addData("deget", fingerServo.position)
            telemetry.update()
            /*


            telemetry.addData("deltaTime", deltaTime)
            telemetry.addData("positionServo2", servo2.position)
            telemetry.addData("positionServo1", servo1.position)
            telemetry.addData("poseDif", servo1.position - servo2.position)
            telemetry.update()*/



        }



    }
}