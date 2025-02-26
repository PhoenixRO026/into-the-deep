package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.ftc.RawEncoder
import com.lib.units.Duration
import com.lib.units.Pose
import com.lib.units.cm
import com.lib.units.deg
import com.lib.units.s
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

class Robot(
    hardwareMap: HardwareMap,
    pose: Pose = Pose(0.0.cm, 0.0.cm, 0.0.deg)
) {
    val drive: Drive
    val intake: Intake
    val lift: Lift
    val outtake: Outtake

    fun initTeleop() {
        intake.initTeleop()
        outtake.initTeleop()
    }

    fun initAuto() {
        intake.initAuto()
        outtake.initAuto()
    }

    fun update(deltaTime: Duration) {
        drive.update()
        intake.update(deltaTime)
        lift.update(deltaTime)
        outtake.update(deltaTime)
    }

    fun sampleToBasket() = SequentialAction(
        lift.liftToBasketAction(),
        outtake.armToBasketAction()
    )

    fun armAndLiftToIntakeWaiting() = SequentialAction(
        outtake.armToIntakeAction(),
        lift.liftToIntakeWaitingAction()
    )

    fun armAndLiftToIntake() = SequentialAction(
        ParallelAction(
            outtake.armToIntakeAction(),
            outtake.openClawAction()
        ),
        lift.liftToIntakeAction()
    )

    fun robotToBarInstant() {
        lift.liftToBarInitInstant()
        outtake.armToBarInstant()
    }

    fun turnOffAction() = ParallelAction(
        outtake.armToNeutralAction(),
        intake.sweeperOffAction(),
        intake.tiltUpAction(),
        InstantAction { intake.extendoPower = 0.0 },
        InstantAction { lift.power = 0.0 }
    )

    init {
        val mecanumDrive = MecanumDrive(hardwareMap, pose.pose2d)

        val liftEncoder = RawEncoder(mecanumDrive.rightBack)
        val intakeExtendoEncoder = RawEncoder(mecanumDrive.leftFront)

        liftEncoder.direction = DcMotorSimple.Direction.REVERSE
        intakeExtendoEncoder.direction = DcMotorSimple.Direction.FORWARD

        val liftLeftMotor = hardwareMap.get(DcMotorEx::class.java, "motorLiftLeft")
        val liftRightMotor = hardwareMap.get(DcMotorEx::class.java, "motorLiftRight")
        val intakeExtendoMotor = hardwareMap.get(DcMotorEx::class.java, "motorExtendoIntake")
        val intakeSweeperMotor = hardwareMap.get(DcMotorEx::class.java, "motorSweeper")

        liftLeftMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        liftRightMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        intakeExtendoMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        intakeSweeperMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        liftLeftMotor.direction = DcMotorSimple.Direction.FORWARD
        liftRightMotor.direction = DcMotorSimple.Direction.REVERSE
        intakeExtendoMotor.direction = DcMotorSimple.Direction.FORWARD
        intakeSweeperMotor.direction = DcMotorSimple.Direction.FORWARD

        liftLeftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        liftRightMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        intakeExtendoMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        intakeSweeperMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        val intakeTiltServo = hardwareMap.get(Servo::class.java, "servoIntakeTilt")
        val outtakeExtendoServo = hardwareMap.get(Servo::class.java, "servoExtendoOuttake")
        val outtakeShoulderServo = hardwareMap.get(Servo::class.java, "servoShoulder")
        val outtakeElbowServo = hardwareMap.get(Servo::class.java, "servoElbow")
        val outtakeWristServo = hardwareMap.get(Servo::class.java, "servoWrist")
        val outtakeClawServo = hardwareMap.get(Servo::class.java, "servoClaw")

        intakeTiltServo.direction = Servo.Direction.FORWARD
        outtakeExtendoServo.direction = Servo.Direction.FORWARD
        outtakeShoulderServo.direction = Servo.Direction.FORWARD
        outtakeElbowServo.direction = Servo.Direction.FORWARD
        outtakeWristServo.direction = Servo.Direction.FORWARD
        outtakeClawServo.direction = Servo.Direction.FORWARD

        intakeTiltServo.scaleRange(0.0, 1.0)
        outtakeExtendoServo.scaleRange(0.0, 1.0)
        outtakeShoulderServo.scaleRange(0.0, 1.0)
        outtakeElbowServo.scaleRange(0.0, 1.0)
        outtakeWristServo.scaleRange(0.0, 1.0)
        outtakeClawServo.scaleRange(0.31, 0.6)

        val intakeColorSensor = hardwareMap.get(NormalizedColorSensor::class.java, "intakeColorSensor")

        drive = Drive(mecanumDrive)
        intake = Intake(
            extendoMotor = intakeExtendoMotor,
            sweeperMotor = intakeSweeperMotor,
            extendoEncoder = intakeExtendoEncoder,
            tiltServo = intakeTiltServo,
            colorSensor = intakeColorSensor
        )
        lift = Lift(
            leftMotor = liftLeftMotor,
            rightMotor = liftRightMotor,
            encoder = liftEncoder
        )
        outtake = Outtake(
            extendoServo = outtakeExtendoServo,
            shoulderServo = outtakeShoulderServo,
            elbowServo = outtakeElbowServo,
            wristServo = outtakeWristServo,
            clawServo = outtakeClawServo
        )
    }

    fun addTelemetry(telemetry: Telemetry, deltaTime: Duration) {
        intake.addTelemetry(telemetry)
        lift.addTelemetry(telemetry)
        telemetry.addData("delta time ms", deltaTime.asMs)
        telemetry.addData("fps", 1.s / deltaTime)
    }
}