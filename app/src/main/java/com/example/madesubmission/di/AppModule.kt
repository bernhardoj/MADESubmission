package com.example.madesubmission.di

import com.example.madesubmission.core.domain.usecase.GameInteractor
import com.example.madesubmission.core.domain.usecase.GameUseCase
import com.example.madesubmission.ui.detail.DetailViewModel
import com.example.madesubmission.ui.explore.ExploreListViewModel
import com.example.madesubmission.ui.search.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    single<GameUseCase> { GameInteractor(get()) }
}

@ExperimentalCoroutinesApi
@FlowPreview
val viewModelModule = module {
    viewModel { (platform: String) -> ExploreListViewModel(platform, get()) }
    viewModel { (id: Int) -> DetailViewModel(id, get()) }
    viewModel { SearchViewModel(get()) }
}