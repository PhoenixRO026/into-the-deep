package org.firstinspires.ftc.teamcode.tele.values

import com.lib.units.s
import org.firstinspires.ftc.teamcode.robot.values.DriveValues
import org.firstinspires.ftc.teamcode.robot.values.IntakeValues
import org.firstinspires.ftc.teamcode.robot.values.LiftValues
import org.firstinspires.ftc.teamcode.robot.values.OuttakeValues
import org.firstinspires.ftc.teamcode.robot.values.RobotValues

val robotValuesWonder = RobotValues(
    drive = DriveValues(
        slowSpeed = 0.3
    ),
    lift = LiftValues(
        highPos = 0
    ),
    intake = IntakeValues(
        boxTiltMaxTravelDuration = 5.s,
        intakeTiltMaxTravelDuration = 5.s
    ),
    outtake = OuttakeValues(
        extendoMaxTravelDuration = 5.s,
        shoulderMaxTravelDuration = 5.s,
        elbowMaxTravelDuration = 5.s,
        wristMaxTravelDuration = 5.s,
        clawMaxTravelDuration = 5.s,
        //clawOpenPos = 0.6,
        //clawClosePos = 0.353,
        shoulderBasketPos = 0.71,
        elbowBasketPos = 0.478,
        wristMiddlPos = 0.476,
        shoulderSpecimenPos = 0.265,
        elbowSpecimenPos = 0.563,
        shoulderIntakePos = 0.358,
        elbowIntakePos = 0.05
    )
)