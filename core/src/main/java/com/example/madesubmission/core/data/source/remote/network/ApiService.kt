package com.example.madesubmission.core.data.source.remote.network

import com.example.madesubmission.core.data.source.remote.response.GameDetailResponse
import com.example.madesubmission.core.data.source.remote.response.GameListResponse
import com.example.madesubmission.core.data.source.remote.response.ScreenshotResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("games?ordering=-rating&page=1&page_size=20")
    suspend fun getGameList(
        @Query("key") api: String,
        @Query("parent_platforms") platforms: String
    ): GameListResponse

    @GET("games?page_size=20")
    suspend fun searchGames(
        @Query("key") api: String,
        @Query("page") page: Int,
        @Query("search") query: String
    ): GameListResponse

    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") id: Int,
        @Query("key") api: String
    ): GameDetailResponse

    @GET("games/{id}/screenshots")
    suspend fun getScreenshots(
        @Path("id") id: Int,
        @Query("key") api: String
    ): ScreenshotResponse
}