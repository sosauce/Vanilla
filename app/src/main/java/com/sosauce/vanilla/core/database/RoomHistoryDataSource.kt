package com.sosauce.vanilla.core.database

import android.database.sqlite.SQLiteFullException
import com.sosauce.vanilla.core.domain.DataError
import com.sosauce.vanilla.core.domain.EmptyResult
import com.sosauce.vanilla.core.domain.Result
import com.sosauce.vanilla.feature.history.domain.Calculation
import com.sosauce.vanilla.feature.history.domain.HistoryLocalDataSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomHistoryDataSource(
    private val dao: HistoryDao
) : HistoryLocalDataSource {

    override fun observeCalculations(): Flow<List<Calculation>> =
        dao.getAllCalculations().map { entities ->
            entities.map { it.toCalculation() }
        }

    override suspend fun insertCalculation(calculation: Calculation): EmptyResult<DataError.Local> =
        persist { dao.insertCalculation(calculation.toCalculationEntity()) }

    override suspend fun deleteCalculation(calculation: Calculation): EmptyResult<DataError.Local> =
        persist { dao.deleteCalculation(calculation.toCalculationEntity()) }

    override suspend fun deleteAllCalculations(): EmptyResult<DataError.Local> =
        persist { dao.deleteAllCalculations() }

    private suspend inline fun persist(block: suspend () -> Unit): EmptyResult<DataError.Local> {
        return try {
            block()
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
