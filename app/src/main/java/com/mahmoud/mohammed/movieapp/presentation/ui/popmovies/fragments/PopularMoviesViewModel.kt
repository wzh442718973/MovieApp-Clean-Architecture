package com.mahmoud.mohammed.movieapp.presentation.ui.popmovies.fragments

import androidx.lifecycle.MutableLiveData
import com.mahmoud.mohammed.movieapp.base.BaseViewModel
import com.mahmoud.mohammed.movieapp.common.SingleLiveEvent
import com.mahmoud.mohammed.movieapp.domain.Mapper
import com.mahmoud.mohammed.movieapp.domain.entities.MovieEntity
import com.mahmoud.mohammed.movieapp.domain.usecases.GetPopularMovies
import com.mahmoud.mohammed.movieapp.presentation.entities.Movie
import javax.inject.Inject

class PopularMoviesViewModel
@Inject constructor(
        private val getPopularMovies: GetPopularMovies,
        private val movieEntityMovieMapper: Mapper<MovieEntity, Movie>) : BaseViewModel() {
    var viewState: MutableLiveData<PopularMoviesViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()
    init {
        viewState.value = PopularMoviesViewState()
    }

    fun getPopularMovies(page:Int) {
        addDisposable(getPopularMovies.observable(page)
                .flatMap { movieEntityMovieMapper.observable(it) }
                .subscribe({ movies ->
                    viewState.value?.let {
                        val newState = this.viewState.value?.copy(showLoading = false, movies = movies)
                        this.viewState.value = newState
                        this.errorState.value = null
                    }

                }, {
                    viewState.value = viewState.value?.copy(showLoading = false)
                    errorState.value = it
                }))
    }

}