package com.example.madesubmission.core.domain.usecase

import androidx.paging.PagingData
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.data.source.local.entity.GameUpdateEntity
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.model.GameDetail
import com.example.madesubmission.core.domain.model.RecentSearch
import com.example.madesubmission.core.domain.repository.IGameRepository
import kotlinx.coroutines.flow.Flow

class GameInteractor(private val gameRepository: IGameRepository) : GameUseCase {
    override fun getAllGames(platform: String): Flow<Resource<List<Game>>> =
        gameRepository.getAllGames(platform)

    override fun searchGames(query: String): Flow<PagingData<Game>> =
        gameRepository.searchGames(query)

    override fun getGameDetail(id: Int): Flow<Resource<GameDetail>> =
        gameRepository.getGameDetail(id)

    override fun getFavorites() = gameRepository.getFavorites()
    override suspend fun insertGame(game: Game) = gameRepository.insertGame(game)

    override suspend fun deleteGame(game: Game) = gameRepository.deleteGame(game)

    override suspend fun updateFavoriteGame(gameDetail: GameUpdateEntity) =
        gameRepository.updateFavoriteGame(gameDetail)

    override fun getRecentSearch(): Flow<List<RecentSearch>> =
        gameRepository.getRecentSearch()

    override suspend fun saveRecentSearch(recentSearch: RecentSearch) =
        gameRepository.saveRecentSearch(recentSearch)

    override fun clearRecentSearch() = gameRepository.clearRecentSearch()
}