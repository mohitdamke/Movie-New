package com.example.mymovie.main.data.local.genres

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymovie.main.data.local.media.MediaEntity


@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(
        mediaEntity: List<GenreEntity>
    )

    @Query("SELECT * FROM genres WHERE type = :mediaType")
    suspend fun getGenres(mediaType : String): List<GenreEntity>
}