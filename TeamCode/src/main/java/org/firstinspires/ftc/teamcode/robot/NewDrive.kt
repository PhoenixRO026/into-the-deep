package org.firstinspires.ftc.teamcode.robot

import android.view.WindowInsets.Side
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.lib.units.rotate
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

class NewDrive (hardwareMap: HardwareMap, startPose: Pose2d, side: Side){
    val mechanumDrive = MecanumDrive(hardwareMap, startPose)

    private val offSet = when(side){
        Side.BLUE -> Math.toRadians(-90.0)
        Side.RED -> Math.toRadians(90.0)
        Side.NEUTRAL -> 0.0
    }

    fun resetHeading() {
        mechanumDrive.pose = Pose2d(mechanumDrive.pose.position, 0.0)
    }

    var speed = 1.0

    /// this drives the robot in field centric
    fun drive(forward : Double, strafe : Double, rotate : Double) {
        mechanumDrive.updatePoseEstimate()

        val driveVector = Vector2d(forward, -strafe)
        val rotatedVector = driveVector.rotate(offSet - mechanumDrive.pose.heading.toDouble())

        val drivePower = PoseVelocity2d(rotatedVector * speed, -rotate * speed)

        mechanumDrive.setDrivePowers(drivePower)
    }

    enum class Side {
        BLUE,
        RED,
        NEUTRAL
    }
}