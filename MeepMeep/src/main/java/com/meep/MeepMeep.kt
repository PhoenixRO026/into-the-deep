@file:JvmName("MeepMeep")
package com.meep

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

    val startPose = Pose(-47.0.inch, -61.0.inch, -90.0.deg)
    val  basket = Pose(-55.0.inch, -55.0.inch, 45.0.deg)
    val pivot = Pose(-55.0.inch, -50.0.inch, -135.deg)


    val meepMeep = MeepMeep(600)

    val myBot =
        DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
            .build()

    myBot.runAction(myBot.drive.actionBuilder(startPose.pose2d).ex()
        .setTangent(Math.toRadians(90.0))
        .splineTo(pivot.position, pivot.heading)
        .waitSeconds(3.0)

        .lineToY(-47.0)
        .setTangent(-135.0.deg + 180.0.deg)
        .lineToXLinearHeading(basket.position.x, basket.heading)
        .waitSeconds(3.0)
        .setTangent(-90.0.deg + 180.0.deg)


        .waitSeconds(3.0)
        .setTangent(-135.0.deg + 180.0.deg)
        .lineToXLinearHeading(basket.position.x, basket.heading)

        .waitSeconds(3.0)
        .setTangent(-135.0.deg + 180.0.deg)


        .waitSeconds(3.0)
        .setTangent(45.0.deg + 180.0.deg)
        .lineToXLinearHeading(basket.position.x, basket.heading)

        .waitSeconds(3.0)
        .setTangent(180.0.deg + 180.0.deg)


        .waitSeconds(3.0)
        .setTangent(90.0.deg + 180.0.deg)
        .splineToLinearHeading(Pose(basket.position, basket.heading), 45.0.deg + 180.0.deg)
        .waitSeconds(3.0)
        .build()
    )

    meepMeep.setBackground(Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(myBot)
        .start()
}