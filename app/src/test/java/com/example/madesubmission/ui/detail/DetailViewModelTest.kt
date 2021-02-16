package com.example.madesubmission.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.madesubmission.FakeDataGenerator
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.domain.model.GameDetail
import com.example.madesubmission.core.domain.usecase.GameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class DetailViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var favoriteObserver: Observer<Boolean>

    @Mock
    private lateinit var detailObserver: Observer<Resource<GameDetail>>

    @Mock
    private lateinit var useCase: GameUseCase

    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
        viewModel = DetailViewModel(1, useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadDetail_Success() {
        runBlockingTest {
            val fakeLoading = Resource.Loading<GameDetail>()
            val fakeDetail = Resource.Success(FakeDataGenerator.generateGameDetail())
            val response = flow {
                emit(fakeLoading)
                emit(fakeDetail)
            }
            Mockito.`when`(useCase.getGameDetail(1)).thenReturn(response)
            viewModel.gameDetailLiveData.observeForever(detailObserver)
            viewModel.loadDetail()

            verify(detailObserver).onChanged(fakeLoading)
            verify(detailObserver).onChanged(fakeDetail)
        }
    }

    @Test
    fun loadDetail_Error() {
        runBlockingTest {
            val fakeLoading = Resource.Loading<GameDetail>()
            val fakeError = Resource.Error<GameDetail>("No intenet connection")
            val response = flow {
                emit(fakeLoading)
                emit(fakeError)
            }
            Mockito.`when`(useCase.getGameDetail(1)).thenReturn(response)
            viewModel.gameDetailLiveData.observeForever(detailObserver)
            viewModel.loadDetail()

            verify(detailObserver).onChanged(fakeLoading)
            verify(detailObserver).onChanged(fakeError)
        }
    }

    @Test
    fun setFavorite() {
        viewModel.favoriteLiveData.observeForever(favoriteObserver)

        viewModel.setFavorite(true)
        verify(favoriteObserver).onChanged(true)

        viewModel.setFavorite(false)
        verify(favoriteObserver).onChanged(false)
    }

    @Test
    fun updateFavorite_favorite_true() {
        runBlocking {
            val game = FakeDataGenerator.generateGame(true)
            viewModel.updateFavorite(true, game)

            verify(useCase).updateFavoriteGame(FakeDataGenerator.generateUpdateEntity(true))
            verify(useCase).insertGame(game)
        }
    }

    @Test
    fun updateFavorite_favorite_false() {
        runBlocking {
            val game = FakeDataGenerator.generateGame(false)
            viewModel.updateFavorite(false, game)

            verify(useCase).updateFavoriteGame(FakeDataGenerator.generateUpdateEntity(false))
            verify(useCase).deleteGame(game.id)
        }
    }
}