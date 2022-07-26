package jmmh.thefive.m513.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jmmh.thefive.m513.data.local.entities.Location
import jmmh.thefive.m513.data.local.entities.MovieDetailsEntity

@Dao
interface LocationDao {
    @Query("SELECT * FROM location_table")
     suspend fun getLocation(): List<Location>

    @Insert
    suspend fun insertLocation(location: Location)
}