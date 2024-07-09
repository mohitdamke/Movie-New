package com.example.mymovie.main.data.repository

import androidx.compose.ui.node.TraversableNode
import com.example.mymovie.main.data.local.media.MediaDatabase
import com.example.mymovie.main.data.mapper.toMedia
import com.example.mymovie.main.data.mapper.toMediaEntity
import com.example.mymovie.main.data.remote.api.MediaApi
import com.example.mymovie.main.domain.models.Media
import com.example.mymovie.main.domain.repository.MediaRepository
import com.example.mymovie.util.Constants.TRENDING
import com.example.mymovie.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val mediaApi: MediaApi,
    private val mediaDatabase: MediaDatabase
) : MediaRepository {

    private val mediaDao = mediaDatabase.mediaDao


    override suspend fun updateItem(media: Media) {
        val mediaEntity = media.toMediaEntity()
        mediaDao.updateMediaItem(mediaItem = mediaEntity)
    }

    override suspend fun insertItem(media: Media) {
        val mediaEntity = media.toMediaEntity()
        mediaDao.insertMediaItem(mediaItem = mediaEntity)
    }

    override suspend fun getItem(id: Int, type: String, category: String): Media {
        return mediaDao.getMediaById(id).toMedia(
            type = type,
            category = category
        )

    }

    override suspend fun getMoviesAndTvSeriesList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        category: String,
        page: Int
    ): Flow<Resource<List<Media>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMediaList =
                mediaDao.getMediaListByTypeAndCategory(mediaType = type, category = category)

            val shouldJustLoadFromCatch =
                localMediaList.isNotEmpty() && !fetchFromRemote && !isRefresh

            if (shouldJustLoadFromCatch) {
                emit(Resource.Success(localMediaList.map {
                    it.toMedia(type, category)
                }))
                emit(Resource.Loading(false))
                return@flow
            }

            var searchPage = page
            if (isRefresh) {
                mediaDao.deleteMediaListByTypeAndCategory(type, category)
                searchPage = 1
            }

            val remoteMediaList =
                try {
                    mediaApi.getMoviesAndTvSeriesList(type, category, searchPage).results
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

            remoteMediaList.let { mediaList ->
                val media = mediaList.map {
                    it.toMedia(
                        type = type,
                        category = category
                    )
                }
                val entity = mediaList.map {
                    it.toMediaEntity(
                        type = type,
                        category = category
                    )
                }

                mediaDao.insertMediaList(entity)

                emit(Resource.Success(data = media))

                emit(Resource.Loading(false))

            }
        }
    }


    override suspend fun getTrendingList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        time: String,
        page: Int
    ): Flow<Resource<List<Media>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMediaList =
                mediaDao.getTrendingMediaList(category = TRENDING)

            val shouldJustLoadFromCatch =
                localMediaList.isNotEmpty() && !fetchFromRemote && !isRefresh

            if (shouldJustLoadFromCatch) {
                emit(Resource.Success(localMediaList.map {
                    it.toMedia(type = it.mediaType, category = TRENDING)
                }))
                emit(Resource.Loading(false))
                return@flow
            }

            var searchPage = page
            if (isRefresh) {
                mediaDao.deleteTrendingMediaList(category = TRENDING)
                searchPage = 1
            }

            val remoteMediaList =
                try {
                    mediaApi.getTrendingList(type, time, searchPage).results
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

            remoteMediaList.let { mediaList ->
                val media = mediaList.map {
                    it.toMedia(
                        type = it.media_type ?: "",
                        category = TRENDING
                    )
                }
                val entity = mediaList.map {
                    it.toMediaEntity(
                        type = it.media_type ?: "",
                        category = TRENDING
                    )
                }

                mediaDao.insertMediaList(entity)

                emit(Resource.Success(data = media))

                emit(Resource.Loading(false))

            }
        }
    }


}

