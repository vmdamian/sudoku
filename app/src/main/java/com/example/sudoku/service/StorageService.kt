package com.example.sudoku.service

import android.util.Log
import com.example.sudoku.ScoreboardEntryModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val EVENT_TAG: String = "Storage system event"
private const val DB_URL: String =
    "https://mc---sudoku-project-default-rtdb.europe-west1.firebasedatabase.app"

class StorageService {
    private var database: DatabaseReference = Firebase.database(DB_URL).reference

    fun writeScoreEntry(scoreEntry: ScoreboardEntryModel): Task<Void> {
        val scoresReference = database.child("scoreboard").child("scorepoints")
        val entryKey = scoresReference.push().key
        val jsonDoc = mapOf(entryKey to scoreEntry)

        return scoresReference.updateChildren(jsonDoc)
            .addOnSuccessListener {
                Log.i(EVENT_TAG, "Added new score!")
            }.addOnFailureListener {
                Log.e(EVENT_TAG, "Error while writing data", it)
            }
    }

    fun readScoreEntries(successCallback: (List<ScoreboardEntryModel>) -> Unit) {
        val topScoresQuery = database.child("scoreboard").child("scorepoints")
            .orderByChild("score")
            .limitToFirst(100)

        topScoresQuery.get()
            .addOnSuccessListener {
                Log.i(EVENT_TAG, "Got value ${it.value}")
                val sortedEntries = extractScoreEntries(it)
                successCallback(sortedEntries)
            }.addOnFailureListener {
                Log.e(EVENT_TAG, "Error while fetching data", it)
            }
    }

    private fun extractScoreEntries(dataSnapshot: DataSnapshot): List<ScoreboardEntryModel> {
        val genericTypeIndicator =
            object : GenericTypeIndicator<Map<String, ScoreboardEntryModel>>() {}
        val entryByKey = dataSnapshot.getValue(genericTypeIndicator)!!
        return entryByKey.values.sortedWith(ScoreboardEntryModel.ScorePointsComparator)
    }

}