package com.sosauce.vanilla.core.domain

import java.text.DecimalFormatSymbols

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
 *
 * The expression itself always stays in its canonical form ('.' decimal, no
 * grouping), only the displayed text is mapped to the chosen separators.
 */
fun String.formatNumber(
    shouldFormat: Boolean,
    decimalSeparator: Char = DecimalFormatSymbols.getInstance().decimalSeparator,
    groupingSeparator: Char? = DecimalFormatSymbols.getInstance().groupingSeparator
): String {
    val number = this

    if (number.any { it.isLetter() } || !shouldFormat) return number

    val integer = number.takeWhile { it != '.' }
    val decimal = number.removePrefix(integer).replace('.', decimalSeparator)
    val grouping = groupingSeparator?.takeIf { it != decimalSeparator }

    // 1234
    val formattedInteger = if (grouping == null) {
        integer
    } else {
        integer
            .reversed() // 4321
            .chunked(3) // [432, 1]
            .joinToString(grouping.toString()) // 432,1
            .reversed() // 1,234
    }

    return "${formattedInteger}${decimal}"
}

fun String.toDecimalChar(symbols: DecimalFormatSymbols): Char =
    when (this) {
        DecimalSeparator.DOT -> '.'
        DecimalSeparator.COMMA -> ','
        else -> symbols.decimalSeparator
    }

fun String.toGroupingChar(symbols: DecimalFormatSymbols): Char? =
    when (this) {
        GroupingSeparator.NONE -> null
        GroupingSeparator.COMMA -> ','
        GroupingSeparator.DOT -> '.'
        GroupingSeparator.SPACE -> ' '
        GroupingSeparator.APOSTROPHE -> '\''
        else -> symbols.groupingSeparator
    }
