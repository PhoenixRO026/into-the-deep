package org.firstinspires.ftc.teamcode.library.roadrunnerext

import com.acmerobotics.roadrunner.Vector2d
import kotlin.math.cos
import kotlin.math.sin

fun Vector2d.rotate(radians: Double) = Vector2d(
    x * cos(radians) - y * sin(radians),
    x * sin(radians) + y * cos(radians)
)