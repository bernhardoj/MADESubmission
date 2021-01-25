package com.example.madesubmission.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GameResponse(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("background_image")
    val imageUrl: String?,

    @field:SerializedName("rating")
    val rating: Double,

    @field:SerializedName("genres")
    val genres: List<GenreResponse>?
)