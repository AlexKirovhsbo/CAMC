package com.example.camc.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.camc.model.room.entities.AccelerationReading
import com.example.camc.model.room.entities.AccelerationReadingInfo
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Acceleration Table
 */
@Dao
interface AccelerationDao {
    @Insert
    suspend fun insertReading(reading: AccelerationReading)

    @Delete
    suspend fun deleteReading(reading: AccelerationReading)

    @Query("SELECT * FROM accelerationreading ORDER BY timestampMillis ASC")
    fun getReadingsOrderedByTime(): Flow<List<AccelerationReading>>

    @Query("""
        SELECT timestampMillis, xAxis, yAxis, zAxis, transportationMode, sensor 
        FROM accelerationreading 
        ORDER BY timestampMillis ASC
    """)
    fun getReadingInfoOrderedByTime(): Flow<List<AccelerationReadingInfo>>

    @Query("DELETE FROM accelerationreading")
    suspend fun nukeReadings()
}