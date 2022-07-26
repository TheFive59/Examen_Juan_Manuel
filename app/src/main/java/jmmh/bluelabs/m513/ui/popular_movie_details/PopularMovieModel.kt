package jmmh.bluelabs.m513.ui.popular_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import jmmh.bluelabs.m513.data.repository.MoviePagedRepository
import jmmh.bluelabs.m513.data.repository.NetworkState
import jmmh.bluelabs.m513.data.value_object.Movie

class PopularMovieModel(private val movieRepository: MoviePagedRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMovieList(compositeDisposable)
    }
    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}