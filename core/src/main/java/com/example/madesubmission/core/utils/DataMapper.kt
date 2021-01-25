package com.example.madesubmission.core.utils

import com.example.madesubmission.core.data.source.local.entity.GameDetailEntity
import com.example.madesubmission.core.data.source.local.entity.GameEntity
import com.example.madesubmission.core.data.source.remote.response.GameDetailResponse
import com.example.madesubmission.core.data.source.remote.response.GameResponse
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.model.GameDetail
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

object DataMapper {
    private const val UNKNOWN = "N/A"

    fun responsesToEntities(responses: List<GameResponse>, platformId: String): List<GameEntity> =
        responses.map {
            GameEntity(
                it.id,
                it.name,
                it.imageUrl,
                it.rating,
                if (it.genres == null || it.genres.isEmpty()) UNKNOWN
                else it.genres.joinToString(", ") { genre ->
                    genre.name
                },
                DayUtil.getCurrentDay(),
                false,
                platformId
            )
        }

    fun responsesToEntities(response: GameDetailResponse, isFavorite: Boolean): GameDetailEntity =
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

    fun entityToDomain(gameEntities: List<GameEntity>): List<Game> = gameEntities.map {
        Game(
            it.id,
            it.name,
            it.imageUrl,
            it.rating,
            it.genres,
            it.updated,
            it.isFavorite,
            it.platformId
        )
    }

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

    fun entityToDomain(gameDetailEntities: GameDetailEntity?): GameDetail {
        val gson = GsonBuilder().create()
        return GameDetail(
            gameDetailEntities?.id ?: -1,
            gameDetailEntities?.description ?: UNKNOWN,
            gameDetailEntities?.releaseDate ?: UNKNOWN,
            gameDetailEntities?.platforms ?: UNKNOWN,
            gameDetailEntities?.developers ?: UNKNOWN,
            gameDetailEntities?.publishers ?: UNKNOWN,
            gameDetailEntities?.updated ?: 0,
            gson.fromJson(
                gameDetailEntities?.screenshots,
                object : TypeToken<List<String>>() {}.type
            ) ?: listOf(),
            gameDetailEntities?.isFavorite ?: false
        )
    }

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
}