package com.example.madesubmission.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.model.RecentSearch
import com.example.madesubmission.core.domain.usecase.GameUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.*
import org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner::class)
@ObsoleteCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var useCase: GameUseCase

    @Mock
    private lateinit var recentSearchObserver: Observer<List<RecentSearch>>

    private lateinit var viewModel: SearchViewModel

    private lateinit var fakeViewModel: FakeSearchViewModel

    private val list = listOf(RecentSearch("q1"), RecentSearch("q2"))

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)

        val flow = flow {
            emit(list)
        }
        `when`(useCase.getRecentSearch()).thenReturn(flow)
        viewModel = SearchViewModel(useCase)
        fakeViewModel = FakeSearchViewModel(useCase)
        viewModel.recentSearchLiveData.observeForever(recentSearchObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_test() {
        verify(recentSearchObserver).onChanged(list)
        assertThat(viewModel.queryChannel.value).isEqualTo("")
    }

    @Test
    fun search_games() {
        val q1 = "test 1"
        val flow = flow<PagingData<Game>> { }
        `when`(useCase.searchGames(q1)).thenReturn(flow)
        fakeViewModel.searchGames(q1)
        verify(useCase).searchGames(q1)

        fakeViewModel.searchGames(q1)
        verify(useCase, times(1)).searchGames(q1)
    }
}