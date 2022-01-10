package com.example.sudoku

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.sudoku.databinding.FragmentWinBinding

private const val ARG_SCORE = "score"

class Win : Fragment() {
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
        val binding = DataBindingUtil.inflate<FragmentWinBinding>(inflater, R.layout.fragment_win, container, false)

        binding.buttonBackWin.setOnClickListener {
            findNavController().navigate(R.id.action_win_to_mainMenu)
        }

        binding.winScoreText.text = "Score $score"

        return binding.root
    }
}