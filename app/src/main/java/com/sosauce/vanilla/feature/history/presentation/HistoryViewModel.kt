package com.sosauce.vanilla.feature.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosauce.vanilla.core.domain.isErrorMessage
import com.sosauce.vanilla.feature.history.domain.Calculation
import com.sosauce.vanilla.feature.history.domain.HistoryLocalDataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val localDataSource: HistoryLocalDataSource,
) : ViewModel() {



    val allCalculations = localDataSource.observeCalculations()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun handleHistoryEvent(event: HistoryEvents) {
        when (event) {
            is HistoryEvents.AddCalculation -> {

                val calculation = Calculation(
                    operation = event.operation,
                    result = event.result
                )

                if (event.saveErrors || !event.result.isErrorMessage()) {
                    viewModelScope.launch {
                        if (allCalculations.value.size == event.maxHistoryItems) {
                            localDataSource.deleteCalculation(allCalculations.value.first())
                        }
                        localDataSource.insertCalculation(calculation)
                    }
                } else {
                    return
                }

            }

            is HistoryEvents.DeleteCalculation -> {
                viewModelScope.launch { localDataSource.deleteCalculation(event.calculation) }
            }

            is HistoryEvents.DeleteAllCalculation -> {
                viewModelScope.launch { localDataSource.deleteAllCalculations() }
            }
        }
    }
}
