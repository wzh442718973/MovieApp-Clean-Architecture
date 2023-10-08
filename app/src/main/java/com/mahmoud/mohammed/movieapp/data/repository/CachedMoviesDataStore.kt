package com.mahmoud.mohammed.movieapp.data.repository

import android.graphics.pdf.PdfDocument.Page
import com.mahmoud.mohammed.movieapp.domain.MoviesCache
import com.mahmoud.mohammed.movieapp.domain.MoviesDataStore
import com.mahmoud.mohammed.movieapp.domain.entities.MovieEntity
import com.mahmoud.mohammed.movieapp.domain.entities.Optional
import io.reactivex.Observable

class CachedMoviesDataStore(private val moviesCache: MoviesCache): MoviesDataStore {


    override fun getMovieById(movieId: Int): Observable<Optional<MovieEntity>> {
        return moviesCache.get(movieId)
    }

    override fun getMovies(page: Int): Observable<List<MovieEntity>> {
        return moviesCache.getAll()
    }

}