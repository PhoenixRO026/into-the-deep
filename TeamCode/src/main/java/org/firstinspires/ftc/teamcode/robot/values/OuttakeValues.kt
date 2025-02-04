package org.firstinspires.ftc.teamcode.robot.values

import com.lib.units.Angle
import com.lib.units.Duration
import com.lib.units.s

data class OuttakeValues(
    val extendoMaxTravelDuration: Duration = 2.0.s,
    val shoulderMaxTravelDuration: Duration,
    val elbowMaxTravelDuration: Duration,
    val wristMaxTravelDuration: Duration,
    val clawMaxTravelDuration: Duration,
    //val clawOpenPos: Double,
    //val clawClosePos: Double,
    val wristMiddlPos: Double,
    //basket pos
    val shoulderBasketPos: Double,
    val elbowBasketPos: Double,
    // bar pos
    val shoulderBarPos : Double = 0.5,
    val elbowBarPos : Double = 0.5,
    //specimen pos
    val shoulderSpecimenPos: Double,
    val elbowSpecimenPos: Double,
    //intake pos
    val extendoIntakePos : Double,
    val shoulderIntakePos: Double,
    val elbowIntakePos: Double,
    //robot pos
    val shoulderRobotPos: Double,
    val elbowRobotPos: Double,
    val extendoRobotPos: Double,
    //random pos
    val extendoOutPos: Double = 0.0,
    val elbowWaitingPos: Double,
    val shoulderWaitingPos: Double,
)