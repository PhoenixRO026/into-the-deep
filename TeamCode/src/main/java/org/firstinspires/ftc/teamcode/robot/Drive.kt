package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.MecanumKinematics
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.PoseVelocity2dDual
import com.acmerobotics.roadrunner.Time
import com.acmerobotics.roadrunner.Vector2d
import com.lib.units.rotate
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.library.config.createIMUUsingConfig
import org.firstinspires.ftc.teamcode.library.config.createMotorUsingConfig
import org.firstinspires.ftc.teamcode.roadrunner.Localizer
import org.firstinspires.ftc.teamcode.roadrunner.PinpointLocalizer
import org.firstinspires.ftc.teamcode.robot.config.DriveHardwareConfig
import org.firstinspires.ftc.teamcode.robot.values.DriveValues
import kotlin.math.absoluteValue

class Drive(
    hardwareMap: HardwareMap,
    config: DriveHardwareConfig,
    private val values: DriveValues,
    telemetry: Telemetry?,
    private val localizer: Localizer = PinpointLocalizer(hardwareMap, Pose2d(0.0, 0.0, 0.0), telemetry),
) {
    private val mecanumKinematics = MecanumKinematics(1.0)

    //private val imu: IMU = hardwareMap.createIMUUsingConfig(config.imu)
    private val motorRF: DcMotorEx = hardwareMap.createMotorUsingConfig(config.motorRF)

    private val motorRB: DcMotorEx = hardwareMap.createMotorUsingConfig(config.motorRB)

    private val motorLF: DcMotorEx = hardwareMap.createMotorUsingConfig(config.motorLF)

    private val motorLB: DcMotorEx = hardwareMap.createMotorUsingConfig(config.motorLB)

    private var offset = 0.0

    private val currentSpeed get() = if (isSlowMode) values.slowSpeed else 1.0

    var isSlowMode = false

    val yaw
        get() = localizer.pose.heading.toDouble() - offset

    fun resetFieldCentric() {
        offset = localizer.pose.heading.toDouble()
    }

    fun update() {
        localizer.update()
    }

    fun driveFieldCentric(forward: Double, left: Double, rotate: Double) {
        val driveVec = PoseVelocity2dDual.constant<Time>(
            PoseVelocity2d(
                Vector2d(
                    forward * currentSpeed,
                    left * currentSpeed
                ).rotate(-yaw),
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