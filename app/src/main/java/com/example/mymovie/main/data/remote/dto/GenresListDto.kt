package com.example.mymovie.main.data.remote.dto

import com.example.mymovie.main.domain.models.Genre

data class GenresListDto(
    val genres: List<Genre>
)