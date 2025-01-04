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

    val startPose = Pose2d(-10.0, 57.0, Math.toRadians(-90.0))
    val submerssible = Pose2d(-10.0,36.0,Math.toRadians(-90.0))
    val take_specimen = Pose2d(-38.0,44.0,Math.toRadians(0.0))


    val action = myBot.drive.actionBuilder(startPose)
        .setTangent(Math.toRadians(90.0))
        .lineToY(36.0)
        .strafeTo(take_specimen.position)
        .lineToX(-48.0)
        .waitSeconds(1.0)
        .setTangent(Math.toRadians(0.0))
        .lineToX(-58.5)
        .waitSeconds(1.0)
        .setTangent(Math.toRadians(0.0))
        .lineToX(-57.0)
        .turnTo(Math.toRadians(-130.0))
        .waitSeconds(1.0)
        .turnTo(Math.toRadians(90.0))
        .waitSeconds(1.0)
        .setTangent(Math.toRadians(180.0))
        .lineToX(-38.0)
        .waitSeconds(1.0)
        .strafeTo(submerssible.position)
        .waitSeconds(1.0)
        .strafeTo(take_specimen.position)
        .waitSeconds(1.0)
        .strafeTo(submerssible.position)
        .waitSeconds(1.0)
        .strafeTo(take_specimen.position)
        .waitSeconds(1.0)
        .strafeTo(submerssible.position)
        .waitSeconds(1.0)
        .build()

    myBot.runAction(action)

    meepMeep.setBackground(Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(myBot)
        .start()
}