package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.now
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.lib.units.s
import org.firstinspires.ftc.teamcode.robot.Arm
import org.firstinspires.ftc.teamcode.robot.Claw


@TeleOp
class ArmTest : LinearOpMode() {

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

        val servoShoulder = hardwareMap.get(Servo::class.java, "servoShoulder")
        val servoElbow = hardwareMap.get(Servo::class.java, "servoElbow")
        val servoClaw = hardwareMap.get(Servo::class.java, "servoClaw")
        //val analogInput = hardwareMap.get(AnalogInput::class.java, "pos")
        //val extendServo2 = hardwareMap.get(Servo::class.java, "cTilt2")
        /*
        servo1.direction = Servo.Direction.REVERSE

        */
        var wait = 10.0.s

        var claw = Claw(hardwareMap)
        var arm = Arm(hardwareMap)

        var previousTime: Double
        var deltaTime : Double
        var now : Double

        waitForStart()
        previousTime = now()

        var currentAction : Action? = null

        var ok : Boolean = false

        while (opModeIsActive()){
            now = now()
            deltaTime = now - previousTime
            previousTime = now

            if(gamepad1.a) {
                currentAction = arm.tiltToRobot()
            }
            else if(gamepad1.y) {
                currentAction = arm.tiltToBack()
            }
            else if(gamepad1.x) {
                //currentAction = claw.openClaw()
                claw.openClaw().run(TelemetryPacket())
            }
            else if(gamepad1.b) {
                currentAction = claw.closeClaw()
            }
            else if(gamepad1.dpad_down){
                currentAction = arm.rotateDown()
            }
            else if(gamepad1.dpad_up){
                currentAction = arm.rotateUp()
            }

            val running = currentAction?.run(TelemetryPacket()) ?: false
            if(!running){
                currentAction = null
            }

            //

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
            telemetry.addData("ServoShoulder", servoShoulder.position)
            telemetry.addData("ServoElbow", servoElbow.position)
            telemetry.addData("ServoClaw", servoClaw.position)
            telemetry.addData("deltaTime", deltaTime)
            telemetry.update()



        }



    }
}