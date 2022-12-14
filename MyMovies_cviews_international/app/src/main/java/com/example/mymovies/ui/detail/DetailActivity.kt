package com.example.mymovies.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.bumptech.glide.Glide
import com.example.mymovies.R
import com.example.mymovies.databinding.ActivityDetailBinding
import com.example.mymovies.model.Movie


class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE = "DetailActivity:movie"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        if (movie != null){
            title = movie.title
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w780/${movie.backdrop_path}")
                .into(binding.backdrop)
            binding.summary.text = movie.overview
            bindDetailInfo(binding.detailInfo, movie)

        }
    }

    private fun bindDetailInfo(detailInfo: TextView, movie: Movie) {
        detailInfo.text = buildSpannedString {

            appendInf(R.string.original_language, movie.original_language)
            appendInf(R.string.original_title, movie.original_title)
            appendInf(R.string.release_date, movie.release_date)
            appendInf(R.string.popularity, movie.popularity.toString())
            appendInf(R.string.vote_average, movie.vote_average.toString())

        }
    }

    private fun SpannableStringBuilder.appendInf(stringRes: Int, value:String){
        bold {
            append(getString(stringRes))
            append(": ")
        }
        appendLine(value)
    }
}