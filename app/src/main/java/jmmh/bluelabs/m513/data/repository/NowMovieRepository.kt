package jmmh.bluelabs.m513.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import jmmh.bluelabs.m513.data.api.MovieDBInterface
import jmmh.bluelabs.m513.data.api.POST_PER_PAGE
import jmmh.bluelabs.m513.data.value_object.Movie
import jmmh.bluelabs.m513.data.value_object.NowMovie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NowMovieRepository (private val apiService: MovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<NowMovie>>
    lateinit var moviesSourceFactory: NowSourceFactory

    fun fetchNowMovieList(compositeDisposable: CompositeDisposable): LiveData<PagedList<NowMovie>> {
        moviesSourceFactory = NowSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<NowMovieSource, NetworkState>(
            moviesSourceFactory.moviesLiveDataSource, NowMovieSource::networkState
        )
    }
}