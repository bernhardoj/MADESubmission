package com.example.madesubmission.core.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.madesubmission.core.GlideApp
import com.example.madesubmission.core.databinding.ItemGameBinding
import com.example.madesubmission.core.domain.model.Game

class GameViewHolder(itemView: View, private val listener: (Game) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemGameBinding.bind(itemView)

    fun bind(game: Game) {
        with(binding) {
            GlideApp.with(itemView.context)
                .load(game.imageUrl)
                .placeholder(ColorDrawable(Color.DKGRAY))
                .error(ColorDrawable(Color.BLACK))
                .into(gameImage)
            gameTitle.text = game.name
            rating.text = game.rating.toString()
            genres.text = game.genres
        }
        itemView.setOnClickListener {
            listener(game)
        }
    }
}