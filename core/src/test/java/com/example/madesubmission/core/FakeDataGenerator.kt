package com.example.madesubmission.core

import com.example.madesubmission.core.data.source.local.entity.GameDetailEntity
import com.example.madesubmission.core.data.source.local.entity.GameEntity
import com.example.madesubmission.core.data.source.remote.response.GameDetailResponse
import com.example.madesubmission.core.data.source.remote.response.GameListResponse
import com.example.madesubmission.core.data.source.remote.response.GameResponse
import com.example.madesubmission.core.data.source.remote.response.GenreResponse
import com.example.madesubmission.core.utils.DayUtil

object FakeDataGenerator {
    fun generateGameEntityList(expired: Boolean = false): List<GameEntity> {
        val list = arrayListOf<GameEntity>()
        for (i in 0..19) {
            list.add(
                GameEntity(
                    i,
                    "name",
                    "https://test.com",
                    5.0,
                    "action, comedy",
                    if (expired) 0 else DayUtil.getCurrentDay(),
                    false,
                    "1"
                )
            )
        }
        return list
    }

    fun generateGameResponseList(): List<GameResponse> {
        val list = arrayListOf<GameResponse>()
        for (i in 0..19) {
            list.add(
                GameResponse(
                    i,
                    "name",
                    "https://test.com",
                    5.0,
                    listOf(GenreResponse("action"), GenreResponse("comedy")),
                )
            )
        }
        return list
    }

    fun generateGameDetailEntity(expired: Boolean = false, id: Int = 1): GameDetailEntity = GameDetailEntity(
        id,
        "desc",
        "10-10-2010",
        "platform 1",
        "dev 1, dev 2",
        "pub 1",
        if (expired) 0 else DayUtil.getCurrentDay(),
        "null",
        false
    )

    fun generateGameDetailResponse(): GameDetailResponse = GameDetailResponse(
        1,
        "desc",
        "10-10-2010",
        null,
        null,
        null,
        null
    )

    fun generateGameListResponse(nextPage: String?): GameListResponse = GameListResponse(
        nextPage,
        generateGameResponseList()
    )
}