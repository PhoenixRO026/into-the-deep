@file:JvmName("MeepMeep")
package com.meep.meepmeep

import com.lib.roadrunner_ext.ex
import com.lib.units.Distance2d
import com.lib.units.Pose
import com.lib.units.deg
import com.lib.units.inch
import com.lib.units.s
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.MeepMeep.Background
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder

data object Red {
    val startPose = Pose(-36.inch, -60.inch, 90.deg)
    val basketPose = Pose(-54.inch, -54.inch, 45.deg)
    val firstYellowSample = Distance2d(-48.inch, -25.5.inch)
    val firstYellowPose = Distance2d(-48.inch, -50.inch).headingTowards(firstYellowSample)
    val secondYellowSample = Distance2d(-58.5.inch, -25.5.inch)
    val secondYellowPose = Distance2d(-52.inch, -50.inch).headingTowards(secondYellowSample)
    val thirdYellowSample = Distance2d(-68.5.inch, -25.5.inch)
    val thirdYellowPose = Distance2d(-56.inch, -50.inch).headingTowards(thirdYellowSample)
    val parkPose = Pose(-24.inch, -12.inch, 0.deg)
}

data object Blue {
    val startPose = Pose(36.inch, 60.inch, -90.deg)
    val basketPose = Pose(54.inch, 54.inch, -135.deg)
    val firstYellowSample = Distance2d(48.inch, 25.5.inch)
    val firstYellowPose = Distance2d(48.inch, 50.inch).headingTowards(firstYellowSample)
    val secondYellowSample = Distance2d(58.5.inch, 25.5.inch)
    val secondYellowPose = Distance2d(52.inch, 50.inch).headingTowards(secondYellowSample)
    val thirdYellowSample = Distance2d(68.5.inch, 25.5.inch)
    val thirdYellowPose = Distance2d(56.inch, 50.inch).headingTowards(thirdYellowSample)
    val parkPose = Pose(24.inch, 12.inch, 180.deg)
}

fun main() {
    System.setProperty("sun.java2d.opengl", "true")

    val meepMeep = MeepMeep(600)

    val redBot =
        DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
            .build()

    val blueBot =
        DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
            .build()

    redBot.runAction(redBot.drive.actionBuilder(Red.startPose.pose2d).ex()
        .strafeToLinearHeading(Red.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Red.firstYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Red.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Red.secondYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Red.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Red.thirdYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Red.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Red.parkPose)
        .build()
    )

    blueBot.runAction(blueBot.drive.actionBuilder(Blue.startPose.pose2d).ex()
        .strafeToLinearHeading(Blue.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Blue.firstYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Blue.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Blue.secondYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Blue.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Blue.thirdYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Blue.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(Blue.parkPose)
        .build()
    )

    meepMeep.setBackground(Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(redBot)
        .addEntity(blueBot)
        .start()
}