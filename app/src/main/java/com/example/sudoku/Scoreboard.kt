package com.example.sudoku

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.example.sudoku.service.StorageService

class Scoreboard : Fragment() {
    private lateinit var scoreboardViewAdapter: ScoreboardViewAdapter
    private val storageService: StorageService = StorageService()
    private val highScoresDataset: MutableList<ScoreboardEntryModel> = ArrayList(getHeaderHighScoreItems())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        storageService.writeScoreEntry(scoreEntry)
    }

    private fun setupRecyclerView(binding: FragmentScoreboardBinding) {
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recycler_view_scoreboard)
        val layoutManager =
            binding.root.findViewById<ConstraintLayout>(R.id.layout_manager_recycler_scoreboard)
        recyclerView.layoutManager = LinearLayoutManager(layoutManager.context)
        scoreboardViewAdapter = ScoreboardViewAdapter(highScoresDataset)
        recyclerView.adapter = scoreboardViewAdapter
        storageService.readScoreEntries(successCallback = this::readTopScoresCallback)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun readTopScoresCallback(topScores: List<ScoreboardEntryModel>) {
        highScoresDataset.clear()
        highScoresDataset.addAll(getHeaderHighScoreItems())
        highScoresDataset.addAll(topScores)
        scoreboardViewAdapter.notifyDataSetChanged()
    }

    private fun getHeaderHighScoreItems(): List<ScoreboardEntryModel> {
        return listOf(
            ScoreboardEntryModel("#", "Player name", "Score")
        )
    }
}