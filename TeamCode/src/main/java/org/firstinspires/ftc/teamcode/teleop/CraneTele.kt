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

typealias MotorDirection = DcMotorSimple.Direction
typealias ServoDirection = Servo.Direction

@TeleOp
class CraneTele : LinearOpMode() {
    private var extendSpeed = 0.0001

    data object Config {
        const val MOTOR_RF = "motorRF"
        const val MOTOR_RB = "motorRB"
        const val MOTOR_LF = "motorLF"
        const val MOTOR_LB = "motorLB"

        const val MOTOR_LIFT_LEFT = "liftLeft"
        const val MOTOR_LIFT_RIGHT = "liftRight"

        const val SERVO_EXTEND_LEFT = "extendLeft"
        const val SERVO_EXTEND_RIGHT = "extendRight"
    }

    data object Direction {
        val MOTOR_RF = MotorDirection.FORWARD
        val MOTOR_RB = MotorDirection.FORWARD
        val MOTOR_LF = MotorDirection.REVERSE
        val MOTOR_LB = MotorDirection.REVERSE

        val MOTOR_LIFT_LEFT = MotorDirection.FORWARD
        val MOTOR_LIFT_RIGHT = MotorDirection.REVERSE

        val SERVO_EXTEND_LEFT = ServoDirection.REVERSE
        val SERVO_EXTEND_RIGHT = ServoDirection.FORWARD
    }

    private lateinit var motorRF: DcMotorEx
    private lateinit var motorRB: DcMotorEx
    private lateinit var motorLF: DcMotorEx
    private lateinit var motorLB: DcMotorEx

    private lateinit var motorLiftLeft: DcMotorEx
    private lateinit var motorLiftRight: DcMotorEx

    private lateinit var servoExtendLeft: Servo
    private lateinit var servoExtendRight: Servo

    private inline var liftPower
        get() = motorLiftLeft.power
        set(value) {
            motorLiftLeft.power = value
            motorLiftRight.power = value
        }

    private inline var extendPosition
        get() = servoExtendLeft.position
        set(value) {
            servoExtendLeft.position = value
            servoExtendRight.position = value
        }

    private var previousTime = now()
    private var currentTime = now()
    private inline val deltaTime get() = (currentTime - previousTime) * 1000

    private val mecanumKinematics = MecanumKinematics(1.0)

    override fun runOpMode() {
        initDrive()
        initLift()
        initExtend()

        waitForStart()

        extendPosition = 0.5
        liftPower = 0.0

        resetDeltaTime()

        while (opModeIsActive()) {
            resetDeltaTime()

            drive(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble()
            )

            liftPower = -gamepad2.right_stick_y.toDouble()

            if (gamepad2.dpad_up) {
                extendPosition += extendSpeed * deltaTime
            } else if (gamepad2.dpad_down) {
                extendPosition -= extendSpeed * deltaTime
            }

            telemetry.addData("extend pos", extendPosition)
            telemetry.addData("lift power", liftPower)
            telemetry.addData("delta time ms", deltaTime)
            telemetry.addData("fps", 1000.0 / deltaTime)
            telemetry.update()
        }
    }

    private fun resetDeltaTime() {
        previousTime = currentTime
        currentTime = now()
    }

    private fun initExtend() {
        servoExtendLeft = hardwareMap.get(Servo::class.java, Config.SERVO_EXTEND_LEFT)
        servoExtendRight = hardwareMap.get(Servo::class.java, Config.SERVO_EXTEND_RIGHT)

        servoExtendLeft.direction = Direction.SERVO_EXTEND_LEFT
        servoExtendRight.direction = Direction.SERVO_EXTEND_RIGHT
    }

    private fun initLift() {
        motorLiftLeft = hardwareMap.get(DcMotorEx::class.java, Config.MOTOR_LIFT_LEFT)
        motorLiftRight = hardwareMap.get(DcMotorEx::class.java, Config.MOTOR_LIFT_RIGHT)

        motorLiftLeft.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorLiftRight.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        motorLiftLeft.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motorLiftRight.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        motorLiftLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motorLiftRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        motorLiftRight.direction = Direction.MOTOR_LIFT_RIGHT
        motorLiftLeft.direction = Direction.MOTOR_LIFT_LEFT
    }

    private fun initDrive() {
        motorRF = hardwareMap.get(DcMotorEx::class.java, Config.MOTOR_RF)
        motorRB = hardwareMap.get(DcMotorEx::class.java, Config.MOTOR_RB)
        motorLF = hardwareMap.get(DcMotorEx::class.java, Config.MOTOR_LF)
        motorLB = hardwareMap.get(DcMotorEx::class.java, Config.MOTOR_LB)

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

        motorRF.direction = Direction.MOTOR_RF
        motorRB.direction = Direction.MOTOR_RB
        motorLF.direction = Direction.MOTOR_LF
        motorLB.direction = Direction.MOTOR_LB
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