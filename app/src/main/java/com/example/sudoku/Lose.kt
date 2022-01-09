package com.example.sudoku

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.sudoku.databinding.FragmentLoseBinding

private const val ARG_SCORE = "score"

class Lose : Fragment() {
    private var score: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            score = it.getString(ARG_SCORE)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentLoseBinding>(inflater, R.layout.fragment_lose, container, false)

        binding.buttonBackLose.setOnClickListener {
            findNavController().navigate(R.id.action_lose_to_mainMenu)
        }

        binding.loseScoreText.text = "Score $score"

        return binding.root
    }
}