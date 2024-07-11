package com.example.mymovie.detail.domain.repository

import com.example.mymovie.main.domain.models.Media
import com.example.mymovie.util.Resource
import kotlinx.coroutines.flow.Flow

interface DetailRepository {

    suspend fun getDetails(
        type: String,
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<Media>>
}