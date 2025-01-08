package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.roadrunner.SequentialAction
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.robot.Claw

@Autonomous

class AutoTest : LinearOpMode() {


    override fun runOpMode() {
        val claw = Claw(hardwareMap)


        waitForStart()

        while(opModeIsActive()){
            SequentialAction(
                claw.openClaw(),
                claw.closeClaw()
            )
            Thread.sleep(4000)
        }

    }
}