package com.sosauce.vanilla.feature.history.domain

import com.sosauce.vanilla.core.domain.EmptyResult
import com.sosauce.vanilla.core.domain.DataError
import kotlinx.coroutines.flow.Flow

interface HistoryLocalDataSource {
    fun observeCalculations(): Flow<List<Calculation>>
    suspend fun insertCalculation(calculation: Calculation): EmptyResult<DataError.Local>
    suspend fun deleteCalculation(calculation: Calculation): EmptyResult<DataError.Local>
    suspend fun deleteAllCalculations(): EmptyResult<DataError.Local>
}
