@file:JvmName("MeepMeepMain")

package com.dpit.meepmeep

import com.acmerobotics.roadrunner.Pose2d
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.MeepMeep.Background
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder


fun main() {
    System.setProperty("sun.java2d.opengl", "true")

    val meepMeep = MeepMeep(700)

    val myBot =
        DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .build()


    val startPose = Pose2d(50.0, 61.0, Math.toRadians(180.0))
    val basket = Pose2d(55.0,55.0, Math.toRadians(-135.0))
    val first_yellow = Pose2d(47.0,47.0, Math.toRadians(-90.0))
    val mid_yellow = Pose2d(47.0,47.0, Math.toRadians(-60.0))
    val last_yellow = Pose2d(55.0,47.0, Math.toRadians(-60.0))


    val action = myBot.drive.actionBuilder(startPose)
        .waitSeconds(3.0)
        .setTangent(-90.0)
        .splineToLinearHeading(Pose2d(first_yellow.position, first_yellow.heading), Math.toRadians(-90.0))
        .waitSeconds(3.0)
        .setTangent(Math.toRadians(-90.0))
        .setTangent(Math.toRadians(-135.0))
        .lineToXLinearHeading(basket.position.x, basket.heading)
        .waitSeconds(3.0)
        .setTangent(Math.toRadians(-135.0))
        .lineToXLinearHeading(mid_yellow.position.x, mid_yellow.heading)
        .waitSeconds(3.0)
        .setTangent(Math.toRadians(45.0))
        .lineToXLinearHeading(basket.position.x, basket.heading)
        .waitSeconds(3.0)
        .setTangent(Math.toRadians(180.0))
        .splineToLinearHeading(Pose2d(last_yellow.position, last_yellow.heading), Math.toRadians(-90.0))
        .waitSeconds(3.0)
        .setTangent(Math.toRadians(90.0))
        .splineToLinearHeading(Pose2d(basket.position, basket.heading), Math.toRadians(45.0))
        .waitSeconds(3.0)
        .build()

    myBot.runAction(action)


    meepMeep.setBackground(Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(myBot)
        .start()
}