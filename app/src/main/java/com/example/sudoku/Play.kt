package com.example.sudoku

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sudoku.databinding.FragmentPlayBinding

typealias LoseHandler = () -> Unit
typealias TickHandler = (Long) -> Unit

class Play : Fragment() {

    private lateinit var game: Sudoku
    private lateinit var binding: FragmentPlayBinding
    private lateinit var sudokuBoardGridAdapter: SudokuBoardGridAdapter
    private lateinit var gameDisplayContext: GameDisplayContext

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_play, container, false)
        initialiseObjects()
        initialiseVisuals()
        setupListeners()

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        game.pause()
    }

    override fun onResume() {
        super.onResume()
        game.resume()
    }

    private fun win() {
        val bundle = bundleOf("score" to game.score().value.toString())
        findNavController().navigate(R.id.action_play_to_win, bundle)
    }

    private fun lose() {
        val bundle = bundleOf("score" to game.score().value.toString())
        findNavController().navigate(R.id.action_play_to_lose, bundle)
    }

    private fun initialiseObjects() {
        game = Sudoku()
    }

    @SuppressLint("SetTextI18n")
    private fun initialiseVisuals() {
        val numPadGridView = binding.root.findViewById<GridView>(R.id.numpad_grid)
        numPadGridView.adapter =
            NumPadGridAdapter(this.requireContext(), onClickAction = this::addDigitToGame)

        val sudokuGridView = binding.root.findViewById<GridView>(R.id.sudoku_board_grid)
        gameDisplayContext = GameDisplayContext(game.current, game.solution)
        sudokuBoardGridAdapter = SudokuBoardGridAdapter(this.requireContext(), gameDisplayContext)
        sudokuGridView.adapter = sudokuBoardGridAdapter

        game.score().observe(viewLifecycleOwner, {
            binding.scoreText.text = "Score: $it"
        })

        game.remainingMillis().observe(viewLifecycleOwner, {
            binding.timeText.text = "Time: ${it/1000}"
        })

        game.finishStatus().observe(viewLifecycleOwner, {
            if (it == Sudoku.GAME_WON) {
                win()
            } else if (it == Sudoku.GAME_LOST){
                lose()
            }
        })
    }

    private fun addDigitToGame(digit: Int) {
        if (gameDisplayContext.isSelectedSquareAlreadySolved()) {
            return
        }

        game.addEntry(
            inputNumber = digit,
            row = gameDisplayContext.getSelectedRow(),
            col = gameDisplayContext.getSelectedCol()
        )
        sudokuBoardGridAdapter.notifyDataSetChanged()
    }

    private fun setupListeners() {
        binding.buttonWin.setOnClickListener {
            win()
        }

        binding.buttonLose.setOnClickListener {
            lose()
        }
    }
}