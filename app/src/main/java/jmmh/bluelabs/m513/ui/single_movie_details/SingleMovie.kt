package jmmh.bluelabs.m513.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import jmmh.bluelabs.m513.R
import jmmh.bluelabs.m513.data.api.MovieDBClient
import jmmh.bluelabs.m513.data.api.MovieDBInterface
import jmmh.bluelabs.m513.data.api.POSTER_BASE_URL
import jmmh.bluelabs.m513.data.repository.MovieDetailsRepository
import jmmh.bluelabs.m513.data.repository.NetworkState
import jmmh.bluelabs.m513.data.value_object.MovieDetails
import java.text.NumberFormat
import java.util.*
import jmmh.bluelabs.m513.databinding.ActivitySingleMovieBinding
class SingleMovie : AppCompatActivity() {
    private lateinit var viewModel: SingleMovieModel
    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var binding: ActivitySingleMovieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)
        binding = ActivitySingleMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
supportActionBar?.hide()
        val movieId: Int = intent.getIntExtra("id",1)

        val apiService : MovieDBInterface = MovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })
        viewModel.networkState.observe(this, Observer {
            binding.progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })
    }
    fun bindUI( it: MovieDetails){
        //title=it.title
        binding.movieTitle.text = it.title
        binding.movieTagline.text = it.tagline
        binding.movieReleaseDate.text = it.releaseDate
        binding.movieRating.text = it.rating.toString()
        binding.movieRuntime.text = it.runtime.toString() + " minutes"
        binding.movieOverview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        //binding.movieBudget.text = formatCurrency.format(it.budget)
        //binding.movieRevenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath




        Glide.with(this)
            .load(moviePosterURL)
            .into(binding.ivMoviePoster);

    }
    private fun getViewModel(movieId: Int):SingleMovieModel{
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SingleMovieModel(movieRepository,movieId)as T
            }
        })[SingleMovieModel::class.java]
    }

}