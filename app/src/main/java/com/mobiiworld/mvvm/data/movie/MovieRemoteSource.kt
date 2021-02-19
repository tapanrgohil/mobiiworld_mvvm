package com.mobiiworld.mvvm.data.movie

import com.mobiiworld.mvvm.data.movie.model.MovieDetailsRS
import com.mobiiworld.mvvm.data.movie.model.SearchRS
import retrofit2.Response

interface MovieRemoteSource {

    suspend fun getMovies(query: String? = null): Response<SearchRS>

    suspend fun getMovieDetails(movieId: String): Response<MovieDetailsRS>

}