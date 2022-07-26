package jmmh.bluelabs.m513.ui.popular_movie_details

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import jmmh.bluelabs.m513.R
import jmmh.bluelabs.m513.data.api.MovieDBClient
import jmmh.bluelabs.m513.data.api.MovieDBInterface
import jmmh.bluelabs.m513.data.repository.MoviePagedRepository
import jmmh.bluelabs.m513.data.repository.NetworkState
import jmmh.bluelabs.m513.data.repository.NowMovieRepository
import jmmh.bluelabs.m513.data.value_object.NowMovie
import jmmh.bluelabs.m513.databinding.ActivityPopularMovieBinding
import jmmh.bluelabs.m513.ui.now_playing_movie.NowMovieAdapter
import jmmh.bluelabs.m513.ui.now_playing_movie.NowMovieModel
import kotlinx.android.synthetic.main.activity_splash.*


class PopularMovieActivity : AppCompatActivity() {
    private lateinit var viewModel: PopularMovieModel
    private lateinit var viewModelNow: NowMovieModel
    private lateinit var movieRepository: MoviePagedRepository
    private lateinit var movieRepositoryNow: NowMovieRepository
    private lateinit var binding: ActivityPopularMovieBinding

    //private val filteredMoviesx: ArrayList<NowMovieRepository>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_movie)
        binding = ActivityPopularMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val apiService: MovieDBInterface = MovieDBClient.getClient()
        movieRepository = MoviePagedRepository(apiService)
        movieRepositoryNow = NowMovieRepository(apiService)
        viewModel = getViewModel()
        viewModelNow = getViewModelNow()
        val movieAdapter = PopularMovieAdapter(this)
        val nowmovieAdapter = NowMovieAdapter(this)


        //NowPlayingMovie
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1
                else return 3
            }
        }
        val gridNowMovie = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val gridLayout = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.rvMovieList.layoutManager = gridLayout
        binding.rvMovieList.setHasFixedSize(true)
        binding.rvMovieList.adapter = movieAdapter


        binding.rvNowMovieList.layoutManager = gridNowMovie
        binding.rvNowMovieList.setHasFixedSize(true)
        binding.rvNowMovieList.adapter = nowmovieAdapter
        val searchView: SearchView = findViewById(R.id.search_view) as SearchView

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        });
        viewModel.networkState.observe(this, Observer {
            binding.progressBarPopular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtErrorPopular.visibility =

                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
        viewModelNow.moviePagedListNow.observe(this, Observer {
            nowmovieAdapter.submitList(it)


        })
        viewModelNow.getUsers()
        viewModelNow.networkState.observe(this, Observer {
            binding.progressBarPopular.visibility =
                if (viewModelNow.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            binding.txtErrorPopular.visibility =
                if (viewModelNow.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModelNow.listIsEmpty()) {
                nowmovieAdapter.setNetworkState(it)
            }

        });

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                return false
            }
        })

    }

//Get popular movies model
    private fun getViewModel(): PopularMovieModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PopularMovieModel(movieRepository) as T
            }
        })[PopularMovieModel::class.java]
    }
//Get movies now playing model
    private fun getViewModelNow(): NowMovieModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NowMovieModel(movieRepositoryNow) as T
            }
        })[NowMovieModel::class.java]
    }
}