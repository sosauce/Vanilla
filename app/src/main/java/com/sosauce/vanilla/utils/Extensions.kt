package com.sosauce.vanilla.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowManager
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.sosauce.vanilla.data.calculator.Tokens
import com.sosauce.vanilla.domain.model.Calculation
import java.text.DecimalFormatSymbols

fun Modifier.thenIf(
    condition: Boolean,
    modifier: Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        this.then(modifier())
    } else this
}


fun List<Calculation>.sort(
    newestFirst: Boolean
): List<Calculation> {
    return if (newestFirst) {
        this.sortedByDescending { it.id }
    } else {
        this
    }
}

@Composable
fun anyLightColorScheme(): ColorScheme {
    val context = LocalContext.current

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        dynamicLightColorScheme(context)
    } else {
        lightColorScheme()
    }
}

@Composable
fun anyDarkColorScheme(): ColorScheme {
    val context = LocalContext.current

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        dynamicDarkColorScheme(context)
    } else {
        darkColorScheme()
    }
}

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

fun Char.isOperator(): Boolean {
    val operators =
        listOf(Tokens.ADD, Tokens.SUBTRACT, Tokens.DIVIDE, Tokens.MULTIPLY, Tokens.POWER)
    return this in operators
}


fun String.isErrorMessage(): Boolean {
    return any { char -> char.isLetter() }
}

fun CharSequence.whichParenthesis(): Char {
    return if (count { it == Tokens.OPEN_PARENTHESIS } > count { it == Tokens.CLOSED_PARENTHESIS }) {
        Tokens.CLOSED_PARENTHESIS
    } else {
        Tokens.OPEN_PARENTHESIS
    }
}


/**
 * Formats a number not an expression !!
 */
fun String.formatNumber(shouldFormat: Boolean): String {
    val number = this
    val localSymbols = DecimalFormatSymbols.getInstance()

    if (number.any { it.isLetter() } || !shouldFormat) return number

    val integer = number.takeWhile { it != '.' }
    val decimal = number.removePrefix(integer).replace('.', localSymbols.decimalSeparator)

    // 1234
    val formattedInteger = integer
        .reversed() // 4321
        .chunked(3) // [432, 1]
        .joinToString(localSymbols.groupingSeparator.toString()) // 432,1
        .reversed() // 1,234

    return "${formattedInteger}${decimal}"
}


fun String.formatExpression(shouldFormat: Boolean): String {

    if (!shouldFormat) return this

    var expression = this
    val numberRegex = Regex("[\\d.]+")

    numberRegex.findAll(expression).forEach { result ->
        expression = expression.replace(result.value, result.value.formatNumber(true))
    }

    return expression

}


fun Activity.showOnLockScreen(show: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(show)
    } else {
        if (show) {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }
}

fun Modifier.selfAlignHorizontally(align: Alignment.Horizontal = Alignment.CenterHorizontally): Modifier {
    return this.then(
        Modifier
            .fillMaxWidth()
            .wrapContentWidth(align)
    )
}
fun <T> bouncySpec() = spring<T>(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessLow
)

val Context.appVersion
    get() = packageManager.getPackageInfo(packageName, 0).versionName




