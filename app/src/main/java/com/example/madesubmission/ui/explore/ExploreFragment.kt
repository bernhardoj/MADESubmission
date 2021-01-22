package com.example.madesubmission.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.madesubmission.databinding.FragmentExploreBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ExploreFragment : Fragment() {
    private var binding: FragmentExploreBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            binding?.let { bind ->
                val viewPager = bind.pager
                viewPager.adapter = ViewPagerAdapter(it)

                val tabLayout = bind.tabLayout
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = (viewPager.adapter as ViewPagerAdapter).fragmentsName[position]
                }.attach()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}