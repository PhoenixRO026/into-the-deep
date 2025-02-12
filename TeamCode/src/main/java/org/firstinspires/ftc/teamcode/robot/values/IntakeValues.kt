package org.firstinspires.ftc.teamcode.robot.values

import com.lib.units.Duration

data class IntakeValues(
    val boxTiltMaxTravelDuration: Duration,
    val intakeTiltMaxTravelDuration: Duration,
    val extendoInBot: Int,
    val extendoLim: Int,
    val extendoAuto: Int,
    val intakeUpPos: Double = 0.04,
    val intakeDownPos: Double = 0.48
)