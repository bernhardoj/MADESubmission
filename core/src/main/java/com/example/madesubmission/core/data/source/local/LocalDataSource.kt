package com.example.madesubmission.core.data.source.local

import androidx.paging.PagingSource
import com.example.madesubmission.core.data.source.local.entity.GameDetailEntity
import com.example.madesubmission.core.data.source.local.entity.GameEntity
import com.example.madesubmission.core.data.source.local.entity.GameUpdateEntity
import com.example.madesubmission.core.data.source.local.entity.RecentSearchEntity
import com.example.madesubmission.core.data.source.local.room.GameDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val gameDao: GameDao) {
    fun getAllGames(platform: String): Flow<List<GameEntity>> = gameDao.getAllGames(platform)
    fun getGameDetail(id: Int): Flow<GameDetailEntity> = gameDao.getGameDetail(id)
    fun getFavorites(): PagingSource<Int, GameEntity> = gameDao.getFavorites()
    suspend fun updateFavoriteGame(gameDetail: GameUpdateEntity) = gameDao.updateFavoriteGame(gameDetail)
    suspend fun insertGame(gameEntity: GameEntity) = gameDao.insertGame(gameEntity)
    suspend fun insertGames(gameEntities: List<GameEntity>) = gameDao.insertOrUpdate(gameEntities)
    suspend fun insertGameDetail(gameDetailEntity: GameDetailEntity) = gameDao.insertGameDetail(gameDetailEntity)
    suspend fun deleteGame(gameEntity: GameEntity) = gameDao.deleteGame(gameEntity)
    fun getRecentSearch() = gameDao.getRecentSearch()
    suspend fun saveRecentSearch(recentSearchEntity: RecentSearchEntity) = gameDao.saveRecentSearch(recentSearchEntity)
    suspend fun deleteRecentSearch(recentSearchEntity: RecentSearchEntity) = gameDao.deleteRecentSearch(recentSearchEntity)
    fun clearRecentSearch() = gameDao.clearRecentSearch()
}