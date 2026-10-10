package com.sosauce.vanilla.feature.history.presentation

import com.sosauce.vanilla.core.database.Calculation

sealed interface HistoryEvents {

    data class DeleteCalculation(val calculation: Calculation) : HistoryEvents
    data object DeleteAllCalculation : HistoryEvents
    data class AddCalculation(
        val operation: String,
        val result: String,
        val maxHistoryItems: Int,
        val saveErrors: Boolean
    ) : HistoryEvents
}