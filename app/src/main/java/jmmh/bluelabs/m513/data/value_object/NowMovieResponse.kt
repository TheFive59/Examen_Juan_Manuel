package jmmh.bluelabs.m513.data.value_object

import com.google.gson.annotations.SerializedName

data class NowMovieResponse(
    val page: Int,
    @SerializedName("results")
    val nowmovieList: List<NowMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int

)