package jmmh.thefive.m513.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import jmmh.thefive.m513.data.local.dao.MovieDetailsDao
import jmmh.thefive.m513.data.local.entities.MovieDetailsEntity

@Database(
    entities = [MovieDetailsEntity::class],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun daoMovie(): MovieDetailsDao
}