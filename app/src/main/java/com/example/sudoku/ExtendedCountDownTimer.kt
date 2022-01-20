package com.example.sudoku

import android.os.CountDownTimer

// ExtendedCountDownTimer is a wrapper over the CountDownTimer that allows to be paused and extended.
// There is some loss of precision here (more or less equal to the tickInterval) as the internal countdown
// is only updated at every tick, so don't use this if precision is key for you, or lower the tickInterval
// as much as possible is that is affordable in your use case.
// In the Sudoku countdown precision is not so relevant and we can afford dropping some (less than 1000ms anyway)
class ExtendedCountDownTimer(
    var durationMillis : Long,
    var tickIntervalMillis: Long, var finishHandler: LoseHandler, var tickHandler : TickHandler) {

    private lateinit var timer : CountDownTimer

    fun start() {
        timer = object : CountDownTimer(durationMillis, tickIntervalMillis) {
            override fun onTick(remainingMillis: Long) {
                durationMillis = remainingMillis
                tickHandler(remainingMillis)
            }

            override fun onFinish() {
                finishHandler()
            }
        }

        timer.start()
    }

    fun extend(extensionMillis : Long) {
        timer.cancel()
        durationMillis += extensionMillis
        timer.start()
    }

    fun pause() {
        timer.cancel()
    }

    fun remaining(): Long {
        return durationMillis
    }
}