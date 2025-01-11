package org.firstinspires.ftc.teamcode.tele.values

import com.lib.units.s
import org.firstinspires.ftc.teamcode.robot.values.DriveValues
import org.firstinspires.ftc.teamcode.robot.values.IntakeValues
import org.firstinspires.ftc.teamcode.robot.values.LiftValues
import org.firstinspires.ftc.teamcode.robot.values.OuttakeValues
import org.firstinspires.ftc.teamcode.robot.values.RobotValues

val robotValuesWonder = RobotValues(
    drive = DriveValues(
        slowSpeed = 0.5
    ),
    lift = LiftValues(
        highPos = 0,
        basketPos = 0,
        secondBar = 0,
        inRobot = 0,
        onHangBar = 0,
    ),
    intake = IntakeValues(
        boxTiltMaxTravelDuration = 5.s,
        intakeTiltMaxTravelDuration = 5.s,
        extendoLimit = 0,
        extendoInBot = 0,
        extendoLim = 0
    ),
    outtake = OuttakeValues(
        shoulderMaxTravelDuration = 4.s,
        elbowMaxTravelDuration = 3.s,
        wristMaxTravelDuration = 3.s,
        clawMaxTravelDuration = 5.s,
        //clawOpenPos = 0.6,
        //clawClosePos = 0.353,
        shoulderBasketPos = 0.71,
        elbowBasketPos = 0.478,
        wristMiddlPos = 0.476,
        shoulderSpecimenPos = 0.7,
        elbowSpecimenPos = 0.4869,
        shoulderIntakePos = 0.358,
        elbowIntakePos = 0.05,
        shoulderPickupPos = 0.2269,
        elbowPickupPos = 0.0,
        extendForHang = 0.0,
        elbowWaitingPos = 0.5,
        shoulderWaitingPos = 0.5,
    )
)