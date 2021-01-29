package com.example.madesubmission.ui.explore

import androidx.lifecycle.*
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.usecase.GameUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ExploreListViewModel(private val platform: String, private val gameUseCase: GameUseCase) :
    ViewModel() {
    private var _gameLiveData = MutableLiveData<Resource<List<Game>>>()
    val gameLiveData: LiveData<Resource<List<Game>>>
        get() = _gameLiveData

    init {
        loadAllGames()
    }

    fun loadAllGames() {
        viewModelScope.launch {
            gameUseCase.getAllGames(platform).collect {
                _gameLiveData.value = it
            }
        }
    }
}