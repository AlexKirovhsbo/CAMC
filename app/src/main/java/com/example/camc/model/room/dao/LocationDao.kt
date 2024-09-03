package com.example.camc.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.camc.model.room.entities.LocationReading
import com.example.camc.model.room.entities.LocationReadingInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert
    suspend fun insertReading(reading: LocationReading)

    @Delete
    suspend fun deleteReading(reading: LocationReading)

    @Query("SELECT * FROM locationreading ORDER BY timestampMillis ASC")
    fun getReadingsOrderedByTime(): Flow<List<LocationReading>>

    @Query("""
        SELECT timestampMillis, velocity, bearing, transportationMode, sensor 
        FROM locationreading 
        ORDER BY timestampMillis ASC
    """)
    fun getReadingInfoOrderedByTime(): Flow<List<LocationReadingInfo>>

    @Query("DELETE FROM locationreading")
    suspend fun nukeReadings()
}