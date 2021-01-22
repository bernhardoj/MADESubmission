package com.example.madesubmission.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GameListResponse(
    @field:SerializedName("results")
    val games: List<GameResponse>
)