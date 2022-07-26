package jmmh.thefive.m513.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import jmmh.thefive.m513.data.repository.NetworkState
import jmmh.thefive.m513.model.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import jmmh.thefive.m513.data.repository.MovieDetailsRepository

class SingleMovieModel (private val movieRepository :
MovieDetailsRepository, movieId: Int)  : ViewModel()  {
    private val compositeDisposable = CompositeDisposable()

    val  movieDetails : LiveData<MovieDetails> by lazy {
        movieRepository.
        fetchSingleMovieDetails(compositeDisposable,movieId)
    }
    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

     override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}