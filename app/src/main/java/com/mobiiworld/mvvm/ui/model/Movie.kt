package com.mobiiworld.mvvm.ui.model

import org.threeten.bp.ZonedDateTime

data class Movie(
    val title: String,
    val year: String,
    val imdbID: String,
    val type: String,
    val poster: String)