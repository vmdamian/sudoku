package com.example.sudoku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.sudoku.databinding.FragmentMainMenuBinding

class MainMenu : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentMainMenuBinding>(inflater, R.layout.fragment_main_menu, container, false)

        binding.buttonPlay.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenu_to_play)
        }

        binding.buttonScoreboard.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenu_to_scoreboard)
        }

        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenu_to_settings)
        }

        return binding.root
    }
}