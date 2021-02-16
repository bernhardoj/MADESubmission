package com.example.madesubmission.favorites.ui

import androidx.paging.PagingData
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.usecase.GameUseCase
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoriteViewModelTest {
    @Mock
    private lateinit var useCase: GameUseCase

    private lateinit var viewModel: FakeFavoriteViewModel

    @Before
    fun setUp() {
        viewModel = FakeFavoriteViewModel(useCase)
    }

    @Test
    fun testGetFavorites() {
        val flow = flow<PagingData<Game>> { }
        `when`(useCase.getFavorites()).thenReturn(flow)
        viewModel.getFavorites()
        verify(useCase).getFavorites()

        viewModel.getFavorites()
        verify(useCase, times(1)).getFavorites()
    }
}