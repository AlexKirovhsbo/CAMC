package com.example.camc.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.camc.model.room.entities.GyroReading
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Gyro Table
 */
@Dao
interface GyroDao {
    @Insert
    suspend fun insertReading(reading: GyroReading)

    @Delete
    suspend fun deleteReading(reading: GyroReading)

    @Query("SELECT * FROM GyroReading ORDER BY timestampMillis ASC")
    fun getReadingsOrderedByTime(): Flow<List<GyroReading>>

    @Query("DELETE FROM GyroReading")
    suspend fun nukeReadings()
}