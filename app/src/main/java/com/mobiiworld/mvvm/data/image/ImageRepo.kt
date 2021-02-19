package com.mobiiworld.mvvm.data.image

import com.mobiiworld.mvvm.data.core.Resource
import com.mobiiworld.mvvm.data.image.model.ImageNetworkModel
import com.mobiiworld.mvvm.data.image.source.ImageDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.contracts.ExperimentalContracts

interface ImageRepo {
    fun getImages(fromCache: Boolean = false): Flow<Resource<List<ImageNetworkModel>>>
}

@ExperimentalContracts
@Singleton
class ImageRepoImpl @Inject
constructor(private val imageDataSource: ImageDataSource) : ImageRepo {
    override fun getImages(fromCache: Boolean): Flow<Resource<List<ImageNetworkModel>>> {
        return imageDataSource.getImages(fromCache)
    }
}