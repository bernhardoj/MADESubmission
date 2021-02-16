package com.example.madesubmission.ui.explore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.madesubmission.FakeDataGenerator
import com.example.madesubmission.core.data.Resource
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.usecase.GameUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class ExploreListViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var useCase: GameUseCase
    private lateinit var viewModel: ExploreListViewModel
    private val platform = "1"

    @Mock
    private lateinit var observer: Observer<Resource<List<Game>>>

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
        viewModel = ExploreListViewModel(platform, useCase)
    }

    @After
    fun tearUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadAllGames_Success() {
        runBlockingTest {
            val fakeLoading = Resource.Loading<List<Game>>()
            val fakeList = Resource.Success(FakeDataGenerator.generateGameList())
            val response = flow {
                emit(fakeLoading)
                emit(fakeList)
            }
            `when`(useCase.getAllGames(platform)).thenReturn(response)
            viewModel.gameLiveData.observeForever(observer)
            viewModel.loadAllGames()

            verify(observer).onChanged(fakeLoading)
            verify(observer).onChanged(fakeList)
        }
    }

    @Test
    fun loadAllGames_Error() {
        runBlockingTest {
            val fakeLoading = Resource.Loading<List<Game>>()
            val fakeError = Resource.Error<List<Game>>("No internet connection")
            val response = flow {
                emit(fakeLoading)
                emit(fakeError)
            }
            `when`(useCase.getAllGames(platform)).thenReturn(response)
            viewModel.gameLiveData.observeForever(observer)
            viewModel.loadAllGames()

            verify(observer).onChanged(fakeLoading)
            verify(observer).onChanged(fakeError)
        }
    }
}