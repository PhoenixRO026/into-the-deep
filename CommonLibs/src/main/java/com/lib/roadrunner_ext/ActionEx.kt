package com.lib.roadrunner_ext

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.SequentialAction
import com.lib.units.Duration
import com.lib.units.SleepAction

fun Action.delayedBy(duration: Duration) = SequentialAction(SleepAction(duration), this)