package com.example.madesubmission.ui.search

import androidx.lifecycle.*
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.model.RecentSearch
import com.example.madesubmission.core.domain.usecase.GameUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel(private val gameUseCase: GameUseCase) : ViewModel() {
    val queryChannel = ConflatedBroadcastChannel<String>()
    private var _gameLiveData = MutableLiveData<Resource<List<Game>>>()
    private var _recentSearchLiveData = MutableLiveData<List<RecentSearch>>()
    val recentSearchLiveData: LiveData<List<RecentSearch>>
        get() = _recentSearchLiveData
    val gameLiveData: LiveData<Resource<List<Game>>>
        get() = _gameLiveData

    init {
        searchGames()
        getRecentSearch()
    }

    fun searchGames() {
        viewModelScope.launch {
            queryChannel.asFlow()
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.trim().isNotEmpty() }
                .mapLatest { gameUseCase.getAllGames(query = it) }
                .collect {
                    it.collect { result ->
                        _gameLiveData.postValue(result)
                    }
                }
        }
    }

    private fun getRecentSearch() {
        viewModelScope.launch {
            gameUseCase.getRecentSearch().collect {
               _recentSearchLiveData.postValue(it)
            }
        }
    }

    fun saveRecentSearch(query: String) {
        viewModelScope.launch {
            gameUseCase.saveRecentSearch(RecentSearch(query))
        }
    }
}