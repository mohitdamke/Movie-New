package com.example.mymovie.detail.presentation

import com.example.mymovie.main.domain.models.Genre

sealed class MediaDetailsScreenEvents {

    data class SetDataAndLoad(
        val moviesGenresList: List<Genre>,
        val tvGenresList: List<Genre>,
        val id: Int,
        val type: String,
        val category: String
    ) : MediaDetailsScreenEvents()

    object Refresh : MediaDetailsScreenEvents()

    object NavigateToWatchVideo : MediaDetailsScreenEvents()
}