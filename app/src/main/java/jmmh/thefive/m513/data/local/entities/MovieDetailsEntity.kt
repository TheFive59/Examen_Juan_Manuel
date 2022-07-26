package jmmh.thefive.m513.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_details_table")
data class MovieDetailsEntity(
    @PrimaryKey()
    @ColumnInfo(name = "id") val id: Int=0,
    @ColumnInfo(name = "description") val overview: String,
    @ColumnInfo(name = "popularised") val popularity: Double,
    @ColumnInfo(name = "posterUrl") @SerializedName("poster_path")
    val posterPath: String,
    @ColumnInfo(name = "launch") @SerializedName("release_date")
    val releaseDate: String,
    @ColumnInfo(name = "duration") @SerializedName("runtime")
    val duracion: Int,
    @ColumnInfo(name = "statusMovie") val status: String,
    @ColumnInfo(name = "subtitle") val tagline: String,
    @ColumnInfo(name = "movie_name") val title: String,
    @ColumnInfo(name = "videoMovie") val video: Boolean,
    @ColumnInfo(name = "coverUrl") val backdrop_path: String,
    @ColumnInfo(name = "punctuation") @SerializedName("vote_average")
    val rating: Double
)

