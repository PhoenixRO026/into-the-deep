package org.firstinspires.ftc.teamcode.library

import com.acmerobotics.roadrunner.now

class TimeKeep {

    private var isInitialized = false

    var previousTime = now()
    var currentTime = now()
    inline val deltaTime get() = (currentTime - previousTime) * 1000

    fun resetDeltaTime() {
        if (isInitialized.not()) {
            isInitialized = true
            previousTime = now()
            currentTime = now()
            return
        }

        previousTime = currentTime
        currentTime = now()
    }
}