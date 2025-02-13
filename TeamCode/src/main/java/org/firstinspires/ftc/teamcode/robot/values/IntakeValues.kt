package org.firstinspires.ftc.teamcode.robot.values

import com.lib.units.Duration

data class IntakeValues(
    val boxTiltMaxTravelDuration: Duration,
    val intakeTiltMaxTravelDuration: Duration,
    val extendoInBot: Int,
    val extendoLim: Int,
    val extendoAuto: Int,
    val intakeDownPos: Double = 0.49,
    val intakeUpPos: Double = 0.04
)