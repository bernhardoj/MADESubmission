package com.example.madesubmission.favorites.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.usecase.GameUseCase
import kotlinx.coroutines.flow.Flow

class FavoriteViewModel(private val gameUseCase: GameUseCase) : ViewModel() {
    private var currentResult: Flow<PagingData<Game>>? = null

    fun getFavorites(): Flow<PagingData<Game>> {
        val lastResult = currentResult
        if (lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<Game>> = gameUseCase.getFavorites().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }
}