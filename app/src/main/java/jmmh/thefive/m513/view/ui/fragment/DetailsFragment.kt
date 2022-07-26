package jmmh.thefive.m513.view.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import jmmh.thefive.m513.data.utils.Constants
import jmmh.thefive.m513.data.api.MovieDBClient
import jmmh.thefive.m513.data.api.MovieDBInterface
import jmmh.thefive.m513.data.repository.MovieDetailsRepository
import jmmh.thefive.m513.data.repository.NetworkState
import jmmh.thefive.m513.model.MovieDetails
import jmmh.thefive.m513.databinding.FragmentDetailsBinding
import jmmh.thefive.m513.viewmodel.SingleMovieModel
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private lateinit var viewModel: SingleMovieModel
    private lateinit var movieRepository: MovieDetailsRepository
    private val binding get() = _binding!!
    var movieId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            movieId=arguments!!.getInt("movieId")
        }

        val apiService: MovieDBInterface = MovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)
        viewModel.movieDetails.observe(this, androidx.lifecycle.Observer {
            bindUI(it)
        })
         viewModel.networkState.observe(this, androidx.lifecycle.Observer {
             progress_bar_popular.visibility = if (it == NetworkState.LOADING)
                 View.VISIBLE else View.GONE
             txt_error_popular.visibility = if (it == NetworkState.ERROR)
                 View.VISIBLE else View.GONE
         })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_details, container, false)
        return binding.root

    }

    private fun bindUI(it: MovieDetails?) {
        lblMovieTitle.text = it?.title.toString()
        lbl_tagline_movie.text = it?.tagline.toString()
        textReleaseDate.text = it?.releaseDate
        textRating.text = it?.rating.toString()
        textRuntime.text = it?.duracion.toString() + " minutes"
        textMovieOverview.text = it?.overview
        //detailRank.rating = (movie.vote_average / 2)
        var ratingG=it?.rating
        if (ratingG != null) {
            detail_rank.rating =((ratingG / 2).toFloat())
        }
        val moviePosterURL = Constants.POSTER_BASE_URL + it?.posterPath
        val movieBackDropURL = Constants.POSTER_BASE_URL + it?.backdrop_path

        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster)
        Glide.with(this)
            .load(movieBackDropURL)
            .into(movie_avatar)
    }
    private fun getViewModel(movieId: Int): SingleMovieModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SingleMovieModel(movieRepository, movieId) as T
            }
        })[SingleMovieModel::class.java]
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieId=arguments!!.getInt("movieId")
    }
}