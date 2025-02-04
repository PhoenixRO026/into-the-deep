package org.firstinspires.ftc.teamcode.tele.values

import com.lib.units.deg
import com.lib.units.s
import org.firstinspires.ftc.teamcode.robot.values.DriveValues
import org.firstinspires.ftc.teamcode.robot.values.IntakeValues
import org.firstinspires.ftc.teamcode.robot.values.LiftValues
import org.firstinspires.ftc.teamcode.robot.values.OuttakeValues
import org.firstinspires.ftc.teamcode.robot.values.RobotValues

val robotValuesTransilvaniaCollege = RobotValues(
    drive = DriveValues(
        slowSpeed = 0.5
    ),
    lift = LiftValues(
        highPos = 0,
        basketPos = 2980,
        secondBar = 493,
        secondBarUp = 1324,
        inRobot = 0,
        onHangBar = 0,
        liftWaitingPos = 817,
        liftIntakePos = 318
    ),
    intake = IntakeValues(
        boxTiltMaxTravelDuration = 5.s,
        intakeTiltMaxTravelDuration = 5.s,
        //extendoLimit = 1424,
        extendoInBot = 0,
        extendoLim = 1424,
        extendoAuto = 1237,
    ),
    outtake = OuttakeValues(
        extendoMaxTravelDuration = 3.s,
        shoulderMaxTravelDuration = 3.s,
        elbowMaxTravelDuration = 3.s,
        wristMaxTravelDuration = 3.s,
        clawMaxTravelDuration = 5.s,
        //clawOpenPos = 0.6,
        //clawClosePos = 0.353,
        wristMiddlPos = 0.5133,
        //basket
        shoulderBasketPos = 0.4045,
        elbowBasketPos = 0.7172,
        //bar
        shoulderBarPos = 0.8476,
        elbowBarPos = 0.8459,
        // specimen
        shoulderSpecimenPos = 0.3472,
        elbowSpecimenPos = 0.7511,
        //intake
        extendoIntakePos = 0.25,
        shoulderIntakePos = 0.788,
        elbowIntakePos = 0.3649,
        //robot
        shoulderRobotPos = 0.6404,
        elbowRobotPos = 0.5159,
        extendoRobotPos = 0.0,
        //random
        extendoOutPos = 0.465,
        elbowWaitingPos = 0.5,
        shoulderWaitingPos = 0.5
    )
)