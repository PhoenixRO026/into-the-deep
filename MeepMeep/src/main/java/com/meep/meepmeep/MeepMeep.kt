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

fun main() {
    System.setProperty("sun.java2d.opengl", "true")

    val meepMeep = MeepMeep(600)

    val startPose = Pose(-36.inch, -60.inch, 90.deg)
    val basketPose = Pose(-54.inch, -54.inch, 45.deg)
    val firstYellowPose = Pose(-48.inch, -50.inch, 90.deg)
    val secondYellowPose = Pose(-52.inch, -50.inch, 90.deg)
    val thirdYellowSample = Distance2d(-68.5.inch, -25.5.inch)
    val thirdYellowPose = Distance2d(-56.inch, -50.inch).headingTowards(thirdYellowSample)
    val parkPose = Pose(-30.inch, -12.inch, 90.deg)

    val myBot =
        DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
            .build()

    myBot.runAction(myBot.drive.actionBuilder(startPose.pose2d).ex()
        .strafeToLinearHeading(basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(firstYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(secondYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(thirdYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(parkPose)
        .build()
    )

    meepMeep.setBackground(Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(myBot)
        .start()
}