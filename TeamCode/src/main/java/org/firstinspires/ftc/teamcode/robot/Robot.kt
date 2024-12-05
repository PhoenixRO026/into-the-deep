package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.MecanumKinematics
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.PoseVelocity2dDual
import com.acmerobotics.roadrunner.Time
import com.acmerobotics.roadrunner.Vector2d
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.config.createIMUUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createServoWithConfig
import org.firstinspires.ftc.teamcode.library.roadrunnerext.rotate
import kotlin.math.absoluteValue

class Robot(
    private val config: RobotConfig
) {
    private val mecanumKinematics = MecanumKinematics(1.0)

    private lateinit var imu: IMU
    private lateinit var motorRF: DcMotorEx
    private lateinit var motorRB: DcMotorEx
    private lateinit var motorLF: DcMotorEx
    private lateinit var motorLB: DcMotorEx

    private lateinit var motorLiftLeft: DcMotorEx
    private lateinit var motorLiftRight: DcMotorEx

    private lateinit var servoExtendLeft: Servo
    private lateinit var servoExtendRight: Servo

    var liftPower
        get() = motorLiftLeft.power
        set(value) {
            motorLiftLeft.power = value
            motorLiftRight.power = value
        }

    var extendPosition
        get() = servoExtendLeft.position
        set(value) {
            servoExtendLeft.position = value
            servoExtendRight.position = value
        }

    val yaw
        get() = imu.robotYawPitchRollAngles.yaw

    fun init(hardwareMap: HardwareMap) {
        imu = hardwareMap.createIMUUsingConfig(config.imu)

        motorRF = hardwareMap.createMotorUsingConfig(config.motorRF)
        motorRB = hardwareMap.createMotorUsingConfig(config.motorRB)
        motorLF = hardwareMap.createMotorUsingConfig(config.motorLF)
        motorLB = hardwareMap.createMotorUsingConfig(config.motorLB)

        motorLiftLeft = hardwareMap.createMotorUsingConfig(config.motorLiftLeft)
        motorLiftRight = hardwareMap.createMotorUsingConfig(config.motorLiftRight)

        servoExtendLeft = hardwareMap.createServoWithConfig(config.servoExtendLeft)
        servoExtendRight = hardwareMap.createServoWithConfig(config.servoExtendRight)
    }

    fun drive(forward: Double, left: Double, rotate: Double) {
        val driveVec = PoseVelocity2dDual.constant<Time>(
            PoseVelocity2d(
                Vector2d(
                    forward,
                    left
                ).rotate(-yaw),
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