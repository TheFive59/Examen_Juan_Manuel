package jmmh.thefive.m513.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import jmmh.thefive.m513.data.api.MovieDBInterface
import jmmh.thefive.m513.model.Movie
import jmmh.thefive.m513.data.utils.Constants
import jmmh.thefive.m513.data.remote.MovieDataSource


class MoviePagedRepository(private val apiService: MovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesSourceFactory: MovieSourceFactory

    fun fetchLiveMovieList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        moviesSourceFactory = MovieSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(Constants.POST_PER_PAGE)
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