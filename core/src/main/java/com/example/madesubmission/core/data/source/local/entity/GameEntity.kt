package com.example.madesubmission.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val rating: Double,
    val genres: String,
    val updated: Int,
    val isFavorite: Boolean,
    var platformId: String
)