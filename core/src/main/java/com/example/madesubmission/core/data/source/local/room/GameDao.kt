package com.example.madesubmission.core.data.source.local.room

import androidx.paging.PagingSource
import androidx.room.*
import com.example.madesubmission.core.data.source.local.entity.GameDetailEntity
import com.example.madesubmission.core.data.source.local.entity.GameEntity
import com.example.madesubmission.core.data.source.local.entity.GameUpdateEntity
import com.example.madesubmission.core.data.source.local.entity.RecentSearchEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

@Dao
interface GameDao {
    @Query("SELECT * from games WHERE isFavorite=0 AND platformId LIKE '%' || :platform || '%' ORDER BY rating DESC")
    fun getAllGames(platform: String): Flow<List<GameEntity>>

    @Query("SELECT * from game_detail WHERE id=:id")
    fun getGameDetail(id: Int): Flow<GameDetailEntity>

    @Query(
        "SELECT games.id, games.imageUrl, games.name, games.genres, games.rating, games.updated, games.isFavorite, games.platformId "
                + "from games INNER JOIN game_detail ON games.id=game_detail.id WHERE game_detail.isFavorite=1"
    )
    fun getFavorites(): PagingSource<Int, GameEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGame(gameEntity: GameEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(gameEntities: List<GameEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameDetail(gameDetailEntity: GameDetailEntity)

    @Update(entity = GameDetailEntity::class)
    suspend fun updateFavoriteGame(gameDetail: GameUpdateEntity)

    @Delete
    suspend fun deleteGame(gameEntity: GameEntity)

    @Query("SELECT * FROM games WHERE id=:id")
    fun getGame(id: Int): Flow<GameEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveRecentSearch(recentSearch: RecentSearchEntity)

    @Query("SELECT * FROM recent_search")
    fun getRecentSearch(): Flow<List<RecentSearchEntity>>

    @Delete
    suspend fun deleteRecentSearch(recentSearch: RecentSearchEntity)

    @Query("DELETE FROM recent_search")
    suspend fun clearRecentSearch()

    suspend fun insertOrUpdate(gameEntities: List<GameEntity>) {
        for (gameEntity in gameEntities) {
            // Get existed game record if any
            val game = getGame(gameEntity.id).firstOrNull()
            game?.let {
                var currentPlatformId = game.platformId

                // If the existing game record does not contain the fetched
                // platformId, append it.
                if (gameEntity.platformId !in currentPlatformId) {
                    currentPlatformId += ",${gameEntity.platformId}"
                }

                gameEntity.platformId = currentPlatformId
            }
        }
        insertGames(gameEntities)
    }

    @Query("DELETE FROM games WHERE platformId LIKE '%' || :platform || '%'")
    suspend fun clearGames(platform: String)
}