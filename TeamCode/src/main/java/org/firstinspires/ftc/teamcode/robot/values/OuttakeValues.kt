package org.firstinspires.ftc.teamcode.robot.values

import com.lib.units.Duration

data class OuttakeValues(
    val shoulderMaxTravelDuration: Duration,
    val elbowMaxTravelDuration: Duration,
    val wristMaxTravelDuration: Duration,
    val clawMaxTravelDuration: Duration,
    //val clawOpenPos: Double,
    //val clawClosePos: Double,
    val shoulderBasketPos: Double,
    val elbowBasketPos: Double,
    val wristMiddlPos: Double,
    val shoulderSpecimenPos: Double,
    val elbowSpecimenPos: Double,
    val shoulderIntakePos: Double,
    val elbowIntakePos: Double,
    val shoulderPickupPos: Double,
    val elbowPickupPos: Double,
    val elbowWaitingPos: Double,
    val shoulderWaitingPos: Double,
)