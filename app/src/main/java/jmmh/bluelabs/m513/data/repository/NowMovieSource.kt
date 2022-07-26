package jmmh.bluelabs.m513.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import jmmh.bluelabs.m513.data.api.FIRST_PAGE
import jmmh.bluelabs.m513.data.api.MovieDBInterface
import jmmh.bluelabs.m513.data.value_object.NowMovie


class NowMovieSource(private val apiService: MovieDBInterface, private val compositeDisposable: CompositeDisposable) :
    PageKeyedDataSource<Int, NowMovie>() {
    private var page = 2

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, NowMovie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getNowPlayingMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    // if (it.totalPages >= params.key) {
                    //                            callback.onResult(it.nowmovieList, params.key + 1)
                    //                            networkState.postValue(NetworkState.LOADED)
                    //                        }
                    {
                        callback.onResult(it.nowmovieList, null, page + 1)
                        networkState.postValue(NetworkState.LOADED)

//println("loadInitial onResponse1111 :${it.nowmovieList.get(1)}")
  //                      Log.e("MovieDataSourcea","loadInitial onResponse1111 :${it.nowmovieList.get(0)}***${it.nowmovieList.get(2)}***${it.nowmovieList.get(3)}")
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message.toString())
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NowMovie>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getNowPlayingMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.totalPages >= params.key) {
                            callback.onResult(it.nowmovieList, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message.toString())
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NowMovie>) {

    }
}