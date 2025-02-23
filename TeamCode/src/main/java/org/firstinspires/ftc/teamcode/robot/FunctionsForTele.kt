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
    val robot = TeleRobot(hardwareMap, config, values, timeKeep, telemetry)

    fun initRobot() = SequentialAction(
        InstantAction { robot.outtake.clawPos = 0.0},
        robot.outtake.shoudlerToPosAction(values.outtake.shoulderRobotPos),
        robot.outtake.elbowToPosAction(values.outtake.elbowRobotPos),
        SleepAction(0.5.s),
        robot.outtake.extendoToPosAction(values.outtake.extendoRobotPos),

        robot.lift.liftToPosAction(values.lift.liftWaitingPos),

        InstantAction { robot.intake.intakeUp()},
        SleepAction(0.7.s),
        robot.intake.extendoToPosAction(values.intake.extendoInBot),
    )

    fun intakeSubmersible() = SequentialAction(
        InstantAction{robot.intake.intakeUp()},
        robot.intake.extendoToPosAction(values.intake.extendoLim),
    )

    fun intakeRobot() = SequentialAction(
        InstantAction{robot.intake.intakeUp()},
        SleepAction(0.7.s),
        robot.intake.extendoToPosAction(values.intake.extendoInBot),
    )

    fun getSample() = SequentialAction(
        InstantAction{robot.outtake.clawPos=1.0},
        robot.lift.liftToPosAction(values.lift.liftWaitingPos),
        ParallelAction(
            robot.outtake.shoudlerToPosAction(values.outtake.shoulderIntakePos),
            robot.outtake.elbowToPosAction(values.outtake.elbowIntakePos)
        ),
        robot.lift.liftToPosAction(values.lift.liftIntakePos),
        InstantAction{robot.outtake.clawPos=0.0},
        robot.lift.liftToPosAction(values.lift.liftWaitingPos),
    )

    fun liftSample() = SequentialAction(
        robot.lift.liftToPosAction(values.lift.liftWaitingPos),
        robot.lift.liftToPosAction(values.lift.basketPos),
        ParallelAction(
            robot.outtake.extendoToPosAction(values.outtake.extendoRobotPos),
            robot.outtake.shoudlerToPosAction(values.outtake.shoulderBasketPos),
            robot.outtake.elbowToPosAction(values.outtake.elbowBasketPos)
        )
    )
}