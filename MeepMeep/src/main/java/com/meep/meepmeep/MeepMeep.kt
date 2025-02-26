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
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity

data object RedBasket {
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

data object BlueBasket {
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

data object RedSpecimen {
    val startPose = Pose(4.inch, -60.inch, 90.deg)
    val firstSpecimenPos = Pose(4.inch, -36.inch, 90.deg)
    val red1Pos = Distance2d(48.inch, -25.5.inch)
    val red2Pos = Distance2d(58.5.inch, -25.5.inch)
    val red3Pos = Distance2d(68.5.inch, -25.5.inch)
    val zonePos = Distance2d(60.inch, -60.inch)
    val firstSamplePos = Distance2d(30.inch, -40.inch).headingTowards(red1Pos)
    val secondSamplePos = Distance2d(34.inch, -40.inch).headingTowards(red2Pos)
    val thirdSamplePos = Distance2d(38.inch, -40.inch).headingTowards(red3Pos)
    val firstKickPos = Distance2d(30.inch, -50.inch).headingTowards(zonePos)
    val secondKickPos = Distance2d(34.inch, -50.inch).headingTowards(zonePos)
    val thirdKickPos = Distance2d(38.inch, -50.inch).headingTowards(zonePos)
    val takeSpecimenPos = Pose(34.inch, -56.inch, 90.deg)
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

    //basketAuto(redBot, blueBot)
    specimenAuto(redBot, blueBot)

    meepMeep.setBackground(Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(redBot)
        .addEntity(blueBot)
        .start()
}

fun specimenAuto(redBot: RoadRunnerBotEntity, blueBot: RoadRunnerBotEntity) {
    redBot.runAction(redBot.drive.actionBuilder(RedSpecimen.startPose.pose2d).ex()
        .strafeToLinearHeading(RedSpecimen.firstSpecimenPos)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedSpecimen.firstSamplePos)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedSpecimen.firstKickPos)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedSpecimen.secondSamplePos)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedSpecimen.secondKickPos)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedSpecimen.thirdSamplePos)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedSpecimen.thirdKickPos)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedSpecimen.takeSpecimenPos)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedSpecimen.firstSpecimenPos)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedSpecimen.takeSpecimenPos)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedSpecimen.firstSpecimenPos)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedSpecimen.takeSpecimenPos)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedSpecimen.firstSpecimenPos)
        .waitSeconds(1.s)
        .build()
    )

    blueBot.runAction(blueBot.drive.actionBuilder(BlueBasket.startPose.pose2d).ex()
        .build()
    )
}

fun basketAuto(redBot: RoadRunnerBotEntity, blueBot: RoadRunnerBotEntity) {
    redBot.runAction(redBot.drive.actionBuilder(RedBasket.startPose.pose2d).ex()
        .strafeToLinearHeading(RedBasket.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedBasket.firstYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedBasket.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedBasket.secondYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedBasket.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedBasket.thirdYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedBasket.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(RedBasket.parkPose)
        .build()
    )

    blueBot.runAction(blueBot.drive.actionBuilder(BlueBasket.startPose.pose2d).ex()
        .strafeToLinearHeading(BlueBasket.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.firstYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.secondYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.thirdYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.parkPose)
        .build()
    )
}