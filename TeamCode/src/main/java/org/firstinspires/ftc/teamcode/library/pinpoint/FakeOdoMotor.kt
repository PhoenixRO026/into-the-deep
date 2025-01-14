package org.firstinspires.ftc.teamcode.library.pinpoint

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorController
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer
import com.qualcomm.robotcore.hardware.PIDCoefficients
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit

fun fakeOdoMotor(pos: () -> Double, vel : () -> Double, update: () -> Unit): DcMotorEx = object :
    DcMotorEx {
    override fun getManufacturer(): Manufacturer = Manufacturer.Other

    override fun getDeviceName(): String = "fake motor"

    override fun getConnectionInfo(): String = "fake info"

    override fun getVersion(): Int = 1

    override fun resetDeviceConfigurationForOpMode() {}

    override fun close() {}

    override fun setDirection(direction: DcMotorSimple.Direction) {}

    override fun getDirection(): DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD

    override fun setPower(power: Double) {}

    override fun getPower(): Double = 0.0

    override fun getMotorType(): MotorConfigurationType = MotorConfigurationType.getUnspecifiedMotorType()

    override fun setMotorType(motorType: MotorConfigurationType) {}

    override fun getController(): DcMotorController = object : DcMotorController {
        override fun getManufacturer(): Manufacturer = Manufacturer.Other

        override fun getDeviceName(): String = "fake motor controller"

        override fun getConnectionInfo(): String = "fake info"

        override fun getVersion(): Int = 1

        override fun resetDeviceConfigurationForOpMode(motor: Int) {}

        override fun resetDeviceConfigurationForOpMode() {}

        override fun close() {}

        override fun setMotorType(motor: Int, motorType: MotorConfigurationType) {}

        override fun getMotorType(motor: Int): MotorConfigurationType = MotorConfigurationType.getUnspecifiedMotorType()

        override fun setMotorMode(motor: Int, mode: DcMotor.RunMode) {}

        override fun getMotorMode(motor: Int): DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        override fun setMotorPower(motor: Int, power: Double) {}

        override fun getMotorPower(motor: Int): Double = 0.0

        override fun isBusy(motor: Int): Boolean = false

        override fun setMotorZeroPowerBehavior(
            motor: Int,
            zeroPowerBehavior: DcMotor.ZeroPowerBehavior
        ) {}

        override fun getMotorZeroPowerBehavior(motor: Int): DcMotor.ZeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

        override fun getMotorPowerFloat(motor: Int): Boolean = false

        override fun setMotorTargetPosition(motor: Int, position: Int) {}

        override fun getMotorTargetPosition(motor: Int): Int = 0

        override fun getMotorCurrentPosition(motor: Int): Int = 0

    }

    override fun getPortNumber(): Int = 0

    override fun setZeroPowerBehavior(zeroPowerBehavior: DcMotor.ZeroPowerBehavior) {}

    override fun getZeroPowerBehavior(): DcMotor.ZeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

    @Deprecated("Deprecated")
    override fun setPowerFloat() {}

    override fun getPowerFloat(): Boolean = false

    override fun setTargetPosition(position: Int) {}

    override fun getTargetPosition(): Int = 0

    override fun isBusy(): Boolean = false

    override fun getCurrentPosition(): Int {
        update()
        return pos().toInt()
    }

    override fun setMode(mode: DcMotor.RunMode) {}

    override fun getMode(): DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

    override fun setMotorEnable() {}

    override fun setMotorDisable() {}

    override fun isMotorEnabled(): Boolean = true

    override fun setVelocity(angularRate: Double) {}

    override fun setVelocity(angularRate: Double, unit: AngleUnit) {}

    override fun getVelocity(): Double {
        return vel()
    }

    override fun getVelocity(unit: AngleUnit): Double = 0.0

    @Deprecated("Deprecated")
    override fun setPIDCoefficients(
        mode: DcMotor.RunMode,
        pidCoefficients: PIDCoefficients
    ) {}

    override fun setPIDFCoefficients(
        mode: DcMotor.RunMode,
        pidfCoefficients: PIDFCoefficients
    ) {}

    override fun setVelocityPIDFCoefficients(p: Double, i: Double, d: Double, f: Double) {}

    override fun setPositionPIDFCoefficients(p: Double) {}

    @Deprecated("Deprecated", ReplaceWith(
        "PIDCoefficients()",
        "com.qualcomm.robotcore.hardware.PIDCoefficients"
    )
    )
    override fun getPIDCoefficients(mode: DcMotor.RunMode): PIDCoefficients = PIDCoefficients()

    override fun getPIDFCoefficients(mode: DcMotor.RunMode): PIDFCoefficients = PIDFCoefficients()

    override fun setTargetPositionTolerance(tolerance: Int) {}

    override fun getTargetPositionTolerance(): Int = 0

    override fun getCurrent(unit: CurrentUnit): Double = 0.0

    override fun getCurrentAlert(unit: CurrentUnit): Double = 0.0

    override fun setCurrentAlert(current: Double, unit: CurrentUnit) {}

    override fun isOverCurrent(): Boolean = false
}