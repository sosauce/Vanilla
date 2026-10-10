package com.sosauce.vanilla.feature.history.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.sosauce.vanilla.core.database.Calculation

data class HistoryState(
    val calculations: List<Calculation> = emptyList(),
    val operation: MutableState<String> = mutableStateOf(""),
    val result: MutableState<String> = mutableStateOf("")
)