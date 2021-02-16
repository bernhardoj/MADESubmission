package com.example.madesubmission.core.utils

import com.example.madesubmission.core.data.source.local.entity.GameDetailEntity
import com.example.madesubmission.core.data.source.local.entity.GameEntity
import com.example.madesubmission.core.data.source.local.entity.RecentSearchEntity
import com.example.madesubmission.core.data.source.remote.response.*
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.domain.model.RecentSearch

import com.google.common.truth.Truth.assertThat
import com.google.gson.GsonBuilder

import org.junit.Test
class DataMapperTest {
    @Test
    fun testResponsesToEntityNoNull() {
        val response = GameResponse(
            1,
            "test",
            "https://test.com",
            5.0,
            listOf(GenreResponse("action"), GenreResponse("romance"))
        )
        val entity = DataMapper.responsesToEntity(response, "1")
        assertThat(entity.id).isEqualTo(response.id)
        assertThat(entity.name).isEqualTo(response.name)
        assertThat(entity.imageUrl).isEqualTo(response.imageUrl)
        assertThat(entity.rating).isEqualTo(response.rating)
        assertThat(entity.genres).isEqualTo("action, romance")
        assertThat(entity.isFavorite).isFalse()
        assertThat(entity.platformId).isEqualTo("1")
    }

    @Test
    fun testResponsesToEntityNull() {
        val response = GameResponse(
            1,
            "test",
            "https://test.com",
            5.0,
            null
        )
        val entity = DataMapper.responsesToEntity(response, "1")
        assertThat(entity.id).isEqualTo(response.id)
        assertThat(entity.name).isEqualTo(response.name)
        assertThat(entity.imageUrl).isEqualTo(response.imageUrl)
        assertThat(entity.rating).isEqualTo(response.rating)
        assertThat(entity.genres).isEqualTo("N/A")
        assertThat(entity.isFavorite).isFalse()
        assertThat(entity.platformId).isEqualTo("1")
    }

    @Test
    fun testDetailResponsesToEntityNoNull() {
        val detailResponse = GameDetailResponse(
            1,
            "description",
            "10-10-2010",
            listOf(PlatformResponse(PlatformNameResponse("playstation")), PlatformResponse(PlatformNameResponse("xbox"))),
            listOf(DeveloperResponse("developer 1"), DeveloperResponse("developer 2")),
            listOf(PublisherResponse("publisher 1"), PublisherResponse("publisher 2")),
            listOf("https://test.com/ss1", "https://test.com/ss1")
        )
        val entity = DataMapper.responsesToEntity(detailResponse, false)
        assertThat(entity.id).isEqualTo(detailResponse.id)
        assertThat(entity.description).isEqualTo(detailResponse.description)
        assertThat(entity.releaseDate).isEqualTo(detailResponse.releaseDate)
        assertThat(entity.platforms).isEqualTo("playstation, xbox")
        assertThat(entity.developers).isEqualTo("developer 1, developer 2")
        assertThat(entity.isFavorite).isFalse()
        assertThat(entity.publishers).isEqualTo("publisher 1, publisher 2")
        assertThat(entity.screenshots).isEqualTo(GsonBuilder().create().toJson(detailResponse.screenshots))
    }

    @Test
    fun testDetailResponsesToEntityNull() {
        val detailResponse = GameDetailResponse(
            1,
            "description",
            null,
            null,
            null,
            null,
            null
        )
        val entity = DataMapper.responsesToEntity(detailResponse, true)
        assertThat(entity.id).isEqualTo(detailResponse.id)
        assertThat(entity.description).isEqualTo(detailResponse.description)
        assertThat(entity.releaseDate).isEqualTo("N/A")
        assertThat(entity.platforms).isEqualTo("N/A")
        assertThat(entity.developers).isEqualTo("N/A")
        assertThat(entity.isFavorite).isTrue()
        assertThat(entity.publishers).isEqualTo("N/A")
        assertThat(entity.screenshots).isEqualTo("null")
    }

    @Test
    fun testEntityToDomain() {
        val entity = GameEntity(
            1,
            "name",
            "https://test/com",
            5.0,
            "N/A",
            23,
            false,
            "4,8"
        )
        val domain = DataMapper.entityToDomain(entity)
        assertThat(entity.id).isEqualTo(domain.id)
        assertThat(entity.name).isEqualTo(domain.name)
        assertThat(entity.imageUrl).isEqualTo(domain.imageUrl)
        assertThat(entity.rating).isEqualTo(domain.rating)
        assertThat(entity.genres).isEqualTo(domain.genres)
        assertThat(entity.isFavorite).isEqualTo(domain.isFavorite)
        assertThat(entity.updated).isEqualTo(domain.updated)
        assertThat(entity.platformId).isEqualTo(domain.platformId)
    }

    @Test
    fun testNullDetailEntityToDomain() {
        val detailEntity = null
        val domain = DataMapper.entityToDomain(detailEntity)
        assertThat(domain.id).isEqualTo(-1)
        assertThat(domain.description).isEqualTo("N/A")
        assertThat(domain.releaseDate).isEqualTo("N/A")
        assertThat(domain.platforms).isEqualTo("N/A")
        assertThat(domain.developers).isEqualTo("N/A")
        assertThat(domain.publishers).isEqualTo("N/A")
        assertThat(domain.updated).isEqualTo(0)
        assertThat(domain.screenshots).isEqualTo(listOf<String>())
        assertThat(domain.isFavorite).isFalse()
    }

    @Test
    fun testDetailEntityToDomain() {
        val detailEntity = GameDetailEntity(
            1,
            "desc",
            "10-10-2010",
            "playstation",
            "dev 1",
            "pub 1, pub 2",
            24,
            GsonBuilder().create().toJson(listOf("https://test.com/ss")),
            true
        )
        val domain = DataMapper.entityToDomain(detailEntity)
        assertThat(domain.id).isEqualTo(1)
        assertThat(domain.description).isEqualTo("desc")
        assertThat(domain.releaseDate).isEqualTo("10-10-2010")
        assertThat(domain.platforms).isEqualTo("playstation")
        assertThat(domain.developers).isEqualTo("dev 1")
        assertThat(domain.publishers).isEqualTo("pub 1, pub 2")
        assertThat(domain.updated).isEqualTo(24)
        assertThat(domain.screenshots).isEqualTo(listOf("https://test.com/ss"))
        assertThat(domain.isFavorite).isTrue()
    }

    @Test
    fun testRecentSearchEntityToDomain() {
        val recentSearchEntity = RecentSearchEntity("dota")
        val domain = DataMapper.entityToDomain(recentSearchEntity)
        assertThat(domain.query).isEqualTo(recentSearchEntity.query)
    }

    @Test
    fun testDomainToEntity() {
        val domain = Game(
            1,
            "name",
            "https://test.com",
            5.0,
            "action, platformer",
            24,
            false,
            "1"
        )
        val entity = DataMapper.domainToEntity(domain)
        assertThat(entity.id).isEqualTo(domain.id)
        assertThat(entity.name).isEqualTo(domain.name)
        assertThat(entity.imageUrl).isEqualTo(domain.imageUrl)
        assertThat(entity.rating).isEqualTo(domain.rating)
        assertThat(entity.genres).isEqualTo(domain.genres)
        assertThat(entity.updated).isEqualTo(domain.updated)
        assertThat(entity.isFavorite).isTrue()
        assertThat(entity.platformId).isEqualTo(domain.platformId)
    }

    @Test
    fun testRecentSearchDomainToEntity() {
        val domain = RecentSearch("dota")
        val entity = DataMapper.domainToEntity(domain)
        assertThat(entity.query).isEqualTo(domain.query)
    }
}