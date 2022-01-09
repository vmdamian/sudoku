package com.example.sudoku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
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

        return binding.root
    }
}