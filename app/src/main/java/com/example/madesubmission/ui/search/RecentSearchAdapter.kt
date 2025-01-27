package com.example.madesubmission.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.madesubmission.R
import com.example.madesubmission.core.domain.model.RecentSearch
import com.example.madesubmission.databinding.ItemRecentSearchBinding

class RecentSearchAdapter(private val listener: RecentSearchListener) :
    RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>() {
    private var recentSearch = ArrayList<RecentSearch>()

    fun setList(recentSearch: List<RecentSearch>) {
        this.recentSearch.clear()
        this.recentSearch.addAll(recentSearch)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_recent_search, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recentSearch[position])
    }

    override fun getItemCount(): Int = recentSearch.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRecentSearchBinding.bind(itemView)

        fun bind(recentSearch: RecentSearch) {
            binding.recentQuery.text = recentSearch.query
            itemView.setOnClickListener {
                listener.onClick(recentSearch.query)
            }
            itemView.setOnLongClickListener {
                val context = itemView.context
                AlertDialog.Builder(context)
                    .setTitle(recentSearch.query)
                    .setMessage(
                        String.format(
                            context.getString(R.string.delete_recent_search_confirmation),
                            recentSearch.query
                        )
                    )
                    .setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                        listener.onLongClick(recentSearch)
                    }
                    .setNegativeButton(context.getString(R.string.no), null)
                    .show()
                true
            }
        }
    }
}