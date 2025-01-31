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
        basketPos = 2488,
        secondBar = 0,
        inRobot = 0,
        onHangBar = 0
    ),
    intake = IntakeValues(
        boxTiltMaxTravelDuration = 5.s,
        intakeTiltMaxTravelDuration = 5.s,
        extendoLimit = 0,
        extendoInBot = 0,
        extendoLim = 1545
    ),
    outtake = OuttakeValues(
        shoulderMaxTravelDuration = 3.s,
        elbowMaxTravelDuration = 3.s,
        wristMaxTravelDuration = 3.s,
        clawMaxTravelDuration = 5.s,
        //clawOpenPos = 0.6,
        //clawClosePos = 0.353,
        wristMiddlPos = 0.476,
        //basket
        shoulderBasketPos = 0.4045,
        elbowBasketPos = 0.7172,
        //bar
        shoulderBarPos = 0.8476,
        elbowBarPos = 0.8459,
        // specimen
        shoulderSpecimenPos = 0.3733,
        elbowSpecimenPos = 0.7506,
        //intake
        extendoIntakePos = 465.38.deg,
        shoulderIntakePos = 0.788,
        elbowIntakePos = 0.3649,
        //robot
        shoulderRobotPos = 0.6404,
        elbowRobotPos = 0.5159,
        extendForHang = 0.0,
        elbowWaitingPos = 0.5,
        shoulderWaitingPos = 0.5
    )
)