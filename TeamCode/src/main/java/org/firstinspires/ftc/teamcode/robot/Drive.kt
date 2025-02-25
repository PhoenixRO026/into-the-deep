package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.lib.roadrunner_ext.ex
import com.lib.units.Pose
import com.lib.units.rotate
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

class Drive(
    private val mecanumDrive: MecanumDrive
) {
    @Config
    data object DriveConfig {
        @JvmField var slowSpeed = 0.5
    }

    private var headingOffset = 0.0
    private val heading get() = mecanumDrive.localizer.pose.heading.toDouble() - headingOffset
    private val curentSpeed get() = if (isSlowMode) DriveConfig.slowSpeed else  1.0

    var isSlowMode = false

    fun actionBuilder(beginPose: Pose) = mecanumDrive.actionBuilder(beginPose.pose2d).ex()
    
    fun resetFieldCentric() {
        headingOffset = mecanumDrive.localizer.pose.heading.toDouble()
    }

    fun update() {
        mecanumDrive.updatePoseEstimate()
    }

    fun driveFieldCentric(forward: Double, left: Double, rotate: Double) {
        val driveVec = PoseVelocity2d(
            Vector2d(
                forward * curentSpeed,
                left * curentSpeed
            ).rotate(-heading),
            rotate * curentSpeed
        )

        mecanumDrive.setDrivePowers(driveVec)
    }
}