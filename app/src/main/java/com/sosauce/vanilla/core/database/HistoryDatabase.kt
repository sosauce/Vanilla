package com.sosauce.vanilla.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CalculationEntity::class],
    version = 1
)
abstract class HistoryDatabase : RoomDatabase() {
    abstract val dao: HistoryDao
}