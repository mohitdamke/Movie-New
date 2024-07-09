package com.example.mymovie.di

import com.example.mymovie.main.data.repository.GenreRepositoryImpl
import com.example.mymovie.main.data.repository.MediaRepositoryImpl
import com.example.mymovie.main.domain.repository.GenreRepository
import com.example.mymovie.main.domain.repository.MediaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMediaRepository(mediaRepositoryImpl: MediaRepositoryImpl): MediaRepository

    @Binds
    @Singleton
    abstract fun bindGenreRepository(genreRepositoryImpl: GenreRepositoryImpl): GenreRepository


}