package com.sosauce.vanilla.feature.calculator.presentation

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
import com.sosauce.vanilla.feature.calculator.domain.Evaluator
import com.sosauce.vanilla.core.data.preferences.getDecimalPrecision
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun handleAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.GetResult -> {
                textFieldState.setTextAndPlaceCursorAtEnd(evaluatedCalculation)
            }

            is CalculatorAction.AddToField -> textFieldState.insertText(action.char)
            is CalculatorAction.ResetField -> textFieldState.clearText()
            is CalculatorAction.Backspace -> textFieldState.backspace()
            is CalculatorAction.AddExpressionToField -> textFieldState.setTextAndPlaceCursorAtEnd(action.expression)
        }
    }

}