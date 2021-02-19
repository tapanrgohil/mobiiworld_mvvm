package com.mobiiworld.mvvm.data.movie

import com.mobiiworld.mvvm.data.movie.model.MovieDetailsRS
import com.mobiiworld.mvvm.data.movie.model.SearchRS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET("/")
    suspend fun getMovies(@Query("s") query: String): Response<SearchRS>

    @GET("/")
    suspend fun fetchMovieDetails(@Query("i") movieId: String): Response<MovieDetailsRS>

}