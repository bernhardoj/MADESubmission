package com.example.madesubmission.core.domain.repository

import com.example.madesubmission.core.FakeDataGenerator
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.data.source.local.LocalDataSource
import com.example.madesubmission.core.data.source.local.entity.GameEntity
import com.example.madesubmission.core.data.source.remote.RemoteDataSource
import com.example.madesubmission.core.data.source.remote.response.GameDetailResponse
import com.example.madesubmission.core.data.source.remote.response.GameResponse
import com.example.madesubmission.core.utils.DataMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.*
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GameRepositoryTest {

    private val id = 1
    private val platformId = "1"

    private lateinit var repository: GameRepository

    @Mock
    private lateinit var remote: RemoteDataSource

    @Mock
    private lateinit var local: LocalDataSource

    @Before
    fun setUp() {
        repository = GameRepository(remote, local)
    }

    @Test
    fun getAllGames_without_call() {
        runBlockingTest {
            val list = FakeDataGenerator.generateGameEntityList()
            val flowLocal = flow {
                emit(list)
            }

            `when`(local.getAllGames(platformId)).thenReturn(flowLocal)
            val response = repository.getAllGames(platformId)

            assertThat(response.count()).isEqualTo(2)
            assertThat(response.first()).isInstanceOf(Resource.Loading::class.java)
            assertThat(response.drop(1).first()).isInstanceOf(Resource.Success::class.java)

            val success = response.drop(1).first() as Resource.Success
            assertThat(success.data).isEqualTo(list.map { DataMapper.entityToDomain(it) })
        }
    }

    @Test
    fun getAllGames_with_call() {
        runBlockingTest {
            val list = FakeDataGenerator.generateGameResponseList()
            var entityList = listOf<GameEntity>()
            val flowLocal = flow {
                emit(entityList)
            }
            val flowRemote = flow {
                emit(Resource.Success(list))
            }

            `when`(local.getAllGames(platformId)).thenReturn(flowLocal)
            `when`(remote.getAllGames(platformId)).thenReturn(flowRemote)
            doAnswer { _ ->
                entityList = list.map { DataMapper.responsesToEntity(it, platformId) }
                null
            }.`when`(local).insertGames(list.map {
                DataMapper.responsesToEntity(it, platformId)
            })

            val response = repository.getAllGames(platformId)

            assertThat(response.count()).isEqualTo(2)
            assertThat(response.first()).isInstanceOf(Resource.Loading::class.java)
            assertThat(response.drop(1).first()).isInstanceOf(Resource.Success::class.java)

            val success = response.drop(1).first() as Resource.Success
            assertThat(success.data).isEqualTo(entityList.map { DataMapper.entityToDomain(it) })
        }
    }

    @Test
    fun getAllGames_with_call_expired() {
        runBlockingTest {
            val entityList = FakeDataGenerator.generateGameEntityList(true)
            val flowLocal = flow {
                emit(entityList)
            }
            val flowRemote = flow {
                emit(Resource.Error<List<GameResponse>>("No internet connection"))
            }

            `when`(local.getAllGames(platformId)).thenReturn(flowLocal)
            `when`(remote.getAllGames(platformId)).thenReturn(flowRemote)

            val response = repository.getAllGames(platformId)

            assertThat(response.count()).isEqualTo(2)
            assertThat(response.first()).isInstanceOf(Resource.Loading::class.java)
            assertThat(response.drop(1).first()).isInstanceOf(Resource.Success::class.java)

            val success = response.drop(1).first() as Resource.Success
            assertThat(success.data).isEqualTo(entityList.map { DataMapper.entityToDomain(it) })
        }
    }

    @Test
    fun getAllGames_with_call_error() {
        runBlockingTest {
            val entityList = listOf<GameEntity>()
            val flowLocal = flow {
                emit(entityList)
            }
            val flowRemote = flow {
                emit(Resource.Error<List<GameResponse>>("No internet connection"))
            }

            `when`(local.getAllGames(platformId)).thenReturn(flowLocal)
            `when`(remote.getAllGames(platformId)).thenReturn(flowRemote)

            val response = repository.getAllGames(platformId)

            assertThat(response.count()).isEqualTo(2)
            assertThat(response.first()).isInstanceOf(Resource.Loading::class.java)
            assertThat(response.drop(1).first()).isInstanceOf(Resource.Error::class.java)

            val error = response.drop(1).first() as Resource.Error
            assertThat(error.message).isEqualTo("No internet connection")
        }
    }

    @Test
    fun getGameDetail_without_call() {
        runBlockingTest {
            val detailEntity = FakeDataGenerator.generateGameDetailEntity()
            val flowLocal = flow {
                emit(detailEntity)
            }

            `when`(local.getGameDetail(id)).thenReturn(flowLocal)
            val response = repository.getGameDetail(id)

            assertThat(response.count()).isEqualTo(2)
            assertThat(response.first()).isInstanceOf(Resource.Loading::class.java)
            assertThat(response.drop(1).first()).isInstanceOf(Resource.Success::class.java)

            val success = response.drop(1).first() as Resource.Success
            assertThat(success.data).isEqualTo(DataMapper.entityToDomain(detailEntity) )
        }
    }

    @Test
    fun getGameDetail_with_call() {
        runBlockingTest {
            val detailFromRemote = FakeDataGenerator.generateGameDetailResponse()
            var detailFromLocal = FakeDataGenerator.generateGameDetailEntity(id=-1)
            val flowLocal = flow {
                emit(detailFromLocal)
            }
            val flowRemote = flow {
                emit(Resource.Success(detailFromRemote))
            }

            `when`(local.getGameDetail(id)).thenReturn(flowLocal)
            `when`(remote.getGameDetail(id)).thenReturn(flowRemote)
            doAnswer {
                detailFromLocal = DataMapper.responsesToEntity(detailFromRemote, false)
                null
            }.`when`(local).insertGameDetail(DataMapper.responsesToEntity(detailFromRemote, false))

            val response = repository.getGameDetail(id)

            assertThat(response.count()).isEqualTo(2)
            assertThat(response.first()).isInstanceOf(Resource.Loading::class.java)
            assertThat(response.drop(1).first()).isInstanceOf(Resource.Success::class.java)

            val success = response.drop(1).first() as Resource.Success
            assertThat(success.data).isEqualTo(DataMapper.entityToDomain(detailFromLocal))
        }
    }

    @Test
    fun getGameDetail_with_call_expired() {
        runBlockingTest {
            val detailFromLocal = FakeDataGenerator.generateGameDetailEntity(expired = true)
            val flowLocal = flow {
                emit(detailFromLocal)
            }
            val flowRemote = flow {
                emit(Resource.Error<GameDetailResponse>("No internet connection"))
            }

            `when`(local.getGameDetail(id)).thenReturn(flowLocal)
            `when`(remote.getGameDetail(id)).thenReturn(flowRemote)

            val response = repository.getGameDetail(id)

            assertThat(response.count()).isEqualTo(2)
            assertThat(response.first()).isInstanceOf(Resource.Loading::class.java)
            assertThat(response.drop(1).first()).isInstanceOf(Resource.Success::class.java)

            val success = response.drop(1).first() as Resource.Success
            assertThat(success.data).isEqualTo(DataMapper.entityToDomain(detailFromLocal))
        }
    }

    @Test
    fun getGameDetail_with_call_error() {
        runBlockingTest {
            val detailFromLocal = FakeDataGenerator.generateGameDetailEntity(id=-1)
            val flowLocal = flow {
                emit(detailFromLocal)
            }
            val flowRemote = flow {
                emit(Resource.Error<GameDetailResponse>("No internet connection"))
            }

            `when`(local.getGameDetail(id)).thenReturn(flowLocal)
            `when`(remote.getGameDetail(id)).thenReturn(flowRemote)

            val response = repository.getGameDetail(id)

            assertThat(response.count()).isEqualTo(2)
            assertThat(response.first()).isInstanceOf(Resource.Loading::class.java)
            assertThat(response.drop(1).first()).isInstanceOf(Resource.Error::class.java)

            val error = response.drop(1).first() as Resource.Error
            assertThat(error.message).isEqualTo("No internet connection")
        }
    }
}