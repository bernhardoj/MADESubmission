package com.example.madesubmission.core.domain.repository

import androidx.paging.PagingData
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.data.source.local.entity.GameUpdateEntity
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.model.GameDetail
import com.example.madesubmission.core.domain.model.RecentSearch
import kotlinx.coroutines.flow.Flow

interface IGameRepository {
    fun getAllGames(platform: String): Flow<Resource<List<Game>>>
    fun searchGames(query: String): Flow<Resource<List<Game>>>
    fun getGameDetail(id: Int): Flow<Resource<GameDetail>>
    fun getFavorites(): Flow<PagingData<Game>>
    suspend fun insertGame(game: Game)
    suspend fun deleteGame(game: Game)
    suspend fun updateFavoriteGame(gameDetail: GameUpdateEntity)
    fun getRecentSearch(): Flow<List<RecentSearch>>
    suspend fun saveRecentSearch(recentSearch: RecentSearch)
    fun clearRecentSearch()
}