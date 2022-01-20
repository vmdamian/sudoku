package com.example.sudoku

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kotlin.random.Random

class Sudoku {
    private val level = getRandomLevel()
    var current = deepCopy(level.first)
    val solution = deepCopy(level.second)

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

        private val initial2: Array<IntArray> = arrayOf(
            intArrayOf(0, 5, 0, 3, 8, 0, 0, 2, 0),
            intArrayOf(0, 0, 6, 0, 0, 2, 0, 3, 4),
            intArrayOf(2, 0, 0, 0, 0, 4, 0, 0, 0),
            intArrayOf(6, 1, 4, 7, 0, 5, 8, 0, 0),
            intArrayOf(0, 0, 3, 4, 0, 0, 5, 0, 0),
            intArrayOf(0, 0, 0, 0, 1, 0, 4, 0, 7),
            intArrayOf(0, 0, 0, 5, 7, 0, 2, 0, 0),
            intArrayOf(7, 0, 2, 8, 3, 9, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 3, 7, 0)
        )

        private val solution2: Array<IntArray> = arrayOf(
            intArrayOf(4, 5, 1, 3, 8, 7, 9, 2, 6),
            intArrayOf(8, 9, 6, 1, 5, 2, 7, 3, 4),
            intArrayOf(2, 3, 7, 6, 9, 4, 1, 8, 5),
            intArrayOf(6, 1, 4, 7, 2, 5, 8, 9, 3),
            intArrayOf(9, 7, 3, 4, 6, 8, 5, 1, 2),
            intArrayOf(5, 2, 8, 9, 1, 3, 4, 6, 7),
            intArrayOf(3, 6, 9, 5, 7, 1, 2, 4, 8),
            intArrayOf(7, 4, 2, 8, 3, 9, 6, 5, 1),
            intArrayOf(1, 8, 5, 2, 4, 6, 3, 7, 9)
        )

        private val initial3: Array<IntArray> = arrayOf(
            intArrayOf(0, 0, 0, 0, 1, 0, 4, 0, 0),
            intArrayOf(0, 4, 9, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 1, 8, 0, 7, 0, 0, 0, 9),
            intArrayOf(0, 6, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(8, 7, 0, 3, 0, 5, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 8, 4, 5, 7, 0),
            intArrayOf(6, 0, 7, 0, 0, 3, 0, 2, 0),
            intArrayOf(0, 0, 3, 0, 0, 6, 0, 0, 5),
            intArrayOf(9, 2, 0, 8, 0, 0, 0, 1, 0)
        )

        private val solution3: Array<IntArray> = arrayOf(
            intArrayOf(2, 3, 6, 5, 1, 9, 4, 8, 7),
            intArrayOf(7, 4, 9, 6, 3, 8, 2, 5, 1),
            intArrayOf(5, 1, 8, 4, 7, 2, 3, 6, 9),
            intArrayOf(4, 6, 5, 7, 9, 1, 8, 3, 2),
            intArrayOf(8, 7, 2, 3, 6, 5, 1, 9, 4),
            intArrayOf(3, 9, 1, 2, 8, 4, 5, 7, 6),
            intArrayOf(6, 5, 7, 1, 4, 3, 9, 2, 8),
            intArrayOf(1, 8, 3, 9, 2, 6, 7, 4, 5),
            intArrayOf(9, 2, 4, 8, 5, 7, 6, 1, 3)
        )

        private val initial4: Array<IntArray> = arrayOf(
            intArrayOf(9, 0, 0, 0, 5, 0, 0, 0, 8),
            intArrayOf(5, 3, 1, 6, 0, 0, 2, 0, 0),
            intArrayOf(4, 0, 0, 0, 0, 0, 0, 9, 0),
            intArrayOf(0, 8, 0, 0, 3, 9, 0, 0, 0),
            intArrayOf(3, 0, 0, 0, 0, 2, 8, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 4, 0, 0, 3),
            intArrayOf(0, 0, 9, 0, 8, 0, 0, 7, 0),
            intArrayOf(8, 0, 0, 0, 0, 1, 0, 2, 6),
            intArrayOf(0, 6, 2, 0, 0, 0, 0, 0, 0)
        )

        private val solution4: Array<IntArray> = arrayOf(
            intArrayOf(9, 2, 6, 4, 5, 7, 1, 3, 8),
            intArrayOf(5, 3, 1, 6, 9, 8, 2, 4, 7),
            intArrayOf(4, 7, 8, 1, 2, 3, 6, 9, 5),
            intArrayOf(6, 8, 7, 5, 3, 9, 4, 1, 2),
            intArrayOf(3, 1, 4, 7, 6, 2, 8, 5, 9),
            intArrayOf(2, 9, 5, 8, 1, 4, 7, 6, 3),
            intArrayOf(1, 5, 9, 2, 8, 6, 3, 7, 4),
            intArrayOf(8, 4, 3, 9, 7, 1, 5, 2, 6),
            intArrayOf(7, 6, 2, 3, 4, 5, 9, 8, 1)
        )

        private val initial5: Array<IntArray> = arrayOf(
            intArrayOf(0, 6, 0, 0, 0, 0, 9, 0, 3),
            intArrayOf(0, 8, 2, 0, 0, 0, 0, 0, 7),
            intArrayOf(9, 0, 0, 0, 6, 0, 0, 0, 1),
            intArrayOf(2, 0, 0, 0, 0, 4, 0, 7, 5),
            intArrayOf(0, 0, 0, 0, 0, 5, 0, 0, 9),
            intArrayOf(5, 9, 0, 0, 8, 7, 2, 1, 6),
            intArrayOf(0, 3, 0, 0, 4, 6, 1, 5, 0),
            intArrayOf(6, 0, 8, 7, 0, 1, 0, 9, 4),
            intArrayOf(4, 0, 0, 0, 0, 0, 0, 0, 8)
        )

        private val solution5: Array<IntArray> = arrayOf(
            intArrayOf(1, 6, 5, 4, 7, 8, 9, 2, 3),
            intArrayOf(3, 8, 2, 5, 1, 9, 6, 4, 7),
            intArrayOf(9, 4, 7, 2, 6, 3, 5, 8, 1),
            intArrayOf(2, 1, 3, 6, 9, 4, 8, 7, 5),
            intArrayOf(8, 7, 6, 1, 2, 5, 4, 3, 9),
            intArrayOf(5, 9, 4, 3, 8, 7, 2, 1, 6),
            intArrayOf(7, 3, 9, 8, 4, 6, 1, 5, 2),
            intArrayOf(6, 2, 8, 7, 5, 1, 3, 9, 4),
            intArrayOf(4, 5, 1, 9, 3, 2, 7, 6, 8)
        )

        private val levels : Array<Pair<Array<IntArray>, Array<IntArray>>> = arrayOf(
            Pair(initial1, solution1),
            Pair(initial2, solution2),
            Pair(initial3, solution3),
            Pair(initial4, solution4),
            Pair(initial5, solution5)
        )

        private fun getRandomLevel() : Pair<Array<IntArray>, Array<IntArray>> {
            return levels[Random.nextInt(levels.size)]
        }

        private fun deepCopy(level: Array<IntArray>) : Array<IntArray> {
            return Gson().fromJson(Gson().toJson(level), Array<IntArray>::class.java)
        }
    }
}
