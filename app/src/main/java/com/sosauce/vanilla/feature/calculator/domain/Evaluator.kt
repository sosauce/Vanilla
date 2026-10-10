package com.sosauce.vanilla.feature.calculator.domain

import com.notkamui.keval.BigDecimal
import com.notkamui.keval.Keval
import com.notkamui.keval.KevalInvalidArgumentException
import com.notkamui.keval.KevalNumbers
import com.sosauce.vanilla.core.domain.Tokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.PI
import kotlin.math.sqrt

class NegativeSquareRootException : RuntimeException("Be for real 3:<")
object Evaluator {

    private val KEVAL = Keval.create(KevalNumbers.BigDecimal) {
        includeDefault()
        binaryOperator {
            symbol = Tokens.MULTIPLY
            precedence = 3
            isLeftAssociative = true
            implementation = { a, b -> a * b }
        }

        unaryOperator {
            symbol = Tokens.FACTORIAL
            isPrefix = false
            implementation = {
                if (it < BigDecimal.ZERO) throw KevalInvalidArgumentException("Factorial of a negative number")
                if (!it.isWholeNumber()) throw KevalInvalidArgumentException("Factorial of a non-integer")
                fac(it, it)
            }
        }

        unaryOperator {
            symbol = Tokens.SQUARE_ROOT
            isPrefix = true
            implementation = { arg ->
                if (arg < BigDecimal.ZERO) throw NegativeSquareRootException()
                arg.betterSqrt(32)
            }
        }

        unaryOperator {
            symbol = Tokens.MODULO
            isPrefix = false
            implementation = { arg -> arg.divide(100.toBigDecimal()) }
        }

        constant {
            name = "PI"
            value = PI.toBigDecimal()
        }

    }

    @JvmStatic
    suspend fun eval2(formula: String, precision: Int): Result<String> = withContext(Dispatchers.Default) {
        return@withContext try {
            if (formula.isEmpty()) return@withContext Result.success("")
            val result = KEVAL
                .eval(formula.replace(Tokens.PI.toString(), "PI").handleRelativePercentage())
                .setScale(precision, RoundingMode.HALF_EVEN)
                .stripTrailingZeros().toPlainString()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // We don't call "handleRelativePercentage" here to avoid recursive call
    @JvmStatic
    private fun evalParenthesis(formula: String): String {
        val result = KEVAL.eval(formula)
        return result.stripTrailingZeros().toPlainString()
    }

    private fun String.handleRelativePercentage(): String {
        val expression = processParenthesisExpression()

        return relativePercentageRegex.replace(expression) { match ->
            val firstOperand = match.groupValues[1]
            val operator = match.groupValues[2]
            val percentage = match.groupValues[3]

            when (operator) {
                "+" -> "$firstOperand + ($firstOperand * $percentage / 100)"
                "-" -> "$firstOperand - ($firstOperand * $percentage / 100)"
                "*" -> "$firstOperand * ($percentage / 100)"
                else -> firstOperand
            }

        }

    }

    private fun String.processParenthesisExpression(): String {
        val hasModulo = this.contains("%")

        return parenthesisRegex.replace(this) { matchResult ->
            val calculated = evalParenthesis(matchResult.value)
            if (hasModulo) calculated else "($calculated)"
        }

    }

    private val parenthesisRegex = Regex("""\(([^()]+)\)""")
    private val relativePercentageRegex = Regex("""(\d+(?:\.\d+)?)\s*([+\-*])\s*(\d+(?:\.\d+)?)%""")


}


private fun fac(n: BigDecimal, acc: BigDecimal): BigDecimal {
    return if (n == BigDecimal.ONE) {
        acc
    } else {
        val lessOne = n.subtract(BigDecimal.ONE)
        fac(lessOne, acc.multiply(lessOne))
    }
}

private fun BigDecimal.isWholeNumber(): Boolean {
    return remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0
}


// Source - https://stackoverflow.com/a/19743026
// Posted by barwnikk
// Retrieved 2026-05-23, License - CC BY-SA 3.0
private fun BigDecimal.betterSqrt(scale: Int): BigDecimal {
    val two = 2.toBigDecimal()
    var x0 = BigDecimal.ZERO
    var x1 = sqrt(this.toDouble()).toBigDecimal()

    while (x0 != x1) {
        x0 = x1
        x1 = this.divide(x0, scale, RoundingMode.HALF_UP)
        x1 = x1.add(x0)
        x1 = x1.divide(two, scale, RoundingMode.HALF_UP)
    }

    return x1
}