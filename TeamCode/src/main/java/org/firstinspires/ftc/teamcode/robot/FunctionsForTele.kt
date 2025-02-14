package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.lib.units.SleepAction
import com.lib.units.rad
import com.lib.units.s
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.controlHub
import org.firstinspires.ftc.teamcode.library.expansionHub
import org.firstinspires.ftc.teamcode.roadrunner.Localizer
import org.firstinspires.ftc.teamcode.robot.config.RobotHardwareConfig
import org.firstinspires.ftc.teamcode.robot.values.RobotValues
import org.firstinspires.ftc.teamcode.tele.config.robotHardwareConfigTransilvaniaCollege
import org.firstinspires.ftc.teamcode.tele.values.robotValuesTransilvaniaCollege

class FunctionsForTele(
    hardwareMap: HardwareMap,
    private val config: RobotHardwareConfig,
    values: RobotValues,
    private val timeKeep: TimeKeep,
    telemetry: Telemetry? = null,
) {
    val values = robotValuesTransilvaniaCollege
    val drive = Drive(hardwareMap, config.drive, values.drive, telemetry)
    val intake = Intake(hardwareMap, config.intake, values.intake, timeKeep)
    val lift = Lift(hardwareMap, config.lift, values.lift, timeKeep)
    val outtake = Outtake(hardwareMap, config.outtake, values.outtake, timeKeep)

    fun initRobot() = SequentialAction(
        InstantAction { outtake.clawPos = 0.0},
        outtake.shoudlerToPosAction(values.outtake.shoulderRobotPos),
        outtake.elbowToPosAction(values.outtake.elbowRobotPos),
        SleepAction(0.5.s),
        outtake.extendoToPosAction(values.outtake.extendoRobotPos),

        lift.liftToPosAction(values.lift.liftWaitingPos),

        InstantAction { intake.intakeUp()},
        SleepAction(0.7.s),
        intake.extendoToPosAction(values.intake.extendoInBot),
    )

    fun intakeSubmersible() = SequentialAction(
        InstantAction{intake.intakeUp()},
        intake.extendoToPosAction(values.intake.extendoLim),
    )

    fun intakeRobot() = SequentialAction(
        InstantAction{intake.intakeUp()},
        SleepAction(0.7.s),
        intake.extendoToPosAction(values.intake.extendoInBot),
    )

    fun getSample() = SequentialAction(
            InstantAction{outtake.clawPos=1.0},
            lift.liftToPosAction(values.lift.liftWaitingPos),
            ParallelAction(
                outtake.shoudlerToPosAction(values.outtake.shoulderIntakePos),
                outtake.elbowToPosAction(values.outtake.elbowIntakePos)
            ),
            lift.liftToPosAction(values.lift.liftIntakePos),
            InstantAction{outtake.clawPos=0.0},
            lift.liftToPosAction(values.lift.liftWaitingPos),
    )

    fun liftSample() = SequentialAction(
        lift.liftToPosAction(values.lift.liftWaitingPos),
        lift.liftToPosAction(values.lift.basketPos),
        ParallelAction(
            outtake.extendoToPosAction(values.outtake.extendoRobotPos),
            outtake.shoudlerToPosAction(values.outtake.shoulderBasketPos),
            outtake.elbowToPosAction(values.outtake.elbowBasketPos)
        )
    )
}