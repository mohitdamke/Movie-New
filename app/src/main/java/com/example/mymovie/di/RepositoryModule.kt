package com.example.mymovie.di

import com.example.mymovie.detail.data.repository.DetailRepositoryImpl
import com.example.mymovie.detail.data.repository.ExtraDetailRepositoryImpl
import com.example.mymovie.detail.domain.repository.DetailRepository
import com.example.mymovie.detail.domain.repository.ExtraDetailRepository
import com.example.mymovie.main.data.repository.GenreRepositoryImpl
import com.example.mymovie.main.data.repository.MediaRepositoryImpl
import com.example.mymovie.main.domain.repository.GenreRepository
import com.example.mymovie.main.domain.repository.MediaRepository
import com.example.mymovie.search.data.repository.SearchRepositoryImpl
import com.example.mymovie.search.domain.repository.SearchRepository
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

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindDetailsRepository(
        detailsRepositoryImpl: DetailRepositoryImpl
    ): DetailRepository

    @Binds
    @Singleton
    abstract fun bindExtraDetailsRepository(
        extraDetailsRepositoryImpl: ExtraDetailRepositoryImpl
    ): ExtraDetailRepository

}