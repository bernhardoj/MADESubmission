package com.example.madesubmission.core.domain.model

data class GameDetail(
    val id: Int,
    val description: String,
    val releaseDate: String,
    val platforms: String,
    val developers: String,
    val publishers: String,
    val updated: Int,
    val screenshots: List<String>,
    val isFavorite: Boolean
)