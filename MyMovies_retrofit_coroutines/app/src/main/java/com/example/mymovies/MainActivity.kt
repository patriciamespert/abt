package com.example.mymovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mymovies.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val moviesAdapter = MoviesAdapter(emptyList()) { movie ->
            Toast
                .makeText(this@MainActivity, movie.title, Toast.LENGTH_SHORT).show()
        }
        binding.recycler.adapter = moviesAdapter

        thread {
            //las peticiones no pueden bloquear el hilo principal
            val apiKey = getString(R.string.api_key)
            val popularMovies = MovieDbClient.service.listPopularMovies(apiKey)
            val body = popularMovies.execute().body()

            runOnUiThread{
                if(body != null)
                    moviesAdapter.movies = body.results
                    moviesAdapter.notifyDataSetChanged();e

            }



        }

    }
}