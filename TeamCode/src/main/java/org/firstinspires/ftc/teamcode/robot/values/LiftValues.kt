package org.firstinspires.ftc.teamcode.robot.values

data class LiftValues(
    val highPos: Int,
    val basketPos: Int,
    val secondBar: Int,
    val inRobot: Int,
    val onHangBar: Int,
    val secondBarUp: Int,
    val liftWaitingPos: Int = 5,
    val liftIntakePos: Int = 5
)