package com.example.madesubmission.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GameDetailResponse(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("released")
    val releaseDate: String?,

    @field:SerializedName("genres")
    val genres: List<GenreResponse>?,

    @field:SerializedName("platforms")
    val platforms: List<PlatformResponse>?,

    @field:SerializedName("developers")
    val developers: List<DeveloperResponse>?,

    @field:SerializedName("publishers")
    val publishers: List<PublisherResponse>?,

    var screenshots: List<String>?
)
