package com.example.madesubmission.ui.search

import com.example.madesubmission.core.domain.model.RecentSearch

interface RecentSearchListener {
    fun onClick(query: String)
    fun onLongClick(recentSearch: RecentSearch)
}