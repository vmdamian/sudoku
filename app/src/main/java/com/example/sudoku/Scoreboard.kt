package com.example.sudoku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku.databinding.FragmentScoreboardBinding

class Scoreboard : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentScoreboardBinding>(inflater, R.layout.fragment_scoreboard, container, false)

        binding.buttonBackScoreboard.setOnClickListener {
            findNavController().navigate(R.id.action_scoreboard_to_mainMenu)
        }
        setupRecyclerView(binding)

        return binding.root
    }

    private fun setupRecyclerView(binding: FragmentScoreboardBinding) {
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recycler_view_scoreboard)
        val layoutManager = binding.root.findViewById<ConstraintLayout>(R.id.layout_manager_recycler_scoreboard)
        recyclerView.layoutManager = LinearLayoutManager(layoutManager.context)
        val adapter = ScoreboardViewAdapter(getHighScoresDataset())
        recyclerView.adapter = adapter
    }

    // TODO (future PR): replace mock data with an actual call to the DB collection.
    private fun getHighScoresDataset(): List<ScoreboardEntryModel> {
        return listOf(
            ScoreboardEntryModel("#", "Player name", "Score"),
            ScoreboardEntryModel("1", "Awesome Awesome-o", "100"),
            ScoreboardEntryModel("2", "Bro Broski", "85"),
            ScoreboardEntryModel("3", "CC", "10"),
            ScoreboardEntryModel("4", "Davie", "5"),
            ScoreboardEntryModel("13", "Nobody Near Nowhere Nonending Name", "0"),

        )
    }
}