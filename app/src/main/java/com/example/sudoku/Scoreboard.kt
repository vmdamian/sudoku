package com.example.sudoku

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku.databinding.FragmentScoreboardBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Scoreboard : Fragment() {
    private val DB_URL: String = "https://mc---sudoku-project-default-rtdb.europe-west1.firebasedatabase.app"
    private val EVENT_TAG: String = "Scoreboard event"

    private lateinit var database: DatabaseReference
    private lateinit var scoreboardViewAdapter: ScoreboardViewAdapter
    private val highScoresDataset: MutableList<ScoreboardEntryModel> = ArrayList(getHeaderHighScoreItems())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        database = Firebase.database(DB_URL).reference

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentScoreboardBinding>(
            inflater,
            R.layout.fragment_scoreboard,
            container,
            false
        )

        binding.buttonBackScoreboard.setOnClickListener {
            findNavController().navigate(R.id.action_scoreboard_to_mainMenu)
        }
        setupRecyclerView(binding)
        setupAddScoreButton(binding.root)

        return binding.root
    }

    private fun setupAddScoreButton(view: View) {
        val addScoreButton = view.findViewById<Button>(R.id.add_highscore_button)
        addScoreButton.setOnClickListener{
            addNewHighScore(view)
        }
    }

    // Only for demo purposes.
    private fun addNewHighScore(view: View) {
        val nicknameEditText = view.findViewById<EditText>(R.id.highscore_nickname)
        val score = highScoresDataset.last().score.toInt() + 1

        val scoreEntry = ScoreboardEntryModel("", nicknameEditText.text.toString(), score.toString())
        val scoresReference = database.child("scoreboard").child("scorepoints")
        val entryKey = scoresReference.push().key
        scoresReference.updateChildren(mapOf(
            entryKey to scoreEntry
        )).addOnSuccessListener {
            Log.i(EVENT_TAG, "Added new score!")
            // The real "Submit new score" button won't require a view-model update.
            updateHighScoresDataset()
        }
    }

    private fun setupRecyclerView(binding: FragmentScoreboardBinding) {
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recycler_view_scoreboard)
        val layoutManager =
            binding.root.findViewById<ConstraintLayout>(R.id.layout_manager_recycler_scoreboard)
        recyclerView.layoutManager = LinearLayoutManager(layoutManager.context)
        scoreboardViewAdapter = ScoreboardViewAdapter(highScoresDataset)
        recyclerView.adapter = scoreboardViewAdapter
        updateHighScoresDataset()
    }

    private fun updateHighScoresDataset() {
        highScoresDataset.clear()
        highScoresDataset.addAll(getHeaderHighScoreItems())
        addTopScores()
    }

    private fun getHeaderHighScoreItems(): List<ScoreboardEntryModel> {
        return listOf(
            ScoreboardEntryModel("#", "Player name", "Score")
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addTopScores() {
        val topScoresQuery = database.child("scoreboard").child("scorepoints")
            .orderByChild("score")
            .limitToFirst(100)

        topScoresQuery.get().addOnSuccessListener {
            Log.i(EVENT_TAG, "Got value ${it.value}")
            val genericTypeIndicator = object : GenericTypeIndicator<Map<String, ScoreboardEntryModel>>(){}
            val entryByKey = it.getValue(genericTypeIndicator)!!
            val sortedEntries = entryByKey.values.sortedWith(ScoreboardEntryModel.ScorePointsComparator)
            highScoresDataset.addAll(sortedEntries)
            scoreboardViewAdapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Log.e(EVENT_TAG, "Error while fetching data", it)
        }
    }
}