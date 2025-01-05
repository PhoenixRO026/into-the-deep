package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.MecanumKinematics
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.PoseVelocity2dDual
import com.acmerobotics.roadrunner.Time
import com.acmerobotics.roadrunner.Vector2d
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.teamcode.library.config.createIMUUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig
import org.firstinspires.ftc.teamcode.robot.config.DriveHardwareConfig
import org.firstinspires.ftc.teamcode.robot.values.DriveValues
import kotlin.math.absoluteValue

class Drive(
    private val config: DriveHardwareConfig,
    private val values: DriveValues
) {
    private val mecanumKinematics = MecanumKinematics(1.0)

    private lateinit var imu: IMU
    private lateinit var motorRF: DcMotorEx
    private lateinit var motorRB: DcMotorEx
    private lateinit var motorLF: DcMotorEx
    private lateinit var motorLB: DcMotorEx

    private var offset = 0.0

    private val currentSpeed get() = if (isSlowMode) values.slowSpeed else 1.0

    var isSlowMode = false

    val yaw
        get() = imu.robotYawPitchRollAngles.yaw - offset

    fun resetFieldCentric() {
        offset = imu.robotYawPitchRollAngles.yaw
    }

    fun init(hardwareMap: HardwareMap) {
        imu = hardwareMap.createIMUUsingConfig(config.imu)

        motorRF = hardwareMap.createMotorUsingConfig(config.motorRF)
        motorRB = hardwareMap.createMotorUsingConfig(config.motorRB)
        motorLF = hardwareMap.createMotorUsingConfig(config.motorLF)
        motorLB = hardwareMap.createMotorUsingConfig(config.motorLB)
    }

    fun driveFieldCentric(forward: Double, left: Double, rotate: Double) {
        val driveVec = PoseVelocity2dDual.constant<Time>(
            PoseVelocity2d(
                Vector2d(
                    forward * currentSpeed,
                    left * currentSpeed
                ),
                rotate * currentSpeed
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