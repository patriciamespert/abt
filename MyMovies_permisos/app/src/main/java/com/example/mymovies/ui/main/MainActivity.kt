package com.example.mymovies.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.mymovies.model.MovieDbClient
import com.example.mymovies.model.MoviesAdapter
import com.example.mymovies.R
import com.example.mymovies.databinding.ActivityMainBinding
import com.example.mymovies.model.Movie
import com.example.mymovies.ui.detail.DetailActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.jar.Manifest
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val moviesAdapter = MoviesAdapter(emptyList()) { navigateTo(it) }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        requestPopularMovies(isGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.recycler.adapter = moviesAdapter

        requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)

    }
    @SuppressLint("MissingPermission")
    private fun requestPopularMovies(isLocationGranted: Boolean) {

        if(isLocationGranted){
            fusedLocationProviderClient.lastLocation.addOnCompleteListener{
                doRequestPopularMovies(getRegionFromLocation(it.result))
            }
        } else {
            doRequestPopularMovies("US")
        }
    }

    private fun doRequestPopularMovies(region: String) {
        //las peticiones no pueden bloquear el hilo principal
        lifecycleScope.launch{
            val apiKey = getString(R.string.api_key)
            val popularMovies = MovieDbClient.service.listPopularMovies(apiKey, region)
            //!! lo que puesto yo porque no sabia como arreglarlo (no devexperto)
            moviesAdapter.movies = popularMovies.execute().body()!!.results
            moviesAdapter.notifyDataSetChanged()
        }
    }

    private fun getRegionFromLocation(location: Location?): String {

        if (location == null){
            return "US"
        }

        val geocoder = Geocoder(this)
        val result = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        )
        return result.firstOrNull()?.countryCode ?: "US"
    }

    private fun navigateTo(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie)
        startActivity(intent)
    }
}