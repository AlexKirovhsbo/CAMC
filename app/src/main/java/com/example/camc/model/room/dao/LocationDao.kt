package com.example.camc.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.camc.model.room.entities.LocationReading
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert
    suspend fun insertReading(reading: LocationReading)

    @Delete
    suspend fun deleteReading(reading: LocationReading)

    @Query("SELECT * FROM locationreading ORDER BY timestampMillis ASC")
    fun getReadingsOrderedByTime(): Flow<List<LocationReading>>

    @Query("DELETE FROM locationreading")
    suspend fun nukeReadings()
}