package com.example.camc.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.camc.model.room.dao.AccelerationDao
import com.example.camc.model.room.dao.GyroDao
import com.example.camc.model.room.dao.LocationDao
import com.example.camc.model.room.dao.MagnetDao
import com.example.camc.model.room.entities.AccelerationReading
import com.example.camc.model.room.entities.GyroReading
import com.example.camc.model.room.entities.LocationReading
import com.example.camc.model.room.entities.MagnetReading

@Database(
    entities = [
        AccelerationReading::class,
        LocationReading::class,
        GyroReading::class,
        MagnetReading::class],
    version  = 5
)
abstract class ReadingsDatabase: RoomDatabase() {
    abstract val accelerationDao: AccelerationDao
    abstract val gyroDao: GyroDao
    abstract val magnetDao: MagnetDao

    abstract val locationDao: LocationDao
}