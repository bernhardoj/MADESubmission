package com.example.madesubmission.core.data.paging

import androidx.paging.PagingSource
import com.example.madesubmission.core.data.source.remote.RemoteDataSource
import com.example.madesubmission.core.domain.model.Game
import com.example.madesubmission.core.utils.DataMapper
import java.lang.Exception

private const val START_INDEX = 1

class GamesPagingSource(private val query: String, private val dataSource: RemoteDataSource) : PagingSource<Int, Game>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        return try {
            val page = params.key ?: START_INDEX

            val response = dataSource.searchGames(query, page)
            val entity = response.games.map {
                DataMapper.responsesToEntity(it, "")
            }
            val domain = entity.map {
                DataMapper.entityToDomain(it)
            }

            val nextKey = if (response.nextPage == null) null else page + 1

            LoadResult.Page(
                data = domain,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}