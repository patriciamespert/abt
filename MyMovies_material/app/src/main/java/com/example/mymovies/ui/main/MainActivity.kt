package com.example.mymovies.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mymovies.model.MovieDbClient
import com.example.mymovies.model.MoviesAdapter
import com.example.mymovies.R
import com.example.mymovies.databinding.ActivityMainBinding
import com.example.mymovies.model.Movie
import com.example.mymovies.ui.detail.DetailActivity
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val moviesAdapter = MoviesAdapter(emptyList()) { navigateTo(it) }
        binding.recycler.adapter = moviesAdapter

        thread {
            //las peticiones no pueden bloquear el hilo principal
            val apiKey = getString(R.string.api_key)
            val popularMovies = MovieDbClient.service.listPopularMovies(apiKey)
            val body = popularMovies.execute().body()

            runOnUiThread{
                if(body != null)
                    moviesAdapter.movies = body.results
                    moviesAdapter.notifyDataSetChanged()

            }



        }

    }

    private fun navigateTo(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie)
        startActivity(intent)
    }
}