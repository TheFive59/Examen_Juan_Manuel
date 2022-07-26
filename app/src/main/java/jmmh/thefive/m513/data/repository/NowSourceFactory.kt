package jmmh.thefive.m513.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import jmmh.thefive.m513.data.api.MovieDBInterface
import jmmh.thefive.m513.data.remote.NowMovieSource
import jmmh.thefive.m513.model.NowMovie

class NowSourceFactory (
    private val apiService: MovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, NowMovie>() {
    val moviesLiveDataSource = MutableLiveData<NowMovieSource>()
    override fun create(): DataSource<Int, NowMovie> {
        val nowmovieDataSource = NowMovieSource(apiService, compositeDisposable)

        moviesLiveDataSource.postValue(nowmovieDataSource)
        return nowmovieDataSource
    }

}