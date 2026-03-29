package com.sosauce.vanilla.data.calculator

import com.notkamui.keval.Keval
import com.notkamui.keval.KevalInvalidArgumentException
import com.notkamui.keval.KevalInvalidExpressionException
import com.notkamui.keval.KevalZeroDivisionException
import java.math.RoundingMode
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class NegativeSquareRootException : RuntimeException("Be for real 3:<")
class ValueTooLargeException : RuntimeException("Value too large")
object Evaluator {

    private val KEVAL = Keval.create {
        binaryOperator {
            symbol = Tokens.ADD
            precedence = 2
            isLeftAssociative = true
            implementation = { a, b -> a + b }
        }
        unaryOperator {
            symbol = Tokens.ADD
            isPrefix = true
            implementation = { it }
        }
        binaryOperator {
            symbol = Tokens.SUBTRACT
            precedence = 2
            isLeftAssociative = true
            implementation = { a, b -> a - b }
        }
        unaryOperator {
            symbol = Tokens.SUBTRACT
            isPrefix = true
            implementation = { -it }
        }

        binaryOperator {
            symbol = Tokens.MULTIPLY
            precedence = 3
            isLeftAssociative = true
            implementation = { a, b -> a * b }
        }

        binaryOperator {
            symbol = Tokens.DIVIDE
            precedence = 3
            isLeftAssociative = true
            implementation = { a, b ->
                if (b == 0.0) throw KevalZeroDivisionException()
                a / b
            }
        }

        binaryOperator {
            symbol = Tokens.POWER
            precedence = 4
            isLeftAssociative = false
            implementation = { a, b -> a.pow(b) }
        }

        unaryOperator {
            symbol = Tokens.FACTORIAL
            isPrefix = false
            implementation = {
                if (it < 0) throw KevalInvalidArgumentException("Factorial of a negative number")
                if (floor(it) != it) throw KevalInvalidArgumentException("Factorial of a non-integer")
                (1..it.toInt()).fold(1.0) { acc, i -> acc * i }
            }
        }

        unaryOperator {
            symbol = Tokens.SQUARE_ROOT
            isPrefix = true
            implementation =
                { arg -> if (arg < 0) throw NegativeSquareRootException() else sqrt(arg) }
        }

        unaryOperator {
            symbol = Tokens.MODULO
            isPrefix = false
            implementation = { arg -> arg / 100 }
        }

        constant {
            name = "PI"
            value = Math.PI
        }

    }

    private var prevResult: String = ""


    @JvmStatic
    fun eval(
        formula: String,
        precision: Int
    ): String = try {
        val result = KEVAL
            .eval(formula.replace(Tokens.PI.toString(), "PI").handleRelativePercentage())

        val formattedResult = if (result > Double.MAX_VALUE) {
            throw ValueTooLargeException()
        } else {
            result
                .toBigDecimal()
                .setScale(precision, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()
        }
        prevResult = formattedResult
        formattedResult
    } catch (e: KevalInvalidExpressionException) {
        prevResult
    } catch (e: Exception) {
        e.message ?: "Undetermined error"
    }

    // We don't call "handleRelativePercentage" here to avoid recursive call
    @JvmStatic
    private fun evalParenthesis(formula: String): String {
        val result = KEVAL.eval(formula)
        return if (result > Double.MAX_VALUE) {
            throw ValueTooLargeException()
        } else {
            result.toBigDecimal().stripTrailingZeros().toPlainString()
        }
    }

    private fun String.handleRelativePercentage(): String {

        return relativePercentageRegex.replace(this.processParenthesisExpression()) { match ->
            val firstOperand = match.groupValues[1].toDouble()
            val operator = match.groupValues[2]
            val percentage = match.groupValues[3].toDouble()

            when (operator) {
                "+" -> "$firstOperand + ($firstOperand * $percentage / 100)"
                "-" -> "$firstOperand - ($firstOperand * $percentage / 100)"
                "*" -> "$firstOperand * ($percentage / 100)"
                else -> "$firstOperand"
            }

        }

    }

    private fun String.processParenthesisExpression(): String {
        var expression = this


        parenthesisRegex.findAll(this).forEach { matchResult ->
            val calculated = evalParenthesis(matchResult.value)
            val replaceWith = if (this.contains("%")) calculated else "($calculated)"
            expression = expression.replace(matchResult.value, replaceWith)
        }
        return expression
    }

    private val parenthesisRegex = Regex("""\(([^()]+)\)""")
    private val relativePercentageRegex = Regex("""(\d+(?:\.\d+)?)\s*([+\-*])\s*(\d+(?:\.\d+)?)%""")


}
