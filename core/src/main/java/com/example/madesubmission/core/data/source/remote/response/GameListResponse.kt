package com.example.madesubmission.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GameListResponse(
    @field:SerializedName("next")
    val nextPage: String?,

    @field:SerializedName("results")
    val games: List<GameResponse>
)