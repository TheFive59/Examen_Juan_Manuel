package jmmh.bluelabs.m513.single_movie_details
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import jmmh.bluelabs.m513.data.repository.NetworkState
import jmmh.bluelabs.m513.data.value_object.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import jmmh.bluelabs.m513.data.repository.MovieDetailsRepository

class SingleMovieModel (private val movieRepository : MovieDetailsRepository, movieId: Int)  : ViewModel()  {
    private val compositeDisposable = CompositeDisposable()

    val  movieDetails : LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable,movieId)
    }
    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

     override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}