package com.mobiiworld.mvvm.data.movie.di

import com.mobiiworld.mvvm.data.movie.MovieApiService
import com.mobiiworld.mvvm.data.movie.MovieRepo
import com.mobiiworld.mvvm.data.movie.MovieRepoImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.contracts.ExperimentalContracts

@Module(includes = [MovieSources::class])
@InstallIn(ApplicationComponent::class)
object MovieModule {

    @Provides
    @Singleton
    fun provideMovieApiService(retrofit: Retrofit): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }


}


@Module()
@InstallIn(ApplicationComponent::class)
abstract class MovieSources {
    @ExperimentalContracts
    @Binds
    @Singleton
    abstract fun provideMovieRepo(impl: MovieRepoImpl): MovieRepo

}
