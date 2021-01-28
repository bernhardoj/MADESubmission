package com.example.madesubmission.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.madesubmission.core.data.source.local.entity.GameDetailEntity
import com.example.madesubmission.core.data.source.local.entity.GameEntity
import com.example.madesubmission.core.data.source.local.entity.RecentSearchEntity

@Database(entities = [GameEntity::class, GameDetailEntity::class, RecentSearchEntity::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun recentSearchDao(): RecentSearchDao
}