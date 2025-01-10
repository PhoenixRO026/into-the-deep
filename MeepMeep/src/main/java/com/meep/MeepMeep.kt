@file:JvmName("MeepMeep")
package com.meep

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.lib.roadrunner_ext.ex
import com.lib.units.Pose
import com.lib.units.deg
import com.lib.units.inch
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.MeepMeep.Background
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder

fun main() {
    System.setProperty("sun.java2d.opengl", "true")

    val startPose = Pose(10.0.inch, -57.0.inch, 90.0.deg)
    val submerssible = Pose(10.0.inch, -36.0.inch, 90.0.deg)
    val take_specimen = Pose(38.0.inch, -50.0.inch, 0.0.deg)

    val meepMeep = MeepMeep(800)

    val myBot =
        DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
            .build()

    myBot.runAction(
        myBot.drive.actionBuilder(Pose2d(0.0, 0.0, 0.0)).ex()
            .setTangent(Math.toRadians(90.0))
            .lineToY(-40.0)
            .afterTime(0.0, SequentialAction(

            )
            )

            //.afterTime(0.0, functions.scoreSecondBar())
            .waitSeconds(1.0)
            .setTangent(Math.toRadians(0.0))
            .lineToXLinearHeading(34.0,Math.toRadians(45.0))
            //.afterTime(0.0, functions.grab())
            .waitSeconds(1.0)
            .turnTo(Math.toRadians(-50.0))
            //.afterTime(0.0, functions.release())
            .waitSeconds(1.0)
            .setTangent(Math.toRadians(0.0))
            .lineToXLinearHeading(40.0,Math.toRadians(35.0))
            //.afterTime(0.0, functions.grab())
            .waitSeconds(1.0)
            .turnTo(Math.toRadians(-50.0))
            //.afterTime(0.0, functions.release())
            .waitSeconds(1.0)
            .setTangent(0.0)
            .lineToXLinearHeading(50.0,Math.toRadians(35.0))
            //.afterTime(0.0, functions.grab())
            .waitSeconds(1.0)
            .turnTo(Math.toRadians(-90.0))
            .setTangent(Math.toRadians(180.0))
            .lineToX(38.0)
            //.wallGrab(arm, claw)
            .strafeTo(take_specimen.position)
            .waitSeconds(1.0)
            //.afterTime(0.0, functions.scoreSecondBar())
            .strafeTo(submerssible.position)
            .waitSeconds(1.0)
            //.wallGrab(arm, claw)
            .strafeTo(take_specimen.position)
            //.afterTime(0.0, functions.scoreSecondBar())
            .strafeTo(submerssible.position)
            //.wallGrab(arm, claw)
            .strafeTo(take_specimen.position)
            //.afterTime(0.0, functions.scoreSecondBar())
            .strafeTo(submerssible.position)
            .build()
    )

    meepMeep.setBackground(Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(myBot)
        .start()
}