package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.firstinspires.ftc.teamcode.lib.units.SleepAction
import org.firstinspires.ftc.teamcode.lib.units.s
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

class Functions (hardwareMap: HardwareMap) {

    var claw = Claw(hardwareMap)
    var arm = Arm(hardwareMap)
    var intake = Intake(hardwareMap)
    var lift = Lift(hardwareMap)
    var drive = NewDrive(hardwareMap, Pose2d(10.0, -57.0, Math.toRadians(90.0)), NewDrive.Side.NEUTRAL)

    private val first_bar = 1
    private val second_bar = 1
    private val lift_in = 1
    private val lift_out = 1
    private val extend_out = 1
    private val extend_in = 1
    private val wait = 1.s

    fun scoreFirstBar() = SequentialAction(
        lift.liftGoToPos(first_bar),
        arm.extendOut(),
        claw.rotateToF(),
        arm.tiltUp(),
        arm.tiltDown(),
        claw.openClaw(),
        arm.extendIn(),
        claw.closeClaw(),
        lift.liftGoToPos(lift_in)
    )

    fun scoreSecondBar() = SequentialAction(
        lift.liftGoToPos(second_bar),
        arm.extendOut(),
        claw.rotateToF(),
        arm.tiltUp(),
        arm.tiltDown(),
        claw.openClaw(),
        arm.extendIn(),
        claw.closeClaw(),
        lift.liftGoToPos(lift_in)
    )

    fun grab() = SequentialAction(
        intake.ExtendGoToPos(extend_out),
        intake.powerIntake(1.0),
        SleepAction(wait),
        intake.powerIntake(0.0),
        intake.ExtendGoToPos(extend_in)
    )

    fun release() = SequentialAction(
        intake.ExtendGoToPos(extend_out),
        intake.powerIntake(-1.0),
        SleepAction(wait),
        intake.powerIntake(0.0),
        intake.ExtendGoToPos(extend_in)
    )
}

fun TrajectoryActionBuilder.wallGrab(arm : Arm, claw : Claw) : TrajectoryActionBuilder {
    afterTime(0.0, arm.tiltToBack())
    setTangent(-90.0)
    lineToY(-60.0)
    waitSeconds(1.0)
    afterTime(0.0, SequentialAction(
        claw.closeClaw(),
        arm.tiltToScore()
    ))
    return this
}