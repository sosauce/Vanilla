package com.sosauce.vanilla.domain.repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.sosauce.vanilla.domain.model.Calculation

data class HistoryState(
    val calculations: List<Calculation> = emptyList(),
    val operation: MutableState<String> = mutableStateOf(""),
    val result: MutableState<String> = mutableStateOf("")
)