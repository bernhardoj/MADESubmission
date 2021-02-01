package com.example.madesubmission.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madesubmission.core.R
import com.example.madesubmission.core.domain.model.Game

class GameAdapter(private val listener: (Game) -> Unit) : RecyclerView.Adapter<GameViewHolder>() {
    private val gameList = ArrayList<Game>()

    fun setGameList(gameList: List<Game>) {
        this.gameList.clear()
        this.gameList.addAll(gameList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder =
        GameViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false),
            listener
        )

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item = gameList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = gameList.size
}