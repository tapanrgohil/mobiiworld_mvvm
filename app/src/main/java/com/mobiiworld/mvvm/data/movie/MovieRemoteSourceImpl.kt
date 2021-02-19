package com.mobiiworld.mvvm.data.movie

import com.mobiiworld.mvvm.data.movie.model.MovieDetailsRS
import com.mobiiworld.mvvm.data.movie.model.SearchRS
import retrofit2.Response
import javax.inject.Inject

class MovieRemoteSourceImpl @Inject constructor(
    private val apiService: MovieApiService
) : MovieRemoteSource {

    override suspend fun getMovies(query: String?): Response<SearchRS> =
        apiService.getMovies(query ?: "star")

    override suspend fun getMovieDetails(movieId: String): Response<MovieDetailsRS> =
        apiService.fetchMovieDetails(movieId)

}