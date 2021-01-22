package com.example.madesubmission.core.data.source

import com.example.madesubmission.core.data.Resource
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {
    companion object {
        const val GOOD = 0
        const val EMPTY = 1
        const val EXPIRED = 2
    }

    private val result: Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        val dbSource = loadFromDb().first()
        val result = shouldFetch(dbSource)
        if (result != GOOD) {
            when (val response = createCall().first()) {
                is Resource.Success -> {
                    saveCallResult(response.data)
                    emitAll(loadFromDb().map {
                        Resource.Success(it)
                    })
                }
                is Resource.Error -> {
                    if (result == EXPIRED) {
                        emitAll(loadFromDb().map {
                            Resource.Success(it)
                        })
                    } else {
                        onFetchFailed()
                        emit(Resource.Error<ResultType>(response.message))
                    }
                }
            }
        } else {
            emitAll(loadFromDb().map {
                Resource.Success(it)
            })
        }
    }

    protected open fun onFetchFailed() {}
    protected abstract fun isExpired(data: ResultType): Int
    protected abstract fun loadFromDb(): Flow<ResultType>
    protected abstract fun shouldFetch(data: ResultType): Int
    protected abstract suspend fun createCall(): Flow<Resource<RequestType>>
    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<Resource<ResultType>> = result
}