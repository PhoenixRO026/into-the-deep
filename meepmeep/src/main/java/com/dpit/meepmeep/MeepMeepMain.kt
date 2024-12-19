@file:JvmName("MeepMeepMain")

package com.dpit.meepmeep

import com.acmerobotics.roadrunner.Pose2d
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.MeepMeep.Background
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder


fun main() {
    System.setProperty("sun.java2d.opengl", "true")

    val meepMeep = MeepMeep(800)

    val myBot =
        DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .build()

    val startPose = Pose2d(-36.0, -60.0, Math.toRadians(90.0))
    val corner = Pose2d(-55.0, -52.0, Math.toRadians(0.0))
    val pose1 = Pose2d(-45.0,-15.0,Math.toRadians(0.0))

    val action = myBot.drive.actionBuilder(startPose)
        .setTangent(Math.toRadians(90.0))
        .lineToY(-13.0)
        .setTangent(Math.toRadians(180.0))
        .lineToX(-46.0)
        .setTangent(Math.toRadians(-90.0))
        .splineTo(corner.position,Math.toRadians(240.0))
        .setTangent(240.0)
        .splineTo(pose1.position, Math.toRadians(98.0))
        .lineToX(-30.0)

        .build()

    myBot.runAction(action)

    meepMeep.setBackground(Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(myBot)
        .start()
}