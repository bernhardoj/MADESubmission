package com.example.madesubmission.core.ui.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.madesubmission.core.R
import com.example.madesubmission.core.databinding.LoadStateFooterBinding

class GameLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<GameLoadStateAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.load_state_footer, parent, false))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = LoadStateFooterBinding.bind(itemView)

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error

            with (binding.errorLoad) {
                isVisible = loadState is LoadState.Error
                if (loadState is LoadState.Error) {
                    text = loadState.error.message.toString()
                }
            }
        }
    }
}