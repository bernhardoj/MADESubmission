package com.example.madesubmission

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.example.madesubmission.core.di.databaseModule
import com.example.madesubmission.core.di.networkModule
import com.example.madesubmission.core.di.repositoryModule
import com.example.madesubmission.di.useCaseModule
import com.example.madesubmission.di.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@FlowPreview
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@Suppress("unused")
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    viewModelModule,
                    repositoryModule,
                    useCaseModule
                )
            )
        }
    }
}