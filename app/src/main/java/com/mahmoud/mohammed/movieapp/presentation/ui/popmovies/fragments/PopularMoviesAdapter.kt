package com.mahmoud.mohammed.movieapp.presentation.ui.popmovies.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.mahmoud.mohammed.movieapp.R
import com.mahmoud.mohammed.movieapp.databinding.FragmentMovieListBinding
import com.mahmoud.mohammed.movieapp.databinding.PopularMoviesItemRowBinding
import com.mahmoud.mohammed.movieapp.presentation.entities.Movie

class PopularMoviesAdapter constructor(
    private val onMovieSelected:
        (Movie, View) -> Unit
) :
    RecyclerView.Adapter<PopularMoviesAdapter.MovieCellViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCellViewHolder {
        val binding = PopularMoviesItemRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieCellViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieCellViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie, onMovieSelected)
    }

    private val movies: MutableList<Movie> = mutableListOf()


    override fun getItemCount(): Int {
        return movies.size
    }


    fun addMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    class MovieCellViewHolder(binding: PopularMoviesItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title = binding.title
        val image = binding.image

        fun bind(movie: Movie, listener: (Movie, View) -> Unit) = with(itemView) {


            title.text = movie.originalTitle

            movie.posterPath?.let {
                image.load(it)
            }
            setOnClickListener { listener(movie, itemView) }
        }

    }
}