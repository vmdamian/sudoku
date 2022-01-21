package com.example.sudoku
import android.util.Log
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
        binding.shareScoreButton.setOnClickListener {
            composeMessage("Just finished another Sudoku game. My score was: $score")
        }

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

    private fun composeMessage(message: String) {
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type="text/plain"
            putExtra(Intent.EXTRA_TITLE, "My Sudoku Score")

            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }, null)
        startActivity(share)
    }
}