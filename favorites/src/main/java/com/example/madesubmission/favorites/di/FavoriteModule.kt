package com.example.madesubmission.favorites.di

import com.example.madesubmission.favorites.ui.FavoriteViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { FavoriteViewModel(get()) }
}