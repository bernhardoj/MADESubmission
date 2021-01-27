package com.example.madesubmission.core.data.source.remote

import android.content.Context
import com.example.madesubmission.core.BuildConfig
import com.example.madesubmission.core.R
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.data.source.remote.network.ApiService
import com.example.madesubmission.core.data.source.remote.response.GameDetailResponse
import com.example.madesubmission.core.data.source.remote.response.GameResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService, private val context: Context) {
    suspend fun getAllGames(platform: String): Flow<Resource<List<GameResponse>>> = flow {
        try {
            emit(Resource.Success(apiService.getGameList(BuildConfig.API_KEY, platform).games))
        } catch (e: Exception) {
            emit(Resource.Error<List<GameResponse>>(context.getString(R.string.network_error)))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun searchGames(query: String, page: Int) = apiService.searchGames(BuildConfig.API_KEY, page, query)

    suspend fun getGameDetail(id: Int): Flow<Resource<GameDetailResponse>> = flow {
        try {
            val gameDetail = apiService.getGameDetail(id, BuildConfig.API_KEY)
            val gameScreenshot = apiService.getScreenshots(id, BuildConfig.API_KEY)
            gameDetail.screenshots = gameScreenshot.screenshotUrl.map {
                it.urls
            }
            emit(Resource.Success(gameDetail))
        } catch (e: Exception) {
            emit(Resource.Error<GameDetailResponse>(context.getString(R.string.network_error)))
        }
    }.flowOn(Dispatchers.IO)
}