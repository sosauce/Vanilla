@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.feature.calculator.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sosauce.nekobites.animations.AnimatedCounter
import com.sosauce.vanilla.core.domain.Tokens
import com.sosauce.vanilla.core.presentation.rememberColoredOperators
import com.sosauce.vanilla.core.presentation.rememberDecimal
import com.sosauce.vanilla.core.presentation.rememberDecimalSeparator
import com.sosauce.vanilla.core.presentation.rememberGroupingSeparator
import com.sosauce.vanilla.core.presentation.rememberUseSystemFont
import com.sosauce.vanilla.feature.calculator.presentation.CalculatorViewModel
import com.sosauce.vanilla.core.designsystem.theme.nunitoFontFamily
import com.sosauce.vanilla.core.domain.formatNumber
import com.sosauce.vanilla.core.domain.isErrorMessage
import com.sosauce.vanilla.core.presentation.rememberResolvedSeparators

@Composable
fun CalculationDisplay(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel
) {

    val useSystemFont by rememberUseSystemFont()
    val shouldFormat by rememberDecimal()
    val decimalPreference by rememberDecimalSeparator()
    val groupingPreference by rememberGroupingSeparator()
    val (decimalSeparator, groupingSeparator) = rememberResolvedSeparators(
        decimalPreference = decimalPreference,
        groupingPreference = groupingPreference
    )
    val scrollState = rememberScrollState()
    val previewScrollState = rememberScrollState()
    val previewCanShowErrors by viewModel.previewShowErrors.collectAsStateWithLifecycle()
    val coloredOperators by rememberColoredOperators()
    val isError = viewModel.evaluatedCalculation.isErrorMessage()



    LaunchedEffect(viewModel.textFieldState.text) {
        scrollState.animateScrollTo(scrollState.maxValue)
        previewScrollState.animateScrollTo(previewScrollState.maxValue)
    }


    Column(
        modifier = modifier.padding(5.dp),
        verticalArrangement = Arrangement.Bottom
    ) {

        if (isError && previewCanShowErrors) {
            Text(
                text = viewModel.evaluatedCalculation,
                style = MaterialTheme.typography.displayMediumEmphasized.copy(
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(previewScrollState)
            )
        } else {
            Text(
                text = viewModel.evaluatedCalculation.formatNumber(
                    shouldFormat = shouldFormat,
                    decimalSeparator = decimalSeparator,
                    groupingSeparator = groupingSeparator
                ),
                style = MaterialTheme.typography.displayMediumEmphasized.copy(
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(previewScrollState)
            )
        }

        DisableSoftKeyboard {
            BasicTextField(
                state = viewModel.textFieldState,
                lineLimits = TextFieldLineLimits.SingleLine,
                textStyle = MaterialTheme.typography.displayMediumEmphasized.copy(
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = if (!useSystemFont) nunitoFontFamily else null,
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.fillMaxWidth(),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                scrollState = scrollState,
                outputTransformation = CalculatorOutputTransform(
                    format = shouldFormat,
                    coloredOperators = coloredOperators,
                    operatorColor = MaterialTheme.colorScheme.primary,
                    decimalSeparator = decimalSeparator,
                    groupingSeparator = groupingSeparator
                )
            )
        }
    }
}

class CalculatorOutputTransform(
    private val format: Boolean,
    private val coloredOperators: Boolean,
    private val operatorColor: Color,
    private val decimalSeparator: Char,
    private val groupingSeparator: Char?
) : OutputTransformation {

    override fun TextFieldBuffer.transformOutput() {


        if (format) {
            val expression = originalText.toString()
            if (expression.isEmpty()) return

            var shift = 0

            NUMBERS_REGEX.findAll(expression).forEach { match ->
                val start = match.range.first + shift
                val end = match.range.last + 1 + shift
                val number = match.value
                val formatted = number.formatNumber(
                    shouldFormat = true,
                    decimalSeparator = decimalSeparator,
                    groupingSeparator = groupingSeparator
                )

                replace(start, end, formatted)
                shift += formatted.length - number.length
            }

        }

        if (coloredOperators) {
            val operators =
                setOf(Tokens.ADD, Tokens.SUBTRACT, Tokens.MULTIPLY, Tokens.DIVIDE, Tokens.POWER)
            asCharSequence().forEachIndexed { index, char ->
                if (char in operators) {
                    addStyle(SpanStyle(color = operatorColor), index, index + 1)
                }
            }
        }

    }

    companion object {
        val NUMBERS_REGEX = "[\\d.]+".toRegex()
    }
}