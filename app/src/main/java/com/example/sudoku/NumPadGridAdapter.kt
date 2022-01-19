package com.example.sudoku

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button

class NumPadGridAdapter(context: Context, private val onClickAction: (digit: Int) -> Unit) :
    BaseAdapter() {
    private val digits = (1..9).toList()
    private val layoutInflater = LayoutInflater.from(context)

    class ViewHolder(view: View) {
        val button: Button = view.findViewById(R.id.button_numpad)
    }

    override fun getCount(): Int {
        return digits.size
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val view = convertView ?: layoutInflater
            .inflate(R.layout.numpad_grid_item, viewGroup, false)
        val viewHolder = view.tag as? ViewHolder ?: ViewHolder(view)
        view.tag = viewHolder

        viewHolder.button.text = digits[position].toString()
        viewHolder.button.setOnClickListener {
            onClickAction(digits[position])
        }
        return view
    }
}