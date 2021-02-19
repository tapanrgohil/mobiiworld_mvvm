package com.mobiiworld.mvvm.ui.movie.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.mobiiworld.mvvm.R
import com.mobiiworld.mvvm.data.core.Status
import com.mobiiworld.mvvm.exception.SnackbarManager.handleErrorResponse
import com.mobiiworld.mvvm.ui.model.MovieDetails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_detail.*

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private val viewModel by viewModels<MovieDetailsViewModel>()

    private val args by navArgs<MovieDetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.title?.let {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it
        }
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        viewModel.fetchMovieDetails(args.movieId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        ViewCompat.setTransitionName(ivThumb, "thumb_${args.movieId}")
        attachMovieDetailsObserver()
    }

    private fun attachMovieDetailsObserver() {
        viewModel.movieDetails.observe(viewLifecycleOwner, {
            when (it?.status) {
                Status.LOADING -> {
                    pbLoading.isVisible = true
                }
                Status.SUCCESS -> {
                    pbLoading.isVisible = false
                    it.data?.let { movieDetails -> updateUI(movieDetails) }
                }
                Status.ERROR -> {
                    pbLoading.isVisible = false
                    handleErrorResponse(it.retrofitResponse, it.throwable) {
                        viewModel.fetchMovieDetails(args.movieId, true)
                    }
                }
            }
        })
    }

    private fun updateUI(details: MovieDetails) {
        ivThumb.load(details.poster) {
            placeholder(R.mipmap.ic_launcher_round)
            error(R.mipmap.ic_launcher_round)
            crossfade(true)
        }
        tvTitle.text = details.title
        tvDescription.text = details.plot
    }

}