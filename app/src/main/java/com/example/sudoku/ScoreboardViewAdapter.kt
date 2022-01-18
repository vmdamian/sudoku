package com.example.sudoku

import android.annotation.SuppressLint
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.NORMAL
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ScoreboardViewAdapter(private val dataSet: List<ScoreboardEntryModel>) :
    RecyclerView.Adapter<ScoreboardViewAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tableRow: TableRow = view.findViewById(R.id.scoreboard_table_row)
        val rankView: TextView = view.findViewById(R.id.scoreboard_table_rank)
        val playerNameView: TextView = view.findViewById(R.id.scoreboard_table_player_name)
        val scoreView: TextView = view.findViewById(R.id.scoreboard_table_score)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.scoreboard_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        setupRowStyle(viewHolder, position == 0)
        viewHolder.rankView.text = dataSet[position].rank.ifEmpty { position.toString() }
        viewHolder.playerNameView.text = dataSet[position].playerName
        viewHolder.scoreView.text = dataSet[position].score
    }

    private fun setupRowStyle(viewHolder: ViewHolder, isHeader: Boolean) {
        applyTextViewStyle(viewHolder.rankView, isHeader)
        applyTextViewStyle(viewHolder.playerNameView, isHeader)
        applyTextViewStyle(viewHolder.scoreView, isHeader)
    }

    @SuppressLint("ResourceAsColor")
    private fun applyTextViewStyle(textView: TextView, isHeader: Boolean) {
        val color = if (isHeader) R.color.purple_500 else R.color.black
        textView.setTextColor(color)
        val style = if (isHeader) BOLD else NORMAL
        textView.setTypeface(null, style)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
