package com.example.madesubmission.core.domain.usecase

import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.data.source.local.entity.GameUpdateEntity
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.model.GameDetail
import com.example.madesubmission.core.domain.repository.IGameRepository
import kotlinx.coroutines.flow.Flow

class GameInteractor(private val gameRepository: IGameRepository) : GameUseCase {
    override fun getAllGames(query: String, platform: String): Flow<Resource<List<Game>>> {
        return if (query.isEmpty())
            gameRepository.getAllGames(platform)
        else gameRepository.searchGames(query)
    }
    override fun getGameDetail(id: Int): Flow<Resource<GameDetail>> {
        return gameRepository.getGameDetail(id)
    }
    override fun getFavorites() = gameRepository.getFavorites()
    override suspend fun insertGame(game: Game) = gameRepository.insertGame(game)

    override suspend fun deleteGame(game: Game) = gameRepository.deleteGame(game)

    override suspend fun updateFavoriteGame(gameDetail: GameUpdateEntity) =
        gameRepository.updateFavoriteGame(gameDetail)

}