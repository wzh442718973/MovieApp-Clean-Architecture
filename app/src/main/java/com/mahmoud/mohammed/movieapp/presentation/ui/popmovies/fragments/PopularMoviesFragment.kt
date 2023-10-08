package com.mahmoud.mohammed.movieapp.presentation.ui.popmovies.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.mohammed.movieapp.MovieApplication
import com.mahmoud.mohammed.movieapp.R
import com.mahmoud.mohammed.movieapp.base.BaseFragment
import com.mahmoud.mohammed.movieapp.databinding.FragmentMovieListBinding
import javax.inject.Inject

fun newMovieListFragment() = MovieListFragment()
val MOVIE_LIST_FRAGMENT_TAG = MovieListFragment::class.java.name

class MovieListFragment : BaseFragment() {

    @Inject
    lateinit var factory: PopularMoviesVMFactory
    private lateinit var viewModel: PopularMoviesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var popularMoviesAdapter: PopularMoviesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as MovieApplication).createPopularComponenet().inject(this)
        initViewModel()
        if (savedInstanceState == null) {
            viewModel.getPopularMovies(page)
        }

    }

    lateinit var binding: FragmentMovieListBinding;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieListBinding.inflate(layoutInflater, container, false);
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.viewState.observe(this, Observer {
            if (it != null) {
                page += 1
                handleViewState(it)
            }
        })
        viewModel.errorState.observe(this, Observer { throwable ->
            throwable?.let {
                Toast.makeText(activity, throwable.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun handleViewState(state: PopularMoviesViewState) {
        progressBar.visibility = if (state.showLoading) View.VISIBLE else View.GONE
        state.movies?.let { popularMoviesAdapter.addMovies(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.popularMoviesProgress
        popularMoviesAdapter = PopularMoviesAdapter { movie, view ->

            navigateToMovieDetailsScreen(movie)
            /*
                        val i = Intent(context, MovieDetailsActivity::class.java)
                        i.putExtra(MovieDetailsActivity.MOVIE_ID, movie.id)
                        startActivity(i);*/

        }
        recyclerView = binding.popularMoviesRecyclerview
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.adapter = popularMoviesAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isBottom(recyclerView)) {
                    Log.e("wzh", "isBottom");
                    viewModel.getPopularMovies(page + 1)
                }
            }
        })
    }

    fun isBottom(recyclerView: RecyclerView): Boolean {
        if (recyclerView != null &&
            recyclerView.computeVerticalScrollExtent() +
            recyclerView.computeVerticalScrollOffset() >=
            recyclerView.computeVerticalScrollRange()
        ) {
            return true;
        }
        return false;
    }


    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(PopularMoviesViewModel::class.java)

    }

    override fun onDestroy() {
        super.onDestroy()
        (activity?.application as MovieApplication).releasePopularComponent()
    }


    var page: Int = 1
}
