package com.example.madesubmission.ui.search

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.usecase.GameUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@ExperimentalCoroutinesApi
class FakeSearchViewModel(private val gameUseCase: GameUseCase) : ViewModel() {
    private var currentQueryValue: String? = null
    private var currentResult: Flow<PagingData<Game>>? = null

    fun searchGames(query: String): Flow<PagingData<Game>> {
        val lastResult = currentResult
        if (currentQueryValue == query && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val newResult: Flow<PagingData<Game>> = gameUseCase.searchGames(query)
        currentResult = newResult
        return newResult
    }
}