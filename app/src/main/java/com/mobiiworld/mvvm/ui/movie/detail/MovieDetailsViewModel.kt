package com.mobiiworld.mvvm.ui.movie.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiiworld.mvvm.data.core.Resource
import com.mobiiworld.mvvm.data.movie.MovieRepo
import com.mobiiworld.mvvm.ui.model.MovieDetails
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

class MovieDetailsViewModel @ViewModelInject constructor(
    private val movieRepo: MovieRepo
) : ViewModel() {

    private val _movieDetails = MutableLiveData<Resource<MovieDetails>>()
    val movieDetails: LiveData<Resource<MovieDetails>> = _movieDetails

    fun fetchMovieDetails(movieId: String, forceRefresh: Boolean = false) {
        movieRepo.getMovieDetails(movieId, forceRefresh)
            .map { _movieDetails.postValue(it) }
            .launchIn(viewModelScope)
    }

}