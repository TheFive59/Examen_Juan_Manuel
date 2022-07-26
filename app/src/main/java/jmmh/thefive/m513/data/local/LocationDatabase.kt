package jmmh.thefive.m513.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import jmmh.thefive.m513.data.local.dao.LocationDao
import jmmh.thefive.m513.data.local.dao.MovieDetailsDao
import jmmh.thefive.m513.data.local.entities.Location
import jmmh.thefive.m513.data.local.entities.MovieDetailsEntity

@Database(
    entities = [Location::class],
    version = 1
)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun daoLocation(): LocationDao
}