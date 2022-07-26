package jmmh.thefive.m513.data.repository

import androidx.lifecycle.LiveData
import androidx.room.Room
import io.reactivex.disposables.CompositeDisposable
import jmmh.thefive.m513.data.api.MovieDBInterface
import jmmh.thefive.m513.data.local.MovieDatabase
import jmmh.thefive.m513.data.local.dao.MovieDetailsDao
import jmmh.thefive.m513.data.remote.NetworkDetailsDataSource
import jmmh.thefive.m513.model.MovieDetails
import kotlinx.coroutines.launch

class MovieDetailsRepository(private val apiService: MovieDBInterface) {
    lateinit var movieDetailsNetworkDataSource: NetworkDetailsDataSource
    fun fetchSingleMovieDetails(
        compositeDisposable:
        CompositeDisposable, movieId: Int
    ): LiveData<MovieDetails> {

        movieDetailsNetworkDataSource =
            NetworkDetailsDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse

    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}