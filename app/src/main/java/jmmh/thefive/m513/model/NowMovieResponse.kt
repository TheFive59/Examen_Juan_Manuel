package jmmh.thefive.m513.model

import com.google.gson.annotations.SerializedName
import jmmh.thefive.m513.model.NowMovie

data class NowMovieResponse(
    val page: Int,
    @SerializedName("results")
    val nowmovieList: List<NowMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int

)