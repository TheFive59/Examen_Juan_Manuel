package jmmh.thefive.m513.data.remote

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import jmmh.thefive.m513.data.api.MovieDBInterface
import jmmh.thefive.m513.data.local.MovieDatabase
import jmmh.thefive.m513.data.repository.NetworkState
import jmmh.thefive.m513.model.MovieDetails
import kotlinx.coroutines.launch

class NetworkDetailsDataSource(
    private val apiService:
    MovieDBInterface, private val compositeDisposable:
    CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState                   //with this get, no need to implement get function to get networkSate

    private val _downloadedMovieDetailsResponse =
        MutableLiveData<MovieDetails>()
    val downloadedMovieResponse: LiveData<MovieDetails>
        get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieId: Int) {

        _networkState.postValue(NetworkState.LOADING)


        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDataSource", it.message.toString())
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("MovieDetailsDataSource", e.message.toString())
        }
    }
}
