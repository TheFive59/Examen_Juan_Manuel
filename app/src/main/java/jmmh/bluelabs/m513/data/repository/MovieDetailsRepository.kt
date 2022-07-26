package jmmh.bluelabs.m513.data.repository

import androidx.lifecycle.LiveData
import io.reactivex.disposables.CompositeDisposable
import jmmh.bluelabs.m513.data.api.MovieDBInterface
import jmmh.bluelabs.m513.data.value_object.MovieDetails

class MovieDetailsRepository (private val apiService : MovieDBInterface){
    lateinit var movieDetailsNetworkDataSource: NetworkDetailsDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {

        movieDetailsNetworkDataSource = NetworkDetailsDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse

    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}