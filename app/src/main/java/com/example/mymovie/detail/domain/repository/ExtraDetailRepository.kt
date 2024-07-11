package com.example.mymovie.detail.domain.repository

import com.example.mymovie.main.domain.models.Media
import com.example.mymovie.util.Resource
import kotlinx.coroutines.flow.Flow

interface ExtraDetailRepository {
    suspend fun getSimilarMediaList(
        isRefresh: Boolean,
        type: String,
        id: Int,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>>

    suspend fun getVideosList(
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<List<String>>>

}