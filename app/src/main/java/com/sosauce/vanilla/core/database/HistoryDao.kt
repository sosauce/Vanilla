package com.sosauce.vanilla.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert
    suspend fun insertCalculation(calculation: Calculation)

    @Delete
    suspend fun deleteCalculation(calculation: Calculation)

    @Query("DELETE FROM calculation")
    suspend fun deleteAllCalculations()

    @Query("SELECT * FROM calculation ORDER BY id ASC")
    fun getAllCalculations(): Flow<List<Calculation>>
}