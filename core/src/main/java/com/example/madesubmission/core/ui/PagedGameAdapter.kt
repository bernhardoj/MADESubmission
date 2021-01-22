package com.example.madesubmission.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.madesubmission.core.R
import com.example.madesubmission.core.domain.model.Game

class PagedGameAdapter(private val listener: (Game) -> Unit) : PagingDataAdapter<Game, GameViewHolder>(GAME_COMPARATOR) {
    companion object {
        private val GAME_COMPARATOR = object : DiffUtil.ItemCallback<Game>() {
            override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = getItem(position)
        if (game != null) holder.bind(game)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder =
        GameViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false),
            listener
        )
}