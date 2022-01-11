package com.example.sudoku

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ScoreboardEntryModel(val rank: String = "", val playerName: String = "", val score: String = "") {

    object ScorePointsComparator: Comparator<ScoreboardEntryModel> {
        override fun compare(p0: ScoreboardEntryModel?, p1: ScoreboardEntryModel?): Int {
            val p0Score: Int = p0?.score?.toInt()?:0
            val p1Score: Int = p1?.score?.toInt()?:0
            return p1Score - p0Score
        }
    }
}