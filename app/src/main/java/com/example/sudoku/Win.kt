package com.example.sudoku

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.sudoku.databinding.FragmentWinBinding
import com.example.sudoku.service.StorageService

private const val ARG_SCORE = "score"

class Win : Fragment() {
    private var score: String? = null
    private val storageService: StorageService = StorageService()

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
        setupAddScoreButton(binding.root)

        binding.winScoreText.text = "Score $score"

        return binding.root
    }

    private fun setupAddScoreButton(view: View) {
        val addScoreButton = view.findViewById<Button>(R.id.add_highscore_button)
        addScoreButton.setOnClickListener{
            addNewHighScore(view)
            findNavController().navigate(R.id.action_win_to_scoreboard)
        }
    }

    private fun addNewHighScore(view: View) {
        val nicknameEditText = view.findViewById<EditText>(R.id.highscore_nickname)
        val score = this.score?:"0"
        val scoreEntry = ScoreboardEntryModel("", nicknameEditText.text.toString(), score)
        storageService.writeScoreEntry(scoreEntry)
    }
}