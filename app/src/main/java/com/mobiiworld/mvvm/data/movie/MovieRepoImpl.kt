package com.mobiiworld.mvvm.data.movie

import com.mobiiworld.mvvm.data.core.Resource
import com.mobiiworld.mvvm.data.core.getRemoteFlow
import com.mobiiworld.mvvm.ui.model.Movie
import com.mobiiworld.mvvm.ui.model.MovieDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
@Singleton
class MovieRepoImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val mapper: MovieModelMapper
) : MovieRepo {

    override fun getMovies(
        query: String?, forceRefresh: Boolean
    ): Flow<Resource<List<Movie>>> {
        return getRemoteFlow(
            remote = { apiService.getMovies(query.orEmpty()) },
            mapper = { mapper.toListMovie(it) }
        )
    }

    override fun getMovieDetails(
        movieId: String, forceRefresh: Boolean
    ): Flow<Resource<MovieDetails>> {


        return getRemoteFlow(
            remote = { apiService.fetchMovieDetails(movieId) },
            mapper = { mapper.mapUiModel(it) }
        )

    }

}