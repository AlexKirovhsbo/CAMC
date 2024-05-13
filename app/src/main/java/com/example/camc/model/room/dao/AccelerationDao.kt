package com.example.camc.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.camc.model.room.entities.AccelerationReading
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

    @Query("DELETE FROM accelerationreading")
    suspend fun nukeReadings()
}