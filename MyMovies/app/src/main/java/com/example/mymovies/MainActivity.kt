package com.example.mymovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mymovies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.adapter = MoviesAdapter(
            listOf(
                Movie("title 1", "https://loremflickr.com/320/240?lock=1"),
                Movie("title 2", "https://loremflickr.com/320/240?lock=2"),
                Movie("title 3", "https://loremflickr.com/320/240?lock=3"),
                Movie("title 4", "https://loremflickr.com/320/240?lock=4"),
                Movie("title 5", "https://loremflickr.com/320/240?lock=5"),
                Movie("title 6", "https://loremflickr.com/320/240?lock=6")
            )
        ) {
            Toast
                .makeText(this@MainActivity, it.title, Toast.LENGTH_SHORT).show()
        }

    }
}