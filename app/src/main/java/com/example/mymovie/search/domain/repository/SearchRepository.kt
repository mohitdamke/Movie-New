package com.example.mymovie.search.domain.repository

import com.example.mymovie.main.domain.models.Media
import com.example.mymovie.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getSearchList(
        fetchFromRemote: Boolean,
        query: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>>

}










