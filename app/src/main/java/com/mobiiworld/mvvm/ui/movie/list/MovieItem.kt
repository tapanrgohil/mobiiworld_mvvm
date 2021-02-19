package com.mobiiworld.mvvm.ui.movie.list

import android.view.View
import androidx.core.view.ViewCompat
import coil.load
import coil.transform.CircleCropTransformation
import com.mobiiworld.mvvm.R
import com.mobiiworld.mvvm.ui.model.Movie
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_movie.*

class MovieItem(
    private val movie: Movie,
    private val onMovieClicked: (Movie, View) -> Unit
) : Item() {

    override fun getId() = movie.imdbID.hashCode().toLong()

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.run {
            ViewCompat.setTransitionName(ivThumb, "thumb_${movie.imdbID}")
            ivThumb.load(movie.poster) {
                placeholder(R.mipmap.ic_launcher_round)
                error(R.mipmap.ic_launcher_round)
                crossfade(true)
                transformations(CircleCropTransformation())
            }
            tvTitle.text = movie.title
            tvDescription.text = movie.type

            itemView.setOnClickListener { onMovieClicked.invoke(movie, ivThumb) }
        }
    }

    override fun getLayout() = R.layout.item_movie

}