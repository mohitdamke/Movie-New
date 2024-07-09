package com.example.mymovie.main.domain.repository

import com.example.mymovie.main.domain.models.Genre
import com.example.mymovie.util.Resource
import kotlinx.coroutines.flow.Flow

interface GenreRepository {
    suspend fun getGenres(
        fetchFromRemote: Boolean,
        type: String
    ): Flow<Resource<List<Genre>>>
}