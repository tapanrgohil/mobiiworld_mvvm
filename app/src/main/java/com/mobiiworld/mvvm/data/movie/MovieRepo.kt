package com.mobiiworld.mvvm.data.movie

import com.mobiiworld.mvvm.data.core.Resource
import com.mobiiworld.mvvm.ui.model.Movie
import com.mobiiworld.mvvm.ui.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepo {

    fun getMovies(query: String? = null, forceRefresh: Boolean = false): Flow<Resource<List<Movie>>>

    fun getMovieDetails(
        movieId: String,
        forceRefresh: Boolean = false
    ): Flow<Resource<MovieDetails>>

}