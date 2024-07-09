package com.example.mymovie.main.data.local.genres

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymovie.main.domain.models.Genre

@Database(entities = [GenreEntity::class], version = 1)
abstract class GenreDatabase : RoomDatabase() {
    abstract val genreDao: GenreDao
}