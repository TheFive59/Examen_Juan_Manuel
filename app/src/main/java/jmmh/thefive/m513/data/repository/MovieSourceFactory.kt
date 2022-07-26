package jmmh.thefive.m513.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import jmmh.thefive.m513.data.api.MovieDBInterface
import jmmh.thefive.m513.data.remote.MovieDataSource
import jmmh.thefive.m513.model.Movie

class MovieSourceFactory(
    private val apiService: MovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Movie>() {
    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()
    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }

}