package com.mobiiworld.mvvm.data.movie

import com.mobiiworld.mvvm.data.movie.model.MovieDetailsRS
import com.mobiiworld.mvvm.data.movie.model.SearchRS
import com.mobiiworld.mvvm.ui.model.Movie
import com.mobiiworld.mvvm.ui.model.MovieDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieModelMapper @Inject constructor() {

    fun toListMovie(searchRS: SearchRS) = with(searchRS) {
        this.movies.orEmpty().map {
            with(it) {
                Movie(title, year, imdbID, type, poster)
            }
        }
    }

    fun mapUiModel(movieDetailsRS: MovieDetailsRS) = with(movieDetailsRS) {
        MovieDetails(
            year,
            title,
            rated,
            released,
            runtime,
            genre,
            director,
            writer,
            actors,
            plot,
            language,
            country,
            awards,
            poster,
            metascore,
            imdbRating,
            imdbVotes,
            imdbID,
            type,
            dVD,
            boxOffice,
            production,
            website,
            response
        )
    }


}