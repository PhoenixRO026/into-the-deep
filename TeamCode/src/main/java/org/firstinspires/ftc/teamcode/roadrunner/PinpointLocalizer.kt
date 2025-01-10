package org.firstinspires.ftc.teamcode.roadrunner

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.library.pinpoint.GoBildaPinpointDriver

class PinpointLocalizer @JvmOverloads constructor(
    hardwareMap: HardwareMap,
    private val initPose: Pose2d = Pose2d(0.0, 0.0, 0.0)
): Localizer {
    @Config
    data object PinpointConfig {
        @JvmField
        var xOffset = 0.0
        @JvmField
        var yOffset = 0.0
    }

    private var currentPose: Pose2d = initPose
    val odo: GoBildaPinpointDriver = hardwareMap.get(GoBildaPinpointDriver::class.java, "odo")

    init {
        odo.setOffsets(
            PinpointConfig.xOffset,
            PinpointConfig.yOffset
        )
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
        odo.setEncoderDirections(
            GoBildaPinpointDriver.EncoderDirection.FORWARD,
            GoBildaPinpointDriver.EncoderDirection.FORWARD
        )
        odo.resetPosAndIMU()
    }

    override fun setPose(pose: Pose2d) {
        currentPose = pose
    }

    override fun getPose(): Pose2d = currentPose

    override fun update(): PoseVelocity2d {
        odo.update()

        val odoPos = odo.position
        val odoPosX = odoPos.getX(DistanceUnit.INCH)
        val odoPosY = odoPos.getY(DistanceUnit.INCH)
        val odoPosHeading = odoPos.getHeading(AngleUnit.RADIANS)
        val odoVel = odo.velocity
        val odoVelX = odoVel.getX(DistanceUnit.INCH)
        val odoVelY = odoVel.getY(DistanceUnit.INCH)
        val odoVelHeading = odoVel.getHeading(AngleUnit.RADIANS)

        currentPose = Pose2d(
            Vector2d(
                odoPosX,
                odoPosY
            ) + initPose.position,
            odoPosHeading + initPose.heading.toDouble()
        )

        return PoseVelocity2d(
            Vector2d(
                odoVelX,
                odoVelY
            ),
            odoVelHeading
        )
    }
}