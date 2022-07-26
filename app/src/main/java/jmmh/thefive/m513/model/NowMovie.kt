package jmmh.thefive.m513.model

import com.google.gson.annotations.SerializedName

data class NowMovie(
    val id: Int,
    @SerializedName("poster_path")
    val nowposterPath: String,
    @SerializedName("release_date")
    val nowreleaseDate: String,
    @SerializedName("vote_average")
    val nowvoteAverage: String,
    val title: String
)