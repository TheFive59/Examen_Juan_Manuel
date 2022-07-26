package jmmh.bluelabs.m513.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import jmmh.bluelabs.m513.data.api.MovieDBInterface
import jmmh.bluelabs.m513.data.api.POST_PER_PAGE
import jmmh.bluelabs.m513.data.value_object.Movie


class MoviePagedRepository(private val apiService: MovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesSourceFactory: MovieSourceFactory

    fun fetchLiveMovieList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        moviesSourceFactory = MovieSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }
}