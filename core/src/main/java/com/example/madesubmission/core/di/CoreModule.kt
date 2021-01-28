package com.example.madesubmission.core.di

import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.example.madesubmission.core.data.source.local.LocalDataSource
import com.example.madesubmission.core.data.source.local.room.GameDatabase
import com.example.madesubmission.core.data.source.remote.RemoteDataSource
import com.example.madesubmission.core.data.source.remote.network.ApiService
import com.example.madesubmission.core.domain.repository.GameRepository
import com.example.madesubmission.core.domain.repository.IGameRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<GameDatabase>().gameDao() }
    factory { get<GameDatabase>().recentSearchDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            GameDatabase::class.java,
            "Game.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.rawg.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

@ExperimentalPagingApi
val repositoryModule = module {
    single { RemoteDataSource(get(), get()) }
    single { LocalDataSource(get(), get()) }
    single<IGameRepository> { GameRepository(get(), get()) }
}