package com.example.camc.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.camc.model.room.entities.MagnetReading
import kotlinx.coroutines.flow.Flow


/**
 * Data Access Object for Magnet Table
 */
@Dao
interface MagnetDao {
    @Insert
    suspend fun insertReading(reading: MagnetReading)

    @Delete
    suspend fun deleteReading(reading: MagnetReading)

    @Query("SELECT * FROM Magnetreading ORDER BY timestampMillis ASC")
    fun getReadingsOrderedByTime(): Flow<List<MagnetReading>>

    @Query("DELETE FROM Magnetreading")
    suspend fun nukeReadings()
}