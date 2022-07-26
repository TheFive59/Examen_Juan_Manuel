package jmmh.bluelabs.m513.ui.now_playing_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable

import jmmh.bluelabs.m513.data.repository.NetworkState
import jmmh.bluelabs.m513.data.repository.NowMovieRepository
import jmmh.bluelabs.m513.data.value_object.NowMovie
import jmmh.bluelabs.m513.data.value_object.NowMovieResponse

class NowMovieModel (private val movieRepository : NowMovieRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val  moviePagedListNow : LiveData<PagedList<NowMovie>> by lazy {
        movieRepository.fetchNowMovieList(compositeDisposable)
    }

   private val  moviePagedList : MutableLiveData<List<NowMovieResponse>> by lazy {
        MutableLiveData<List<NowMovieResponse>>().also {
         //loadUsers()
        }

    }
    val  networkState : LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedListNow.value?.isEmpty() ?: true
    }

    fun getUsers(): LiveData<List<NowMovieResponse>> {
        return moviePagedList
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}