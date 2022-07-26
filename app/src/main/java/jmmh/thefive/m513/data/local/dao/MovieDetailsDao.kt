package jmmh.thefive.m513.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jmmh.thefive.m513.data.local.entities.MovieDetailsEntity

@Dao
interface MovieDetailsDao {
    @Query("SELECT * FROM movie_details_table WHERE id >:idSearch")
    suspend fun getMovieDetails(idSearch:Int):List<MovieDetailsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movies: MovieDetailsEntity)

}