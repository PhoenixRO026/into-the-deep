package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.roadrunner.MecanumKinematics
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.PoseVelocity2dDual
import com.acmerobotics.roadrunner.Time
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.now
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class CraneTele : LinearOpMode(){
    private var extendSpeed = 0.1

    private lateinit var motorRF: DcMotorEx
    private lateinit var motorRB: DcMotorEx
    private lateinit var motorLF: DcMotorEx
    private lateinit var motorLB: DcMotorEx

    private lateinit var motorLiftLeft: DcMotorEx
    private lateinit var motorLiftRight: DcMotorEx

    private lateinit var servoExtendLeft: Servo
    private lateinit var servoExtendRight: Servo

    private var liftPower
        get() = motorLiftLeft.power
        set(value) {
            motorLiftLeft.power = value
            motorLiftRight.power = value
        }

    private var extendPosition
        get() = servoExtendLeft.position
        set(value) {
            servoExtendLeft.position = value
            servoExtendRight.position = value
        }

    private var previousTime = now() / 100
    private var currentTime = now() / 100
    private val deltaTime = currentTime - previousTime

    private val mecanumKinematics = MecanumKinematics(1.0)

    override fun runOpMode() {
        initDrive()
        initLift()
        initExtend()

        waitForStart()

        resetDeltaTime()

        while (opModeIsActive()) {

            resetDeltaTime()

            drive(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble()
            )

            liftPower = -gamepad2.left_stick_y.toDouble()

            if (gamepad2.dpad_up) {
                extendPosition += extendSpeed * deltaTime
            } else if (gamepad2.dpad_down) {
                extendPosition -= extendSpeed * deltaTime
            }

            telemetry.addData("extend pos", extendPosition)
            telemetry.addData("lift power", liftPower)
            telemetry.update()
        }
    }

    private fun resetDeltaTime() {
        previousTime = currentTime
        currentTime = now() / 100
    }

    private fun initExtend() {
        servoExtendLeft = hardwareMap.get(Servo::class.java, "extendLeft")
        servoExtendRight = hardwareMap.get(Servo::class.java, "extendRight")

        servoExtendRight.direction = Servo.Direction.REVERSE
    }

    private fun initLift() {
        motorLiftLeft = hardwareMap.get(DcMotorEx::class.java, "liftLeft")
        motorLiftRight = hardwareMap.get(DcMotorEx::class.java, "liftRight")

        motorLiftLeft.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorLiftRight.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        motorLiftLeft.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motorLiftRight.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        motorLiftLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motorLiftRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        motorLiftRight.direction = DcMotorSimple.Direction.REVERSE
    }

    private fun initDrive() {
        motorRF = hardwareMap.get(DcMotorEx::class.java, "motorRF")
        motorRB = hardwareMap.get(DcMotorEx::class.java, "motorRB")
        motorLF = hardwareMap.get(DcMotorEx::class.java, "motorLF")
        motorLB = hardwareMap.get(DcMotorEx::class.java, "motorLB")

        motorRF.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorRB.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorLF.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorLB.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        motorRF.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motorRB.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motorLF.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motorLB.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        motorRF.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motorRB.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motorLF.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motorLB.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        motorLF.direction = DcMotorSimple.Direction.REVERSE
        motorLB.direction = DcMotorSimple.Direction.REVERSE
    }

    private fun drive(forward: Double, left: Double, rotate: Double) {
        val driveVec = PoseVelocity2dDual.constant<Time>(
            PoseVelocity2d(
                Vector2d(
                    forward,
                    left
                ),
                rotate
            ),
            1
        )

        val powers = mecanumKinematics.inverse(driveVec)

        val maxPower = powers.all().maxOfOrNull { it.value() } ?: 1.0

        motorRF.power = powers.rightFront.value() / maxPower
        motorRB.power = powers.rightBack.value() / maxPower
        motorLF.power = powers.leftFront.value() / maxPower
        motorLB.power = powers.leftBack.value() / maxPower
    }
}