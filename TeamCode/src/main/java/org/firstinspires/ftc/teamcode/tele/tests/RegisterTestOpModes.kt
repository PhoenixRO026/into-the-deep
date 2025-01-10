package org.firstinspires.ftc.teamcode.tele.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta

@Suppress("unused")
object RegisterTestOpModes {
    private const val GROUP: String = "test"
    private const val DISABLED: Boolean = false

    private fun metaForClass(cls: Class<out OpMode?>): OpModeMeta {
        return OpModeMeta.Builder()
            .setName(cls.simpleName)
            .setGroup(GROUP)
            .setFlavor(OpModeMeta.Flavor.TELEOP)
            .build()
    }

    @JvmStatic
    @OpModeRegistrar
    fun register(manager: OpModeManager) {
        if (DISABLED) return
        manager.register(metaForClass(DriveDirectionTest::class.java), DriveDirectionTest())
        manager.register(metaForClass(DriveOnly::class.java), DriveOnly())
        manager.register(metaForClass(IntakeOnly::class.java), IntakeOnly())
        manager.register(metaForClass(LiftOnly::class.java), LiftOnly())
        manager.register(metaForClass(OuttakeOnly::class.java), OuttakeOnly())
        manager.register(metaForClass(AnalogEncoderTest::class.java), AnalogEncoderTest())
        manager.register(metaForClass(OuttakeExtendoTuning::class.java), OuttakeExtendoTuning())
        manager.register(metaForClass(LiftTuning::class.java), LiftTuning())
        manager.register(metaForClass(IntakeExtendoTuning::class.java), IntakeExtendoTuning())
    }
}