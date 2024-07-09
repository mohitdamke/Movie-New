package com.example.mymovie.main.data.repository

import android.app.Application
import com.example.mymovie.main.data.local.genres.GenreDatabase
import com.example.mymovie.main.data.local.genres.GenreEntity
import com.example.mymovie.main.data.remote.api.GenresApi
import com.example.mymovie.main.domain.models.Genre
import com.example.mymovie.main.domain.repository.GenreRepository
import com.example.mymovie.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GenreRepositoryImpl @Inject constructor(
    private val genreApi: GenresApi,
    private val genreDatabase: GenreDatabase,
    private val application: Application
) : GenreRepository {

    private val genreDao = genreDatabase.genreDao

    override suspend fun getGenres(
        fetchFromRemote: Boolean,
        type: String
    ): Flow<Resource<List<Genre>>> {

        return flow {
            emit(Resource.Loading(true))

            val genreEntity = genreDao.getGenres(type)

            if (genreEntity.isNotEmpty() && !fetchFromRemote) {
                emit(Resource.Success(
                    genreEntity.map {
                        Genre(
                            id = it.id,
                            name = it.name,
                        )
                    }
                ))
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteGenreList = try {
                genreApi.getGenresList(type).genres
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
                return@flow
            }


            remoteGenreList.let {
                genreDao.insertGenres(remoteGenreList.map {
                    GenreEntity(
                        id = it.id,
                        name = it.name,
                        type = type
                    )
                })
            }
            emit(Resource.Success(remoteGenreList))
            emit(Resource.Loading(false))
        }

    }
}


























