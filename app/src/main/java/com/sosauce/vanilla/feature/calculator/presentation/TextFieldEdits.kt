package com.sosauce.vanilla.feature.calculator.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import com.sosauce.vanilla.core.domain.Tokens
import com.sosauce.vanilla.core.domain.isOperator

fun TextFieldState.insertText(char: Char) {


    val expression = this.text
    val cursorPosition = selection.start
    val charInfrontCursor = expression.getOrNull(cursorPosition - 1) ?: ' '
    val charBehindCursor = expression.getOrNull(cursorPosition) ?: ' '


    when (char) {
        Tokens.ZERO, Tokens.ONE, Tokens.TWO, Tokens.THREE, Tokens.FOUR,
        Tokens.FIVE, Tokens.SIX, Tokens.SEVEN, Tokens.EIGHT, Tokens.NINE,
        Tokens.PI, Tokens.OPEN_PARENTHESIS, Tokens.CLOSED_PARENTHESIS,
        Tokens.SQUARE_ROOT, Tokens.MODULO, Tokens.FACTORIAL -> {
            edit { insert(cursorPosition, char.toString()) }
        }

        Tokens.DECIMAL -> {
            val toInsert = if (!charInfrontCursor.isDigit()) {
                "${Tokens.ZERO}${Tokens.DECIMAL}"
            } else Tokens.DECIMAL.toString()

            edit { insert(cursorPosition, toInsert) }
        }

        Tokens.SUBTRACT -> {
            if (charInfrontCursor.isOperator()) {
                edit { insert(cursorPosition, "${Tokens.OPEN_PARENTHESIS}${Tokens.SUBTRACT}") }
            } else if (charBehindCursor.isOperator()) {
                edit {
                    replace(cursorPosition, cursorPosition + 1, char.toString())
                }
            } else {
                edit { insert(cursorPosition, char.toString()) }
            }

        }


        Tokens.ADD, Tokens.DIVIDE, Tokens.MULTIPLY, Tokens.POWER -> {
            if (charInfrontCursor.isOperator()) {
                edit { replace(cursorPosition - 1, cursorPosition, char.toString()) }
            } else if (charBehindCursor.isOperator()) {
                edit {
                    replace(cursorPosition, cursorPosition + 1, char.toString())
                }
            } else {
                edit { insert(cursorPosition, char.toString()) }
            }
        }
    }
}

fun TextFieldState.backspace() {
    val cursorPosition = selection.start
    if (selection.collapsed && cursorPosition > 0) {
        edit {
            delete(cursorPosition - 1, cursorPosition)
        }
    }
}
