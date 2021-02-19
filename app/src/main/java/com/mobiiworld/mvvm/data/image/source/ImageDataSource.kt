package com.mobiiworld.mvvm.data.image.source

import com.mobiiworld.mvvm.data.core.Resource
import com.mobiiworld.mvvm.data.image.model.ImageNetworkModel
import kotlinx.coroutines.flow.Flow

//This can be local and remote sources
interface ImageDataSource {

    fun getImages(fromCache:Boolean): Flow<Resource<List<ImageNetworkModel>>>
}