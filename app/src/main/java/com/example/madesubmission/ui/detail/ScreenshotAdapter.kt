package com.example.madesubmission.ui.detail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madesubmission.R
import com.example.madesubmission.databinding.ItemScreenshotBinding

class ScreenshotAdapter : RecyclerView.Adapter<ScreenshotAdapter.ViewHolder>() {
    private val mScreenshots = ArrayList<String>()

    fun setScreenshots(screenshots: List<String>) {
        mScreenshots.clear()
        mScreenshots.addAll(screenshots)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_screenshot, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mScreenshots[position])
    }

    override fun getItemCount(): Int = mScreenshots.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mBinding = ItemScreenshotBinding.bind(itemView)

        fun bind(url: String) {
            Glide.with(itemView.context)
                .load(url)
                .placeholder(ColorDrawable(Color.GRAY))
                .into(mBinding.gameScreenshot)
        }
    }
}