package jmmh.thefive.m513.model

import com.google.gson.annotations.SerializedName
import jmmh.thefive.m513.model.Movie

data class MovieResponse(
    val page: Int,
    @SerializedName("results")
    val movieList: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)