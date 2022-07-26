package jmmh.thefive.m513.model

import com.google.gson.annotations.SerializedName

data class MovieDetails (
    val id: Int,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("runtime")
    val duracion: Int,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val backdrop_path:String,
    @SerializedName("vote_average")
    val rating: Double
        )