package com.mobiiworld.mvvm.ui.movie.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.mobiiworld.mvvm.R
import com.mobiiworld.mvvm.data.core.Status
import com.mobiiworld.mvvm.exception.SnackbarManager.handleErrorResponse
import com.mobiiworld.mvvm.ui.model.Movie
import com.mobiiworld.mvvm.util.handleResponse
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private val viewModel by viewModels<MoviesViewModel>()

    private val moviesAdapter = GroupAdapter<GroupieViewHolder>()
    private val moviesSection = Section()

    private var searchView: SearchView? = null

    private var queryTextChangedJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviesAdapter.add(moviesSection)
        setHasOptionsMenu(true)

        val dispatcher = requireActivity().onBackPressedDispatcher
        dispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val dialog = MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT))
                dialog.show {
                    title(text = "Are you sure?")
                    message(text = "Are you sure you want to exit?")
                    positiveButton(text = "Yes") { dialog ->
                        isEnabled = false
                        dialog.dismiss()
                        requireActivity().onBackPressed()
                    }
                    negativeButton(text = "No") { dialog ->
                        dialog.dismiss()
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_movies_list, menu)

        searchView = menu.findItem(R.id.menu_search).actionView as SearchView
        searchView?.apply {
            maxWidth = 2000
            queryHint = getString(R.string.search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    queryTextChangedJob?.cancel()
                    queryTextChangedJob = lifecycleScope.launch(Dispatchers.Main) {
                        delay(500)
                        viewModel.filter(newText)
                    }
                    return true
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onDestroyView() {
        searchView = null
        super.onDestroyView()
    }

    private fun init() {
        setupViews()
        attachMoviesObserver()
    }

    private fun setupViews() {
        srMoviesList.setOnRefreshListener {
            searchView?.onActionViewCollapsed()
            viewModel.loadMovies(true)
        }
        rvMovies.apply {
            adapter = moviesAdapter
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun attachMoviesObserver() {
        viewModel.movies.observe(viewLifecycleOwner, {
            when (it?.status) {
                Status.LOADING -> {
                    pbLoading.isVisible = true
                }
                Status.SUCCESS -> {
                    srMoviesList.isRefreshing = false
                    pbLoading.isVisible = false
                    updateMoviesUI(it.data.orEmpty())
                }
                Status.ERROR -> {
                    srMoviesList.isRefreshing = false
                    pbLoading.isVisible = false
                    handleErrorResponse(it.retrofitResponse, it.throwable) {
                        viewModel.loadMovies(true)
                    }
                }
            }
        })
    }

    private fun updateMoviesUI(movies: List<Movie>) {
        moviesSection.setPlaceholder(EmptyItem())
        moviesSection.update(movies.map { MovieItem(it, ::onMovieSelected) })
    }

    private fun onMovieSelected(movie: Movie, thumb: View) {
        val extras = FragmentNavigatorExtras(
            thumb to "thumb_${movie.imdbID}"
        )
        val direction = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailFragment(
            movie.imdbID,
            movie.title
        )
        findNavController().navigate(direction, extras)
    }

}