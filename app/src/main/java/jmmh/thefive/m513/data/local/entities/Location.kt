package jmmh.thefive.m513.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "location_table")
data class Location(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val datetime: String,
    val latitude: String,
    val location: String
)

