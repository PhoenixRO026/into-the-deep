package org.firstinspires.ftc.teamcode.robot.values

import com.lib.units.Duration

data class OuttakeValues(
    val extendoMaxTravelDuration: Duration,
    val shoulderMaxTravelDuration: Duration,
    val elbowMaxTravelDuration: Duration,
    val wristMaxTravelDuration: Duration,
    val clawMaxTravelDuration: Duration,
    val clawOpenPos: Double = 0.6,
    val clawClosePos: Double = 0.353,
    val shoulderBasketPos: Double = 0.71,
    val elbowBasketPos: Double = 0.478,
    val wristMiddlPos: Double = 0.476,
    val shoulderSpecimenPos: Double = 0.265,
    val elbowSpecimenPos: Double = 0.563,
    val shoulderIntakePos: Double = 0.358,
    val elbowIntakePos: Double = 0.05
)