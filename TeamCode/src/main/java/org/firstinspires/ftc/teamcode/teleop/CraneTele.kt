package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.roadrunner.MecanumKinematics
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.PoseVelocity2dDual
import com.acmerobotics.roadrunner.Time
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.now
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.config.MotorConfig
import org.firstinspires.ftc.teamcode.library.config.ServoConfig
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createServoWithConfig
import kotlin.math.absoluteValue

@TeleOp
class CraneTele : LinearOpMode() {
    private var extendSpeed = 0.0001

    data object Config {
        val motorRF = MotorConfig(
            deviceName = "motorRF",
            direction = MotorConfig.Direction.FORWARD,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        )
        val motorRB = MotorConfig(
            deviceName = "motorRB",
            direction = MotorConfig.Direction.FORWARD,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        )
        val motorLF = MotorConfig(
            deviceName = "motorLF",
            direction = MotorConfig.Direction.REVERSE,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        )
        val motorLB = MotorConfig(
            deviceName = "motorLB",
            direction = MotorConfig.Direction.REVERSE,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        )

        val motorLiftLeft = MotorConfig(
            deviceName = "liftLeft",
            direction = MotorConfig.Direction.FORWARD,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        )
        val motorLiftRight = MotorConfig(
            deviceName = "liftRight",
            direction = MotorConfig.Direction.REVERSE,
            runMode = MotorConfig.RunMode.RUN_WITHOUT_ENCODER,
            zeroPowerBehavior = MotorConfig.ZeroPowerBehavior.BRAKE,
            resetEncoder = true
        )

        val servoExtendLeft = ServoConfig(
            deviceName = "extendLeft",
            direction = ServoConfig.Direction.REVERSE
        )
        val servoExtendRight = ServoConfig(
            deviceName = "extendRight",
            direction = ServoConfig.Direction.FORWARD
        )
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

        extendPosition = 0.7
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
        servoExtendLeft = hardwareMap.createServoWithConfig(Config.servoExtendLeft)
        servoExtendRight = hardwareMap.createServoWithConfig(Config.servoExtendRight)
    }

    private fun initLift() {
        motorLiftLeft = hardwareMap.createMotorUsingConfig(Config.motorLiftLeft)
        motorLiftRight = hardwareMap.createMotorUsingConfig(Config.motorLiftRight)

    }

    private fun initDrive() {
        motorRF = hardwareMap.createMotorUsingConfig(Config.motorRF)
        motorRB = hardwareMap.createMotorUsingConfig(Config.motorRB)
        motorLF = hardwareMap.createMotorUsingConfig(Config.motorLF)
        motorLB = hardwareMap.createMotorUsingConfig(Config.motorLB)
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

        val maxMag = powers.all().map { it.value().absoluteValue }.plusElement(1.0).max()

        motorRF.power = powers.rightFront.value() / maxMag
        motorRB.power = powers.rightBack.value() / maxMag
        motorLF.power = powers.leftFront.value() / maxMag
        motorLB.power = powers.leftBack.value() / maxMag
    }
}