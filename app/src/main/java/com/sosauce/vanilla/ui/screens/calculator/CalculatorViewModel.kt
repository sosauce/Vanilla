@file:OptIn(FlowPreview::class)

package com.sosauce.vanilla.ui.screens.calculator

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.notkamui.keval.KevalInvalidExpressionException
import com.sosauce.vanilla.data.actions.CalcAction
import com.sosauce.vanilla.data.calculator.Evaluator
import com.sosauce.vanilla.core.data.preferences.getDecimalPrecision
import com.sosauce.vanilla.utils.backspace
import com.sosauce.vanilla.utils.insertText
import com.sosauce.vanilla.utils.isErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class CalculatorViewModel(
    private val application: Application
) : AndroidViewModel(application) {


    val textFieldState = TextFieldState()
    var evaluatedCalculation by mutableStateOf("")
        private set

    private val _previewShowErrors = MutableStateFlow(false)
    val previewShowErrors = _previewShowErrors.asStateFlow()


    init {
        viewModelScope.launch {
            combine(
                snapshotFlow { textFieldState.text.toString() },
                getDecimalPrecision(application)
            ) { expression, precision ->

                _previewShowErrors.update { false }
                Evaluator.eval2(expression, precision)

            }.collectLatest { result ->
                result
                    .onSuccess {
                        evaluatedCalculation = it
                    }
                    .onFailure { error ->
                        if (error !is KevalInvalidExpressionException) {
                            evaluatedCalculation = error.message ?: "Error"
                            _previewShowErrors.update { true }
                        }
                    }
            }
        }
    }

    fun handleAction(action: CalcAction) {
        when (action) {
            is CalcAction.GetResult -> {
                textFieldState.setTextAndPlaceCursorAtEnd(evaluatedCalculation)
            }

            is CalcAction.AddToField -> textFieldState.insertText(action.char)
            is CalcAction.ResetField -> textFieldState.clearText()
            is CalcAction.Backspace -> textFieldState.backspace()
            is CalcAction.AddExpressionToField -> textFieldState.setTextAndPlaceCursorAtEnd(action.expression)
        }
    }

}