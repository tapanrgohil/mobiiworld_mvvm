package com.mobiiworld.mvvm.ui.movie.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiiworld.mvvm.data.core.Resource
import com.mobiiworld.mvvm.data.core.Status
import com.mobiiworld.mvvm.data.movie.MovieRepo
import com.mobiiworld.mvvm.ui.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

class MoviesViewModel @ViewModelInject constructor(
    private val movieRepo: MovieRepo
) : ViewModel() {

    private val cache = mutableListOf<Movie>()

    private var loadMoviesJob: Job? = null
    private var searchMoviesJob: Job? = null

    private val _movies = MutableLiveData<Resource<List<Movie>>>()
    val movies: LiveData<Resource<List<Movie>>> = _movies

    init {
        loadMovies()
    }

    fun loadMovies(forceRefresh: Boolean = false) {
        searchMoviesJob?.cancel()
        loadMoviesJob = movieRepo.getMovies("star", forceRefresh)
            .map {
                _movies.postValue(it)
                if (it.status == Status.SUCCESS) {
                    cache.clear()
                    cache.addAll(it.data.orEmpty())
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private fun searchMovies(query: String) {
        loadMoviesJob?.cancel()
        searchMoviesJob = movieRepo.getMovies(query, true)
            .map {
                _movies.postValue(it)
                if (it.status == Status.SUCCESS) {
                    cache.clear()
                    cache.addAll(it.data.orEmpty())
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun filter(query: String?) {
        val filteredMovies = if (query == null) cache else cache.filter {
            it.title.contains(query, true)
        }
        _movies.postValue(Resource.success(filteredMovies))
        if (filteredMovies.isEmpty() && query != null) {
            searchMovies(query)
        }
    }

}