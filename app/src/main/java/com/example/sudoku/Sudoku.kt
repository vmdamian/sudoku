package com.example.sudoku

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.EditText
import android.widget.TextView

val solution1 : Array<IntArray> = arrayOf(
    intArrayOf(2, 7, 9, 1, 3, 4, 6, 5, 8),
    intArrayOf(1, 5, 4, 9, 8, 6, 3, 7, 2),
    intArrayOf(8, 3, 6, 7, 2, 5, 1, 9, 4),
    intArrayOf(9, 1, 8, 6, 7, 3, 4, 2, 5),
    intArrayOf(3, 6, 2, 5, 4, 9, 7, 8, 1),
    intArrayOf(5, 4, 7, 8, 1, 2, 9, 6, 3),
    intArrayOf(7, 2, 5, 4, 9, 1, 8, 3, 6),
    intArrayOf(6, 9, 1, 3, 5, 8, 2, 4, 7),
    intArrayOf(4, 8, 3, 2, 6, 7, 5, 1, 9))

val initial1 : Array<IntArray> = arrayOf(
    intArrayOf(0, 0, 9, 0, 0, 0, 0, 0, 8),
    intArrayOf(1, 5, 0, 0, 8, 0, 3, 0, 0),
    intArrayOf(0, 0, 6, 7, 2, 0, 0, 0, 0),
    intArrayOf(0, 0, 8, 0, 0, 3, 4, 0, 0),
    intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 1),
    intArrayOf(5, 4, 7, 8, 0, 0, 9, 0, 3),
    intArrayOf(7, 2, 0, 0, 9, 1, 0, 0, 0),
    intArrayOf(0, 9, 0, 3, 0, 0, 0, 0, 7),
    intArrayOf(0, 8, 3, 0, 0, 7, 5, 1, 9))

class Sudoku() {
    // Available difficulties.
    enum class Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    // Timings (in ms) for each difficulty.
     private val difficultyTimings = mapOf(
        Difficulty.EASY to 300000L,
        Difficulty.MEDIUM to 200000L,
        Difficulty.HARD to 100000L,
    )

    // Added score for a good pick for each difficulty.
    private val difficultyPromotions = mapOf(
        Difficulty.EASY to 30,
        Difficulty.MEDIUM to 20,
        Difficulty.HARD to 10,
    )

    // Removed score for a good pick for each difficulty.
    private val difficultyDemotions = mapOf(
        Difficulty.EASY to 10,
        Difficulty.MEDIUM to 20,
        Difficulty.HARD to 30,
    )

    // Actual game logic
    lateinit var initial : Array<IntArray>
    private lateinit var solution : Array<IntArray>
    lateinit var current : Array<IntArray>
    private lateinit var difficulty : Difficulty
    var score : Int = 0

    // Visuals.
    private lateinit var scoreText : TextView
    private lateinit var timeText : TextView
    private lateinit var boxes : Array<Array<EditText>>

    private lateinit var win : WinHandler
    private lateinit var lose : LoseHandler
    private lateinit var timer : ExtendedCountDownTimer

    @SuppressLint("SetTextI18n")
    constructor(initial : Array<IntArray>,
                solution: Array<IntArray>,
                difficulty: Difficulty,
                scoreText: TextView,
                timeText: TextView,
                boxes: Array<Array<EditText>>,
                winHandler: WinHandler,
                loseHandler: LoseHandler,
    ) : this() {
        this.initial = initial
        this.solution = solution
        this.difficulty = difficulty
        this.scoreText = scoreText
        this.timeText = timeText
        this.boxes = boxes
        this.win = winHandler
        this.lose = loseHandler

        this.current = Array(9) { IntArray(9) }

        this.timer = ExtendedCountDownTimer(difficultyTimings[Difficulty.EASY]!!, 1000, {lose()}, { remainingMillis ->
            this.timeText.text = "${remainingMillis / 1000}"
            this.score -= 1
            this.scoreText.text = "Score: $score"
        } )

        for (row in initial.indices) {
            this.current[row] = IntArray(9)
            for (col in initial[row].indices) {
                current[row][col] = initial[row][col]
            }
        }

        // Setup visuals.
        this.scoreText.text = "Score: $score"
    }

    fun pause() {
        timer.pause()
    }

    fun resume() {
        timer.start()
    }

    // Adds the input value in the current state and returns if that is part of the solution.
    @SuppressLint("SetTextI18n")
    fun addEntry(text : String, row : Int, col : Int) {
        if (text == "") {
            return
        }

        val inputNumber : Int

        try {
            inputNumber = text.toInt()
        } catch (e : NumberFormatException) {
            boxes[row][col].setText("")
            return
        }

        if (inputNumber !in 1..9) {
            boxes[row][col].setText("")
            return
        }

        val goodPick = inputNumber == solution[row][col]

        if(goodPick) {
            current[row][col] = inputNumber
            score += difficultyPromotions[difficulty]!!
            boxes[row][col].setTextColor(Color.GREEN)
            boxes[row][col].isEnabled = false

            timer.extend(5000)

            if (isSolved()) {
                win()
            }
        } else {
            score -= difficultyDemotions[difficulty]!!
            boxes[row][col].setTextColor(Color.RED)
        }

        scoreText.text = "Score: $score"
        return
    }

    // Checks if the current status of the game is solved (matches solution).
    private fun isSolved() : Boolean {
        for (row in current.indices) {
            for (col in current[row].indices) {
                if (current[row][col] != solution[row][col]) {
                    return false
                }
            }
        }
        return true
    }
}
