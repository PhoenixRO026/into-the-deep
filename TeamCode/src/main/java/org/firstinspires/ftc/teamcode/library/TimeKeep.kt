package org.firstinspires.ftc.teamcode.library

import com.lib.units.Time

class TimeKeep {
    private var isInitialized = false

    var previousTime = Time.now()
    var currentTime = Time.now()
    inline val deltaTime get() = currentTime - previousTime

    fun resetDeltaTime() {
        if (isInitialized.not()) {
            isInitialized = true
            previousTime = Time.now()
            currentTime = previousTime
            return
        }

        previousTime = currentTime
        currentTime = Time.now()
    }
}