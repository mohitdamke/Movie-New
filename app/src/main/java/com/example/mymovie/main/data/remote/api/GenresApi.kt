package com.example.mymovie.main.data.remote.api

import com.example.mymovie.main.data.remote.api.MediaApi.Companion.API_KEY
import com.example.mymovie.main.data.remote.dto.GenresListDto
import com.example.mymovie.main.domain.models.Genre
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GenresApi {

    @GET("genre/{type}/list")
    suspend fun getGenresList(
        @Path("type") type: String,
        @Query("api_key") apiKey: String = API_KEY
    ): GenresListDto
}