package org.firstinspires.ftc.teamcode.roadrunner

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.RawEncoder
import com.qualcomm.hardware.lynx.LynxDcMotorController
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.configuration.LynxConstants
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.firstinspires.ftc.teamcode.library.pinpoint.GoBildaPinpointDriver
import org.firstinspires.ftc.teamcode.library.pinpoint.fakeOdoMotor

class PinpointLocalizer @JvmOverloads constructor(
    hardwareMap: HardwareMap,
    initPose: Pose2d = Pose2d(0.0, 0.0, 0.0),
    val telemetry: Telemetry? = null
): Localizer {
    @Config
    data object PinpointConfig {
        @JvmField
        var xOffset = 23.0
        @JvmField
        var yOffset = -115.0
    }

    private var currentPose: Pose2d = initPose
    val odo: GoBildaPinpointDriver = hardwareMap.get(GoBildaPinpointDriver::class.java, "odo")

    val encX: RawEncoder
    val encY: RawEncoder

    init {
        val motorController = hardwareMap.getAll(LynxDcMotorController::class.java).first {
            LynxConstants.isEmbeddedSerialNumber(it.serialNumber)
        }
        encX = RawEncoder(fakeOdoMotor(
            { odo.posX },
            { odo.velX },
            { odo.update() },
            motorController
        ))
        encY = RawEncoder(fakeOdoMotor(
            { odo.posY },
            { odo.velY },
            { odo.update() },
            motorController
        ))

        odo.setOffsets(
            PinpointConfig.xOffset,
            PinpointConfig.yOffset
        )
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
        odo.setEncoderDirections(
            GoBildaPinpointDriver.EncoderDirection.FORWARD,
            GoBildaPinpointDriver.EncoderDirection.REVERSED
        )

        /*val initLine = telemetry?.addLine("Reseting pinpoint")
        telemetry?.update()
        odo.resetPosAndIMU()

        while (odo.deviceStatus != GoBildaPinpointDriver.DeviceStatus.READY) {
            try {
                Thread.sleep(10)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
        telemetry?.removeLine(initLine)
        telemetry?.addLine("Pinpoint initialized")
        telemetry?.update()*/

        odo.resetPosAndIMU()

        try {
            Thread.sleep(300)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }

        odo.setPosition(Pose2D(DistanceUnit.INCH, initPose.position.x, initPose.position.y, AngleUnit.RADIANS, initPose.heading.toDouble()))
    }

    override fun setPose(pose: Pose2d) {
        currentPose = pose
        odo.setPosition(Pose2D(DistanceUnit.INCH, pose.position.x, pose.position.y, AngleUnit.RADIANS, pose.heading.toDouble()))
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
            ),
            odoPosHeading
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