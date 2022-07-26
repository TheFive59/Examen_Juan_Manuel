package jmmh.thefive.m513.view.ui.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import jmmh.thefive.m513.data.api.MovieDBClient
import jmmh.thefive.m513.data.api.MovieDBInterface
import jmmh.thefive.m513.data.repository.MoviePagedRepository
import jmmh.thefive.m513.data.repository.NetworkState
import jmmh.thefive.m513.data.repository.NowMovieRepository
import jmmh.thefive.m513.databinding.FragmentMoviesBinding
import jmmh.thefive.m513.view.adapter.NowMovieAdapter
import jmmh.thefive.m513.viewmodel.NowMovieModel
import jmmh.thefive.m513.view.adapter.PopularMovieAdapter
import jmmh.thefive.m513.viewmodel.PopularMovieModel
import kotlinx.android.synthetic.main.fragment_movies.*

class MoviesFragment : Fragment() {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPopularModel: PopularMovieModel
    private lateinit var viewModelNow: NowMovieModel
    private lateinit var movieRepository: MoviePagedRepository
    private lateinit var movieRepositoryNow: NowMovieRepository

    private lateinit var moviesAdapter: PopularMovieAdapter
    private lateinit var nowMovieAdapter: NowMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiService: MovieDBInterface = MovieDBClient.getClient()
        movieRepository = MoviePagedRepository(apiService)
        movieRepositoryNow = NowMovieRepository(apiService)
        viewPopularModel = getViewModel()
        viewModelNow = getViewModelNow()
        setRecyclerViews()
        setObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun setRecyclerViews() {

        val gridLayout = LinearLayoutManager(activity as Activity, LinearLayoutManager.HORIZONTAL, false)
        moviesAdapter = PopularMovieAdapter(activity as Activity)
        rv_movie_list.layoutManager = gridLayout
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = moviesAdapter

        val gridLayoutNow = LinearLayoutManager(activity as Activity, LinearLayoutManager.HORIZONTAL, false)
        nowMovieAdapter = NowMovieAdapter(activity as Activity)
        rv_now_movie_list.layoutManager = gridLayoutNow
        rv_now_movie_list.setHasFixedSize(true)
        rv_now_movie_list.adapter = nowMovieAdapter

    }

    private fun setObservers() {
        viewPopularModel.moviePagedList.observe(this, androidx.lifecycle.Observer {
            moviesAdapter.submitList(it)
        });
        viewPopularModel.networkState.observe(this, androidx.lifecycle.Observer {
            progress_bar_popular.visibility =
                if (viewPopularModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility =
                if (viewPopularModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            if (!viewPopularModel.listIsEmpty()) {
                moviesAdapter.setNetworkState(it)
            }
        })
        viewModelNow.moviePagedListNow.observe(this, androidx.lifecycle.Observer {
            nowMovieAdapter.submitList(it)
        });

        viewModelNow.networkState.observe(this, androidx.lifecycle.Observer {
            progress_bar_popular.visibility =
                if ( viewModelNow.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility =

                if ( viewModelNow.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (! viewModelNow.listIsEmpty()) {
                nowMovieAdapter.setNetworkState(it)
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
    // Get movies now playing model
    private fun getViewModelNow(): NowMovieModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NowMovieModel(movieRepositoryNow) as T
            }
        })[NowMovieModel::class.java]
    }
}