package jmmh.bluelabs.m513.data.api

import io.reactivex.Single
import jmmh.bluelabs.m513.data.value_object.MovieDetails
import jmmh.bluelabs.m513.data.value_object.MovieResponse
import jmmh.bluelabs.m513.data.value_object.NowMovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDBInterface {
    //Obtain
    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovie(@Query("page") numerpage: Int): Single<NowMovieResponse>
}