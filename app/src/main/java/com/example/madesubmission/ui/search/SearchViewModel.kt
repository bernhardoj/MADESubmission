package com.example.madesubmission.ui.search

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
    private var _queryLiveData = MutableLiveData<String>()
    private var _recentSearchLiveData = MutableLiveData<List<RecentSearch>>()
    val recentSearchLiveData: LiveData<List<RecentSearch>>
        get() = _recentSearchLiveData
    val queryLiveData: LiveData<String>
        get() = _queryLiveData

    private var currentQueryValue: String? = null
    private var currentResult: Flow<PagingData<Game>>? = null

    init {
        initQueryChannel()
        getRecentSearch()
    }

    fun searchGames(query: String): Flow<PagingData<Game>> {
        val lastResult = currentResult
        if (currentQueryValue == query && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val newResult: Flow<PagingData<Game>> = gameUseCase.searchGames(query).cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }

    private fun initQueryChannel() {
        viewModelScope.launch {
            queryChannel.asFlow()
                .debounce(300)
                .distinctUntilChanged()
                .filter { it.trim().isNotEmpty() }
                .collect {
                    _queryLiveData.postValue(it)
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

    fun deleteRecentSearch(recentSearch: RecentSearch) {
        viewModelScope.launch {
            gameUseCase.deleteRecentSearch(recentSearch)
        }
    }
}