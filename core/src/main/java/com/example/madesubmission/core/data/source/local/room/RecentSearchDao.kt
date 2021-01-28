package com.example.madesubmission.core.data.source.local.room

import androidx.room.*
import com.example.madesubmission.core.data.source.local.entity.RecentSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(recentSearch: RecentSearchEntity)

    @Query("SELECT * FROM recent_search")
    fun getRecentSearch(): Flow<List<RecentSearchEntity>>

    @Delete
    suspend fun delete(recentSearch: RecentSearchEntity)

    @Query("DELETE FROM recent_search")
    suspend fun clear()
}