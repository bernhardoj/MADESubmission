package com.example.madesubmission.core.data.source.remote

import com.example.madesubmission.core.BuildConfig
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.data.source.remote.network.ApiService
import com.example.madesubmission.core.data.source.remote.response.GameDetailResponse
import com.example.madesubmission.core.data.source.remote.response.GameResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getAllGames(query: String = "", platform: String = ""): Flow<Resource<List<GameResponse>>> = flow {
        try {
            if (query.isEmpty()) emit(Resource.Success(apiService.getGameList(BuildConfig.API_KEY, platform).games))
            else emit(Resource.Success(apiService.searchGames(BuildConfig.API_KEY, 1, query).games))
        } catch (e: Exception) {
            emit(Resource.Error<List<GameResponse>>("Failed to connect. Please try again!"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getGameDetail(id: Int): Flow<Resource<GameDetailResponse>> = flow {
        try {
            val gameDetail = apiService.getGameDetail(id, BuildConfig.API_KEY)
            val gameScreenshot = apiService.getScreenshots(id, BuildConfig.API_KEY)
            gameDetail.screenshots = gameScreenshot.screenshotUrl.map {
                it.urls
            }
            emit(Resource.Success(gameDetail))
        } catch (e: Exception) {
            emit(Resource.Error<GameDetailResponse>("Failed to connect. Please try again!"))
        }
    }.flowOn(Dispatchers.IO)
}