package com.mobiiworld.mvvm.data.image.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.mobiiworld.mvvm.data.image.ImageRepo
import com.mobiiworld.mvvm.data.image.ImageRepoImpl
import com.mobiiworld.mvvm.data.image.source.ImageDataSource
import com.mobiiworld.mvvm.data.image.source.ImageDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton
import kotlin.contracts.ExperimentalContracts

@Module
@InstallIn(ApplicationComponent::class)
abstract class ImageModule {

    @ExperimentalContracts
    @Binds
    @Singleton
    abstract fun getImageDataSource(imageDataSourceImpl: ImageDataSourceImpl): ImageDataSource

    @Binds
    @ExperimentalContracts
    @Singleton
    abstract fun getImageRepo(repoImpl: ImageRepoImpl): ImageRepo
}