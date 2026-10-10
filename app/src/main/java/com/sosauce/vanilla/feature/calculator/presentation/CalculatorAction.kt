package com.sosauce.vanilla.feature.calculator.presentation

sealed interface CalculatorAction {
    data object GetResult : CalculatorAction
    data object ResetField : CalculatorAction
    data object Backspace : CalculatorAction
    data class AddToField(
        val char: Char
    ) : CalculatorAction

    data class AddExpressionToField(
        val expression: String
    ) : CalculatorAction
}