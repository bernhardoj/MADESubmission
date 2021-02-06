package com.example.madesubmission.core.utils

import com.example.madesubmission.core.data.source.local.entity.GameDetailEntity
import com.example.madesubmission.core.data.source.local.entity.GameEntity
import com.example.madesubmission.core.data.source.local.entity.RecentSearchEntity
import com.example.madesubmission.core.data.source.remote.response.GameDetailResponse
import com.example.madesubmission.core.data.source.remote.response.GameResponse
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.model.GameDetail
import com.example.madesubmission.core.domain.model.RecentSearch
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

object DataMapper {
    private const val UNKNOWN = "N/A"

    fun responsesToEntity(response: GameResponse, platformId: String): GameEntity =
        GameEntity(
            response.id,
            response.name,
            response.imageUrl,
            response.rating,
            if (response.genres == null || response.genres.isEmpty()) UNKNOWN
            else response.genres.joinToString(", ") { genre ->
                genre.name
            },
            DayUtil.getCurrentDay(),
            false,
            platformId
        )

    fun responsesToEntity(response: GameDetailResponse, isFavorite: Boolean): GameDetailEntity =
        GameDetailEntity(
            response.id,
            response.description,
            response.releaseDate ?: UNKNOWN,
            if (response.platforms == null || response.platforms.isEmpty()) UNKNOWN
            else response.platforms.joinToString(separator = ", ") {
                it.platformName.name
            },
            if (response.developers == null || response.developers.isEmpty()) UNKNOWN
            else response.developers.joinToString(separator = ", ") {
                it.name
            },
            if (response.publishers == null || response.publishers.isEmpty()) UNKNOWN
            else response.publishers.joinToString(separator = ", ") {
                it.name
            },
            DayUtil.getCurrentDay(),
            GsonBuilder().create().toJson(response.screenshots),
            isFavorite
        )

    fun entityToDomain(gameEntity: GameEntity): Game = Game(
        gameEntity.id,
        gameEntity.name,
        gameEntity.imageUrl,
        gameEntity.rating,
        gameEntity.genres,
        gameEntity.updated,
        gameEntity.isFavorite,
        gameEntity.platformId
    )

    fun entityToDomain(gameDetailEntity: GameDetailEntity?): GameDetail {
        val gson = GsonBuilder().create()
        return GameDetail(
            gameDetailEntity?.id ?: -1,
            gameDetailEntity?.description ?: UNKNOWN,
            gameDetailEntity?.releaseDate ?: UNKNOWN,
            gameDetailEntity?.platforms ?: UNKNOWN,
            gameDetailEntity?.developers ?: UNKNOWN,
            gameDetailEntity?.publishers ?: UNKNOWN,
            gameDetailEntity?.updated ?: 0,
            gson.fromJson(
                gameDetailEntity?.screenshots,
                object : TypeToken<List<String>>() {}.type
            ) ?: listOf(),
            gameDetailEntity?.isFavorite ?: false
        )
    }

    fun entityToDomain(recentSearchEntity: RecentSearchEntity): RecentSearch =
        RecentSearch(recentSearchEntity.query)

    fun domainToEntity(game: Game): GameEntity {
        return GameEntity(
            game.id,
            game.name,
            game.imageUrl,
            game.rating,
            game.genres,
            game.updated,
            true,
            game.platformId
        )
    }

    fun domainToEntity(recentSearch: RecentSearch): RecentSearchEntity =
        RecentSearchEntity(recentSearch.query)
}