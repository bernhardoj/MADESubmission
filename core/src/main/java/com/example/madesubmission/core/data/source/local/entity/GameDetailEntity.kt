package com.example.madesubmission.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_detail")
data class GameDetailEntity(
    @PrimaryKey
    val id: Int,
    val description: String,
    val releaseDate: String,
    val platforms: String,
    val developers: String,
    val publishers: String,
    val updated: Int,
    val screenshots: String,
    val isFavorite: Boolean
)
