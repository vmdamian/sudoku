package com.example.sudoku

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

class Sudoku {
    private val level = getRandomLevel()
    var current = level.first
    val solution = level.second

    private var _score = MutableLiveData(0)
    private var _remainingMillis = MutableLiveData(GAME_TIME)
    private var _finishStatus = MutableLiveData(0)

    private val timer : ExtendedCountDownTimer = ExtendedCountDownTimer(
        GAME_TIME,
        { this._finishStatus.value = GAME_LOST },
        {
            this._score.value = this._score.value?.minus(1)
            remainingTimeUpdater()
        })
    
    private fun remainingTimeUpdater() {
        this._remainingMillis.value = timer.remaining()
    }

    fun pause() {
        timer.pause()
    }

    fun resume() {
        timer.start()
    }

    // Adds the input value in the current state.
    @SuppressLint("SetTextI18n")
    fun addEntry(inputNumber: Int, row: Int, col: Int) {
        current[row][col] = inputNumber
        val goodPick = inputNumber == solution[row][col]

        if (goodPick) {
            _score.value = _score.value?.plus(PROMOTION_SCORE)

            timer.extend(5000)

            if (isSolved()) {
                _finishStatus.value = GAME_WON
            }
        } else {
            _score.value = _score.value?.minus(DEMOTION_SCORE)
        }

        return
    }

    // Checks if the current status of the game is solved (matches solution).
    private fun isSolved(): Boolean {
        for (row in current.indices) {
            for (col in current[row].indices) {
                if (current[row][col] != solution[row][col]) {
                    return false
                }
            }
        }
        return true
    }

    fun score() : LiveData<Int> {
        return _score
    }

    fun remainingMillis() : LiveData<Long> {
        return _remainingMillis
    }

    fun finishStatus() : LiveData<Int> {
        return _finishStatus
    }
    
    companion object {
        private const val GAME_TIME = 300000L
        private const val PROMOTION_SCORE = 30
        private const val DEMOTION_SCORE = 10
        const val GAME_WON = 69
        const val GAME_LOST = 42

        private val  initial1: Array<IntArray> = arrayOf(
            intArrayOf(0, 0, 9, 0, 0, 0, 0, 0, 8),
            intArrayOf(1, 5, 0, 0, 8, 0, 3, 0, 0),
            intArrayOf(0, 0, 6, 7, 2, 0, 0, 0, 0),
            intArrayOf(0, 0, 8, 0, 0, 3, 4, 0, 0),
            intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 1),
            intArrayOf(5, 4, 7, 8, 0, 0, 9, 0, 3),
            intArrayOf(7, 2, 0, 0, 9, 1, 0, 0, 0),
            intArrayOf(0, 9, 0, 3, 0, 0, 0, 0, 7),
            intArrayOf(0, 8, 3, 0, 0, 7, 5, 1, 9)
        )

        private val solution1: Array<IntArray> = arrayOf(
            intArrayOf(2, 7, 9, 1, 3, 4, 6, 5, 8),
            intArrayOf(1, 5, 4, 9, 8, 6, 3, 7, 2),
            intArrayOf(8, 3, 6, 7, 2, 5, 1, 9, 4),
            intArrayOf(9, 1, 8, 6, 7, 3, 4, 2, 5),
            intArrayOf(3, 6, 2, 5, 4, 9, 7, 8, 1),
            intArrayOf(5, 4, 7, 8, 1, 2, 9, 6, 3),
            intArrayOf(7, 2, 5, 4, 9, 1, 8, 3, 6),
            intArrayOf(6, 9, 1, 3, 5, 8, 2, 4, 7),
            intArrayOf(4, 8, 3, 2, 6, 7, 5, 1, 9)
        )

        private val levels : Array<Pair<Array<IntArray>, Array<IntArray>>> = arrayOf(
            Pair(initial1, solution1)
        )

        private fun getRandomLevel() : Pair<Array<IntArray>, Array<IntArray>> {
            return levels[Random.nextInt(levels.size)]
        }
    }
}
