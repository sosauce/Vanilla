package com.sosauce.vanilla.data.actions

sealed interface CalcAction {
    data object GetResult : CalcAction
    data object ResetField : CalcAction
    data object Backspace : CalcAction
    data class AddToField(
        val char: Char
    ) : CalcAction

    data class AddExpressionToField(
        val expression: String
    ) : CalcAction
}