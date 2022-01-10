package com.example.sudoku

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sudoku.databinding.FragmentPlayBinding

typealias WinHandler = () -> Unit
typealias LoseHandler = () -> Unit
typealias TickHandler = (Long) -> Unit

class Play : Fragment() {

    private lateinit var game : Sudoku
    private lateinit var boxes : Array<Array<EditText>>
    private lateinit var binding : FragmentPlayBinding

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
        val bundle = bundleOf("score" to game.score.toString())
        findNavController().navigate(R.id.action_play_to_win, bundle)
    }

    private fun lose() {
        val bundle = bundleOf("score" to game.score.toString())
        findNavController().navigate(R.id.action_play_to_lose, bundle)
    }

    private fun initialiseObjects() {
        boxes = arrayOf(
            arrayOf(binding.box00, binding.box01, binding.box02, binding.box03, binding.box04, binding.box05, binding.box06, binding.box07, binding.box08),
            arrayOf(binding.box10, binding.box11, binding.box12, binding.box13, binding.box14, binding.box15, binding.box16, binding.box17, binding.box18),
            arrayOf(binding.box20, binding.box21, binding.box22, binding.box23, binding.box24, binding.box25, binding.box26, binding.box27, binding.box28),
            arrayOf(binding.box30, binding.box31, binding.box32, binding.box33, binding.box34, binding.box35, binding.box36, binding.box37, binding.box38),
            arrayOf(binding.box40, binding.box41, binding.box42, binding.box43, binding.box44, binding.box45, binding.box46, binding.box47, binding.box48),
            arrayOf(binding.box50, binding.box51, binding.box52, binding.box53, binding.box54, binding.box55, binding.box56, binding.box57, binding.box58),
            arrayOf(binding.box60, binding.box61, binding.box62, binding.box63, binding.box64, binding.box65, binding.box66, binding.box67, binding.box68),
            arrayOf(binding.box70, binding.box71, binding.box72, binding.box73, binding.box74, binding.box75, binding.box76, binding.box77, binding.box78),
            arrayOf(binding.box80, binding.box81, binding.box82, binding.box83, binding.box84, binding.box85, binding.box86, binding.box87, binding.box88),
        )

        game = Sudoku(initial1, solution1, Sudoku.Difficulty.EASY, binding.scoreText, binding.timeText, boxes,
            { win() },
            { lose() },
        )
    }

    private fun initialiseVisuals() {
        for (row in game.current.indices) {
            for (col in game.current[row].indices) {
                boxes[row][col].gravity = Gravity.CENTER_HORIZONTAL
                boxes[row][col].setTypeface(null, Typeface.BOLD)

                if (game.current[row][col] != 0) {
                    boxes[row][col].setText(game.current[row][col].toString())
                    boxes[row][col].isEnabled = false

                    if (game.initial[row][col] == game.current[row][col]) {
                        boxes[row][col].setTextColor(Color.BLACK)
                    } else {
                        boxes[row][col].setTextColor(Color.GREEN)
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.buttonWin.setOnClickListener {
            win()
        }

        binding.buttonLose.setOnClickListener {
            lose()
        }

        for (row in boxes.indices) {
            for (col in boxes[row].indices) {
                boxes[row][col].doOnTextChanged { text, _, _, _ ->
                    try {
                        game.addEntry(text.toString(), row, col)
                    } catch (e: java.lang.Exception) {

                    }
                }
            }
        }
    }
}