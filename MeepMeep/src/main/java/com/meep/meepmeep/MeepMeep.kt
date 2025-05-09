@file:JvmName("MeepMeep")
package com.meep.meepmeep

import com.lib.roadrunner_ext.delayedBy
import com.lib.roadrunner_ext.ex
import com.lib.units.Distance2d
import com.lib.units.Pose
import com.lib.units.cm
import com.lib.units.deg
import com.lib.units.inch
import com.lib.units.s
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.MeepMeep.Background
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity

data object RedBasket {
     val startPose = Pose(-38.inch, -61.inch, 90.deg)
     val basketPose = Pose(-52.inch, -52.inch, 45.deg)
     val firstYellowSample = Distance2d(-48.2.inch, -25.4.inch)
     val firstYellowPose = Distance2d(-48.inch, -51.inch).headingTowards(firstYellowSample)
     val secondYellowSample = Distance2d(-58.3.inch, -26.1.inch)
     val secondYellowPose = Distance2d(-52.inch, -50.inch).headingTowards(secondYellowSample)
     val thirdYellowSample = Distance2d(-68.9.inch, -25.6.inch)
     val thirdYellowPose = Distance2d(-47.inch, -45.inch).headingTowards(thirdYellowSample)
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
    val startPose = Pose(14.cm, -63.inch, 90.deg)
    val firstSpecimenBeforePos = Pose(4.inch, -40.inch, 90.deg)
    val firstSpecimenPos = Pose(-1.5.inch, -30.5.inch, 90.deg)
    val secondSpecimenPos = Pose(0.inch, -30.5.inch, 90.deg)
    val thirdSpecimenPos = Pose(1.5.inch, -30.5.inch, 90.deg)
    val forthSpecimenPos = Pose(3.inch, -30.5.inch, 90.deg)
    val red1Pos = Distance2d(48.inch, -27.inch)
    val red2Pos = Distance2d(58.5.inch, -26.inch)
    val red3Pos = Distance2d(68.5.inch, -25.5.inch)
    val zonePos = Distance2d(45.1.inch, -67.2.inch)
    val zonePoze3 = Distance2d(50.inch, -67.2.inch)
    val firstSamplePos = Distance2d(26.5.inch, -41.inch).headingTowards(red1Pos)
    val secondSamplePos = Distance2d(33.inch, -37.5.inch).headingTowards(red2Pos)
    val thirdSamplePos = Distance2d(41.inch, -37.inch).headingTowards(red3Pos)
    val firstKickPos = Distance2d(30.inch, -50.inch).headingTowards(zonePos)
    val secondKickPos = Distance2d(34.inch, -50.inch).headingTowards(zonePos)
    val thirdKickPos = Distance2d(38.inch, -50.inch).headingTowards(zonePos)
    val takeSpecimenPos = Pose(40.inch, -60.inch, 90.deg)
}

fun main() {
    System.setProperty("sun.java2d.opengl", "true")

    val meepMeep = MeepMeep(600)

    val redBot =
        DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 100.0, Math.PI * 3.0 / 2.0 , Math.PI * 2, 14.5)
            .setDimensions(32.5.cm.asInch, 33.cm.asInch)
            .build()

    val blueBot =
        DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
            .setDimensions(32.5.cm.asInch, 33.cm.asInch)
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

        .setTangent(-90.deg)
        .splineToLinearHeading(RedSpecimen.firstSamplePos, 0.deg)

        .turnTo(RedSpecimen.firstSamplePos.position.headingTowards(RedSpecimen.zonePos).heading)

        .strafeToLinearHeading(RedSpecimen.secondSamplePos)

        .turnTo(RedSpecimen.secondSamplePos.position.headingTowards(RedSpecimen.zonePos).heading)

        .strafeToLinearHeading(RedSpecimen.thirdSamplePos)

        .turnTo(RedSpecimen.thirdSamplePos.position.headingTowards(RedSpecimen.zonePoze3).heading)

        //start to specimen
        .setTangent(-90.deg)
        .splineToSplineHeading(RedSpecimen.takeSpecimenPos, -90.deg)

        .setTangent(165.deg)
        .splineToLinearHeading(RedSpecimen.secondSpecimenPos, 90.deg)

        .setTangent(-90.deg)
        .splineToLinearHeading(RedSpecimen.takeSpecimenPos, -90.deg)

        .setTangent(165.deg)
        .splineToLinearHeading(RedSpecimen.thirdSpecimenPos, 90.deg)

        .setTangent(-90.deg)
        .splineToLinearHeading(RedSpecimen.takeSpecimenPos, -90.deg)

        .setTangent(165.deg)
        .splineToLinearHeading(RedSpecimen.forthSpecimenPos, 90.deg)
        .build()
    )

    blueBot.runAction(blueBot.drive.actionBuilder(BlueBasket.startPose.pose2d).ex()
        .build()
    )
}

fun basketAuto(redBot: RoadRunnerBotEntity, blueBot: RoadRunnerBotEntity) {
    redBot.runAction(redBot.drive.actionBuilder(RedBasket.startPose.pose2d).ex()
        .strafeToLinearHeading(RedBasket.basketPose)

        .strafeToLinearHeading(RedBasket.firstYellowPose)
        .strafeToLinearHeading(RedBasket.basketPose)

        .strafeToLinearHeading(RedBasket.secondYellowPose)
        .strafeToLinearHeading(RedBasket.basketPose)

        .strafeToLinearHeading(RedBasket.thirdYellowPose)
        .strafeToLinearHeading(RedBasket.basketPose)

        .setTangent(90.deg)
        .splineToSplineHeading(RedBasket.parkPose - 20.cm.x,0.0.deg)
        .lineToX(RedBasket.parkPose.position.x)
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