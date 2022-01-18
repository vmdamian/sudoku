package com.example.sudoku

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button

class SudokuBoardGridAdapter(context: Context, private val gameDisplayContext: GameDisplayContext) :
    BaseAdapter() {
    private val layoutInflater = LayoutInflater.from(context)

    class ViewHolder(view: View) {
        val button: Button = view.findViewById(R.id.button_sudoku_board_item)
    }


    override fun getCount(): Int {
        return gameDisplayContext.getCountOfSquares()
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val view = convertView ?: layoutInflater
            .inflate(R.layout.sudoku_board_grid_item, viewGroup, false)
        val viewHolder = view.tag as? ViewHolder ?: ViewHolder(view)
        view.tag = viewHolder

        val digit = gameDisplayContext.getCurrentBoardValue(position)
        viewHolder.button.text = if (digit != 0) digit.toString() else ""
        viewHolder.button.setOnClickListener {
            gameDisplayContext.selectedSquarePosition = position
            this.notifyDataSetChanged()
        }
        viewHolder.button.setBackgroundColor(gameDisplayContext.getBackgroundColor(position))
        viewHolder.button.setTextColor(gameDisplayContext.getTextColor(position))


        return view
    }
}