package com.example.madesubmission.core.data.paging

import androidx.paging.PagingSource
import com.example.madesubmission.core.BuildConfig
import com.example.madesubmission.core.FakeDataGenerator
import com.example.madesubmission.core.data.source.remote.network.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GamesPagingSourceTest {
    private lateinit var pagingSource: GamesPagingSource

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var loadParams: PagingSource.LoadParams<Int>

    private val query = "test"

    @Before
    fun setUp() {
        pagingSource = GamesPagingSource(query, apiService)
    }

    @Test
    fun load_page() {
        runBlockingTest {
            `when`(loadParams.key).thenReturn(null)
            `when`(apiService.searchGames(BuildConfig.API_KEY, 1, query)).thenReturn(
                FakeDataGenerator.generateGameListResponse("1")
            )

            pagingSource.load(loadParams)
            verify(apiService).searchGames(BuildConfig.API_KEY, 1, query)

            `when`(loadParams.key).thenReturn(2)

            pagingSource.load(loadParams)
            verify(apiService).searchGames(BuildConfig.API_KEY, 2, query)
        }
    }
}