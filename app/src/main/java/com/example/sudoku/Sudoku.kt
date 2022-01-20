package com.example.sudoku

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

val solution1: Array<IntArray> = arrayOf(
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

val initial1: Array<IntArray> = arrayOf(
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

class Sudoku() {
    lateinit var initial: Array<IntArray>
    lateinit var solution: Array<IntArray>
    lateinit var current: Array<IntArray>

    private lateinit var win: WinHandler
    private lateinit var lose: LoseHandler
    private lateinit var timer: ExtendedCountDownTimer

    private var _score = MutableLiveData(0)
    private var _remainingMillis = MutableLiveData(GAME_TIME)

    @SuppressLint("SetTextI18n")
    constructor(
        initial: Array<IntArray>,
        solution: Array<IntArray>,
        winHandler: WinHandler,
        loseHandler: LoseHandler,
    ) : this() {

        this.initial = initial
        this.solution = solution
        this.win = winHandler
        this.lose = loseHandler

        this.current = Array(9) { IntArray(9) }

        this.timer = ExtendedCountDownTimer(
            GAME_TIME,
            1000,
            { lose() },
            {
                this._score.value = this._score.value?.minus(1)
                this._remainingMillis.value = timer.remaining()
            })

        for (row in initial.indices) {
            this.current[row] = IntArray(9)
            for (col in initial[row].indices) {
                current[row][col] = initial[row][col]
            }
        }
    }

    fun pause() {
        timer.pause()
    }

    fun resume() {
        timer.start()
    }

    // Adds the input value in the current state and returns if that is part of the solution.
    @SuppressLint("SetTextI18n")
    fun addEntry(inputNumber: Int, row: Int, col: Int) {
        current[row][col] = inputNumber
        val goodPick = inputNumber == solution[row][col]

        if (goodPick) {
            _score.value = _score.value?.plus(PROMOTION_SCORE)

            timer.extend(5000)

            if (isSolved()) {
                win()
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

    companion object {
        private const val GAME_TIME = 300000L
        private const val PROMOTION_SCORE = 30
        private const val DEMOTION_SCORE = 10
    }
}
