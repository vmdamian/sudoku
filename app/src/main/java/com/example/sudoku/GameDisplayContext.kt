package com.example.sudoku

import android.graphics.Color

const val MAX_ROWS = 9

data class GameDisplayContext(
    val currentBoard: Array<IntArray>,
    val solutionBoard: Array<IntArray>,
    var selectedSquarePosition: Int = 0
) {
    fun getCountOfSquares(): Int {
        return currentBoard.size * currentBoard.size
    }

    fun getTextColor(position: Int): Int {
        val row = getRowByPosition(position)
        val col = getColByPosition(position)
        val isCorrect = currentBoard[row][col] == solutionBoard[row][col]
        return if (isCorrect) Color.WHITE else Color.RED
    }

    fun getBackgroundColor(position: Int): Int {
        val isSelected = selectedSquarePosition == position
        return if (isSelected) Color.LTGRAY else R.color.purple_200
    }

    fun isSelectedSquareAlreadySolved(): Boolean {
        val row = getRowByPosition(selectedSquarePosition)
        val col = getColByPosition(selectedSquarePosition)
        return currentBoard[row][col] == solutionBoard[row][col]
    }

    fun getSelectedRow(): Int {
        return getRowByPosition(selectedSquarePosition)
    }

    fun getSelectedCol(): Int {
        return getColByPosition(selectedSquarePosition)
    }

    fun getCurrentBoardValue(position: Int): Int {
        val row = getRowByPosition(position)
        val col = getColByPosition(position)
        return currentBoard[row][col]
    }

    companion object {
        fun getRowByPosition(position: Int): Int {
            return position / MAX_ROWS
        }

        fun getColByPosition(position: Int): Int {
            return position % MAX_ROWS
        }
    }
}