package com.example.madesubmission.core.data.source.local.entity

import androidx.room.Entity

@Entity
data class GameUpdateEntity(
    val id: Int,
    val isFavorite: Boolean
)
