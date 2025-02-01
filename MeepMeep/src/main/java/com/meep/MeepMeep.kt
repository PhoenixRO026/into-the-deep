@file:JvmName("MeepMeep")
package com.meep

import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.lib.roadrunner_ext.ex
import com.lib.units.M
import com.lib.units.Pose
import com.lib.units.Pose2d
import com.lib.units.SleepAction
import com.lib.units.deg
import com.lib.units.inch
import com.lib.units.s
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.MeepMeep.Background
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder

fun main() {
    System.setProperty("sun.java2d.opengl", "true")

    val startPose = Pose(10.0.inch, -57.0.inch, 90.0.deg)
    val wallGrab = Pose(33.0.inch, -53.0.inch, 90.0.deg)
    val scoring = Pose(10.0.inch, -36.0.inch, 90.0.deg)

    val meepMeep = MeepMeep(600)

    val myBot =
        DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
            .build()

    myBot.runAction(myBot.drive.actionBuilder(startPose.pose2d).ex()
        ///PRELOAD
        .setTangent(Math.toRadians(90.0))
        .lineToY(-36.0.inch)

        .setTangent(Math.toRadians(-90.0))
        .lineToY(-40.0.inch)

        .setTangent(Math.toRadians(0.0))
        .lineToXLinearHeading(34.0,Math.toRadians(45.0))

        .turnTo(Math.toRadians(-50.0))

        .setTangent(Math.toRadians(0.0))
        .lineToXLinearHeading(40.0,Math.toRadians(35.0))

        .turnTo(Math.toRadians(-50.0))

        .setTangent(0.0)
        .lineToXLinearHeading(50.0,Math.toRadians(35.0))

        .turnTo(Math.toRadians(-90.0))

        .setTangent(Math.toRadians(180.0))

        .strafeTo(wallGrab.position)

        .strafeTo(scoring.position)

        .lineToY(-40.0.inch)

        .strafeTo(wallGrab.position)

        .strafeTo(scoring.position)

        .lineToY(-40.0.inch)

        .strafeTo(wallGrab.position)

        .strafeTo(scoring.position)

        .lineToY(-40.0.inch)
/*
        .strafeTo(wallGrab.position)

        .strafeTo(scoring.position)

        .lineToY(-40.0.inch)
        */.build()
    )

    meepMeep.setBackground(Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(myBot)
        .start()
}