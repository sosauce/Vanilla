package com.sosauce.vanilla.domain.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sosauce.vanilla.domain.model.Calculation

@Database(
    entities = [Calculation::class],
    version = 1
)
abstract class HistoryDatabase : RoomDatabase() {
    abstract val dao: HistoryDao
}