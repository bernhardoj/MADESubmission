package com.example.madesubmission.ui.explore

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val fragments = arrayOf(
        ExploreListFragment.newInstance("1"),
        ExploreListFragment.newInstance("2"),
        ExploreListFragment.newInstance("4,8")
    )

    val fragmentsName = arrayOf("PC", "PlayStation", "Mobile")

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}