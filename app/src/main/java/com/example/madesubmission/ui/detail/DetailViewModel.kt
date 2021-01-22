package com.example.madesubmission.ui.detail

import androidx.lifecycle.*
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.data.source.local.entity.GameUpdateEntity
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.model.GameDetail
import com.example.madesubmission.core.domain.usecase.GameUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailViewModel(private val id: Int, private val gameUseCase: GameUseCase) : ViewModel() {
    private val _gameDetailLiveData = MutableLiveData<Resource<GameDetail>>()
    private val _favoriteLiveData = MutableLiveData<Boolean>()
    val gameDetailLiveData: LiveData<Resource<GameDetail>>
        get() = _gameDetailLiveData
    val favoriteLiveData: LiveData<Boolean>
        get() = _favoriteLiveData

    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            gameUseCase.getGameDetail(id).collect {
                _gameDetailLiveData.value = it
            }
        }
    }

    fun setFavorite(isFavorite: Boolean) {
        _favoriteLiveData.value = isFavorite
    }

    fun updateFavorite(isFavorite: Boolean, game: Game) {
        viewModelScope.launch {
            gameUseCase.updateFavoriteGame(GameUpdateEntity(id, isFavorite))
            when (isFavorite) {
                true -> gameUseCase.insertGame(game)
                false -> if (game.isFavorite) gameUseCase.deleteGame(game)
            }
        }
    }
}