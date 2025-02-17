@file:JvmName("MeepMeep")
package com.meep

import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.Vector2d
import com.lib.roadrunner_ext.ex
import com.lib.units.M
import com.lib.units.Pose
import com.lib.units.Pose2d
import com.lib.units.SleepAction
import com.lib.units.deg
import com.lib.units.inch
import com.lib.units.pose
import com.lib.units.s
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.MeepMeep.Background
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder

fun main() {
    System.setProperty("sun.java2d.opengl", "true")
    //val startPose = Pose(23.0.inch, -60.0.inch, 90.0.deg)

    val startPose = Pose(10.0.inch, -62.0.inch, 90.0.deg)
    val pivot = Pose(35.0.inch,-38.0.inch, 45.0.deg)
    val submerssible = Pose(10.0.inch,-38.0.inch, 90.0.deg)
    val take_specimen = Pose(38.0.inch,-50.0.inch, 0.0.deg)
    val parking = Pose(58.0.inch, -55.0.inch, 0.0.deg)
    val wallGrab = Pose(33.0.inch, -53.0.inch, 90.0.deg)
    val scoring = Pose(10.0.inch, -36.0.inch, 90.0.deg)

    val meepMeep = MeepMeep(600)

    val myBot =
        DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
            .build()

    myBot.runAction(myBot.drive.actionBuilder(startPose.pose2d).ex()
        //.afterTime(0.0, scoreSpecimen)
        .waitSeconds(3.5.s)
        .setTangent(Math.toRadians(90.0))
        .lineToY(-27.0.inch)
        //.afterTime(0.5,InstantAction{robot.outtake.clawPos = 1.0})
        .waitSeconds(5.0.s)

        /// more ig

        .setTangent(Math.toRadians(-90.0))
        .splineToLinearHeading(pivot.pose2d, pivot.heading.asDeg)
        .turnTo(Math.toRadians(-45.0))
        .setTangent(Math.toRadians(0.0))
        .lineToXLinearHeading(40.0,Math.toRadians(35.0))
        .turnTo(Math.toRadians(-50.0))
        .setTangent(0.0)
        .lineToXLinearHeading(50.0,Math.toRadians(35.0))
        .turnTo(Math.toRadians(-90.0))
        .setTangent(Math.toRadians(0.0))
        .splineToLinearHeading(wallGrab.pose2d, wallGrab.heading.asDeg)
        .strafeTo(scoring.position)
        .lineToY(-40.0.inch)
        .strafeTo(wallGrab.position)
        .strafeTo(scoring.position)
        .lineToY(-40.0.inch)
        .strafeTo(wallGrab.position)
        .strafeTo(scoring.position)
        .lineToY(-40.0.inch)
        .build()
    )

    meepMeep.setBackground(Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(myBot)
        .start()
}