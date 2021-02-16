package com.example.madesubmission

import com.example.madesubmission.core.data.source.local.entity.GameUpdateEntity
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.model.GameDetail

object FakeDataGenerator {
    fun generateGameList(): List<Game> {
        val games = arrayListOf<Game>()
        games.add(Game(
            1,
            "name",
            "https://test.comm",
            5.0,
            "action",
            24,
            false,
            "1"
        ))
        return games
    }

    fun generateGame(isFavorite: Boolean): Game = Game(
        1,
        "name",
        "https://test.com",
        5.0,
        "action, comedy",
        21,
        isFavorite,
        "1"
    )

    fun generateGameDetail(): GameDetail = GameDetail(
        1,
        "desc",
        "10-10-2020",
        "playstation",
        "dev 1",
        "pub 1, pub 2",
        20,
        listOf(),
        false
    )

    fun generateUpdateEntity(isFavorite: Boolean): GameUpdateEntity = GameUpdateEntity(
        1,
        isFavorite
    )
}