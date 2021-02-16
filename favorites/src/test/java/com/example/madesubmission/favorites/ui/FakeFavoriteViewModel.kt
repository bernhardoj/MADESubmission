package com.example.madesubmission.favorites.ui

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.usecase.GameUseCase
import kotlinx.coroutines.flow.Flow

class FakeFavoriteViewModel(private val gameUseCase: GameUseCase) : ViewModel() {
    private var currentResult: Flow<PagingData<Game>>? = null

    fun getFavorites(): Flow<PagingData<Game>> {
        val lastResult = currentResult
        if (lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<Game>> = gameUseCase.getFavorites()
        currentResult = newResult
        return newResult
    }
}