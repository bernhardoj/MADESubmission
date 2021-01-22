package com.example.madesubmission.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.usecase.GameUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(private val gameUseCase: GameUseCase) : ViewModel() {
    private var _gameLiveData = MutableLiveData<Resource<List<Game>>>()
    val gameLiveData: LiveData<Resource<List<Game>>>
        get() = _gameLiveData

    init {
        loadAllGames()
    }

    fun loadAllGames() {
//        viewModelScope.launch {
//            gameUseCase.getAllGames(query).collect {
//                _gameLiveData.value = it
//            }
//        }
    }
}