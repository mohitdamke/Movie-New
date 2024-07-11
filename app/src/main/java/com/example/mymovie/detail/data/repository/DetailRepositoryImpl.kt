package com.example.mymovie.detail.data.repository

import com.example.mymovie.detail.data.dto.details.DetailsDto
import com.example.mymovie.detail.data.remote.ExtraDetailsApi
import com.example.mymovie.detail.domain.repository.DetailRepository
import com.example.mymovie.main.data.local.media.MediaDatabase
import com.example.mymovie.main.data.mapper.toMedia
import com.example.mymovie.main.domain.models.Media
import com.example.mymovie.util.Constants
import com.example.mymovie.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val extraDetailsApi: ExtraDetailsApi,
    mediaDb: MediaDatabase
) : DetailRepository {

    private val mediaDao = mediaDb.mediaDao


    override suspend fun getDetails(
        type: String,
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<Media>> {

        return flow {

            emit(Resource.Loading(true))

            val mediaEntity = mediaDao.getMediaById(id = id)

            val doDetailsExist = !(mediaEntity.status == null || mediaEntity.tagline == null)

            if (!isRefresh && doDetailsExist) {
                emit(
                    Resource.Success(
                        data = mediaEntity.toMedia(
                            type = mediaEntity.mediaType,
                            category = mediaEntity.category
                        )
                    )
                )

                emit(Resource.Loading(false))
                return@flow
            }

            val remoteDetails = fetchRemoteForDetails(
                type = mediaEntity.mediaType,
                id = id,
                apiKey = apiKey
            )

            if (remoteDetails == null) {
                emit(
                    Resource.Success(
                        data = mediaEntity.toMedia(
                            type = mediaEntity.mediaType,
                            category = mediaEntity.category
                        )
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

            remoteDetails.let { details ->

                mediaEntity.runtime = details.runtime
                mediaEntity.status = details.status
                mediaEntity.tagline = details.tagline

                mediaDao.updateMediaItem(mediaEntity)

                emit(
                    Resource.Success(
                        data = mediaEntity.toMedia(
                            type = mediaEntity.mediaType,
                            category = mediaEntity.category
                        )
                    )
                )

                emit(Resource.Loading(false))

            }


        }

    }

    private suspend fun fetchRemoteForDetails(
        type: String,
        id: Int,
        apiKey: String
    ): DetailsDto? {
        val remoteDetails = try {
            extraDetailsApi.getDetails(
                type = type,
                id = id,
                apiKey = apiKey
            )
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        }

        return remoteDetails

    }

}


