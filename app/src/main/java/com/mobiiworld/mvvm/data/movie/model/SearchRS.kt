package com.mobiiworld.mvvm.data.movie.model

import com.google.gson.annotations.SerializedName
import com.mobiiworld.mvvm.data.movie.model.MovieRS

data class SearchRS(
    @SerializedName("Search")
    val movies: List<MovieRS>?,
    @SerializedName("totalResults")
    val totalResults: String,
    @SerializedName("Response")
    val response: String
)