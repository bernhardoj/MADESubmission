package com.example.madesubmission.core.domain.repository

import androidx.paging.*
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.data.source.NetworkBoundResource
import com.example.madesubmission.core.data.source.local.LocalDataSource
import com.example.madesubmission.core.data.source.local.entity.GameUpdateEntity
import com.example.madesubmission.core.data.source.remote.RemoteDataSource
import com.example.madesubmission.core.data.source.remote.response.GameDetailResponse
import com.example.madesubmission.core.data.source.remote.response.GameResponse
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.model.GameDetail
import com.example.madesubmission.core.utils.DataMapper
import com.example.madesubmission.core.utils.DayUtil
import kotlinx.coroutines.flow.*

class GameRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IGameRepository {
    override fun getAllGames(platform: String): Flow<Resource<List<Game>>> =
        object : NetworkBoundResource<List<Game>, List<GameResponse>>() {
            override fun loadFromDb(): Flow<List<Game>> {
                return localDataSource.getAllGames(platform).map {
                    DataMapper.entityToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Game>): Int {
                if (data.isEmpty()) return EMPTY
                return isExpired(data)
            }

            override suspend fun createCall(): Flow<Resource<List<GameResponse>>> =
                remoteDataSource.getAllGames(platform = platform)

            override suspend fun saveCallResult(data: List<GameResponse>) {
                localDataSource.insertGames(DataMapper.responsesToEntities(data, platform))
            }

            override fun isExpired(data: List<Game>): Int {
                val currentDay = DayUtil.getCurrentDay()
                if (currentDay - data[0].updated != 0) return EXPIRED
                return GOOD
            }
        }.asFlow()

    override fun searchGames(query: String): Flow<Resource<List<Game>>> {
        return flow {
            emit(Resource.Loading())

            when (val response = remoteDataSource.getAllGames(query).first()) {
                is Resource.Success -> {
                    val entities = DataMapper.responsesToEntities(response.data, "")
                    val domain = DataMapper.entityToDomain(entities)
                    emit(Resource.Success(domain))
                }
                is Resource.Error -> {
                    emit(Resource.Error<List<Game>>(response.message))
                }
            }
        }
    }

    override fun getGameDetail(id: Int): Flow<Resource<GameDetail>> =
        object : NetworkBoundResource<GameDetail, GameDetailResponse>() {
            override fun loadFromDb(): Flow<GameDetail> {
                return localDataSource.getGameDetail(id).map {
                    DataMapper.entityToDomain(it)
                }
            }

            override fun shouldFetch(data: GameDetail): Int {
                if (data.id == -1) return EMPTY
                return isExpired(data)
            }

            override suspend fun createCall(): Flow<Resource<GameDetailResponse>> {
                return remoteDataSource.getGameDetail(id)
            }

            override suspend fun saveCallResult(data: GameDetailResponse) {
                val dbSource = loadFromDb().first()
                localDataSource.insertGameDetail(DataMapper.responsesToEntities(data, dbSource.isFavorite))
            }

            override fun isExpired(data: GameDetail): Int {
                val currentDay = DayUtil.getCurrentDay()
                if (currentDay - data.updated != 0) return EXPIRED
                return GOOD
            }
        }.asFlow()

    override fun getFavorites(): Flow<PagingData<Game>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { localDataSource.getFavorites() }
        ).flow.map {
            it.map { entity ->
                DataMapper.entityToDomain(entity)
            }
        }
    }

    override suspend fun insertGame(game: Game) = localDataSource.insertGame(DataMapper.domainToEntity(game))

    override suspend fun deleteGame(game: Game) = localDataSource.deleteGame(DataMapper.domainToEntity(game))

    override suspend fun updateFavoriteGame(gameDetail: GameUpdateEntity) {
        localDataSource.updateFavoriteGame(gameDetail)
    }
}