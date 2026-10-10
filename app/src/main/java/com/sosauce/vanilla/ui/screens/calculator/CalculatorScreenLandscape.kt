@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.ui.screens.calculator

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.navigation3.runtime.NavKey
import com.sosauce.vanilla.R
import com.sosauce.vanilla.data.actions.CalcAction
import com.sosauce.vanilla.data.calculator.Tokens
import com.sosauce.vanilla.data.datastore.rememberDecimalSeparator
import com.sosauce.vanilla.data.datastore.rememberGroupingSeparator
import com.sosauce.vanilla.data.datastore.rememberHistoryMaxItems
import com.sosauce.vanilla.data.datastore.rememberSaveErrorsToHistory
import com.sosauce.vanilla.data.datastore.rememberShowClearButton
import com.sosauce.vanilla.data.datastore.rememberSwapZeroAndDecimal
import com.sosauce.vanilla.data.datastore.rememberUseHistory
import com.sosauce.vanilla.domain.repository.HistoryEvents
import com.sosauce.vanilla.ui.navigation.SettingsHome
import com.sosauce.vanilla.ui.screens.calculator.components.ButtonType
import com.sosauce.vanilla.ui.screens.calculator.components.CalcButton
import com.sosauce.vanilla.ui.screens.calculator.components.CalculationDisplay
import com.sosauce.vanilla.ui.screens.calculator.components.CuteButton
import com.sosauce.vanilla.utils.BACKSPACE
import com.sosauce.vanilla.utils.PARENTHESES
import com.sosauce.vanilla.utils.rememberResolvedSeparators
import com.sosauce.vanilla.utils.whichParenthesis

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalculatorScreenLandscape(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel,
    onHandleHistoryEvent: (HistoryEvents) -> Unit,
    onNavigate: (NavKey) -> Unit,
    onGotoHistory: () -> Unit
) {
    val showClearButton by rememberShowClearButton()
    val swapZeroAndDecimal by rememberSwapZeroAndDecimal()
    val decimalPreference by rememberDecimalSeparator()
    val groupingPreference by rememberGroupingSeparator()
    val localeDecimalChar = rememberResolvedSeparators(
        decimalPreference = decimalPreference,
        groupingPreference = groupingPreference
    ).first.toString()
    val saveErrorsToHistory by rememberSaveErrorsToHistory()
    val maxItemsToHistory by rememberHistoryMaxItems()
    val saveToHistory by rememberUseHistory()


    fun digit(text: String, token: Char) =
        KeypadCell(CalcButton(text, { viewModel.handleAction(CalcAction.AddToField(token)) }))

    val edit1 = if (showClearButton) {
        CalcButton("C", { viewModel.handleAction(CalcAction.ResetField) }, type = ButtonType.ACTION)
    } else {
        CalcButton(
            "(",
            { viewModel.handleAction(CalcAction.AddToField(Tokens.OPEN_PARENTHESIS)) },
            type = ButtonType.OPERATOR
        )
    }
    val edit2 = if (showClearButton) {
        CalcButton(
            PARENTHESES,
            { viewModel.handleAction(CalcAction.AddToField(viewModel.textFieldState.text.whichParenthesis())) },
            type = ButtonType.OPERATOR
        )
    } else {
        CalcButton(
            ")",
            { viewModel.handleAction(CalcAction.AddToField(Tokens.CLOSED_PARENTHESIS)) },
            type = ButtonType.OPERATOR
        )
    }

    val keypadRows: List<List<KeypadCell>> = listOf(
        listOf(
            KeypadCell(
                CalcButton(
                    "√",
                    { viewModel.handleAction(CalcAction.AddToField(Tokens.SQUARE_ROOT)) },
                    type = ButtonType.SPECIAL
                )
            ),
            digit("7", Tokens.SEVEN),
            digit("8", Tokens.EIGHT),
            digit("9", Tokens.NINE),
            KeypadCell(
                CalcButton(
                    "/",
                    { viewModel.handleAction(CalcAction.AddToField(Tokens.DIVIDE)) },
                    type = ButtonType.OPERATOR
                )
            ),
        ),
        listOf(
            KeypadCell(
                CalcButton(
                    "π",
                    { viewModel.handleAction(CalcAction.AddToField(Tokens.PI)) },
                    type = ButtonType.SPECIAL
                )
            ),
            digit("4", Tokens.FOUR),
            digit("5", Tokens.FIVE),
            digit("6", Tokens.SIX),
            KeypadCell(
                CalcButton(
                    "×",
                    { viewModel.handleAction(CalcAction.AddToField(Tokens.MULTIPLY)) },
                    type = ButtonType.OPERATOR
                )
            ),
        ),
        listOf(
            KeypadCell(
                CalcButton(
                    "!",
                    { viewModel.handleAction(CalcAction.AddToField(Tokens.FACTORIAL)) },
                    type = ButtonType.SPECIAL
                )
            ),
            digit("1", Tokens.ONE),
            digit("2", Tokens.TWO),
            digit("3", Tokens.THREE),
            KeypadCell(
                CalcButton(
                    "-",
                    { viewModel.handleAction(CalcAction.AddToField(Tokens.SUBTRACT)) },
                    type = ButtonType.OPERATOR
                )
            ),
        ),
        listOf(
            KeypadCell(
                CalcButton(
                    "%",
                    { viewModel.handleAction(CalcAction.AddToField(Tokens.MODULO)) },
                    type = ButtonType.SPECIAL
                )
            ),
            KeypadCell(
                CalcButton(
                    if (!swapZeroAndDecimal) "0" else localeDecimalChar,
                    { viewModel.handleAction(CalcAction.AddToField(if (!swapZeroAndDecimal) Tokens.ZERO else Tokens.DECIMAL)) }
                ),
                weight = 2f
            ),
            KeypadCell(
                CalcButton(
                    if (!swapZeroAndDecimal) localeDecimalChar else "0",
                    { viewModel.handleAction(CalcAction.AddToField(if (!swapZeroAndDecimal) Tokens.DECIMAL else Tokens.ZERO)) }
                )
            ),
            KeypadCell(
                CalcButton(
                    "+",
                    { viewModel.handleAction(CalcAction.AddToField(Tokens.ADD)) },
                    type = ButtonType.OPERATOR
                )
            ),
        ),
        listOf(
            KeypadCell(
                CalcButton(
                    "^",
                    { viewModel.handleAction(CalcAction.AddToField(Tokens.POWER)) },
                    type = ButtonType.OPERATOR
                )
            ),
            KeypadCell(edit2),
            KeypadCell(
                CalcButton(
                    BACKSPACE,
                    { viewModel.handleAction(CalcAction.Backspace) },
                    onLongClick = { viewModel.handleAction(CalcAction.ResetField) },
                    type = ButtonType.OTHER
                )
            ),
            KeypadCell(edit1),
            KeypadCell(CalcButton("=", onClick = {
                val operation = viewModel.textFieldState.text.toString()
                viewModel.handleAction(CalcAction.GetResult)
                val result = viewModel.evaluatedCalculation
                if (saveToHistory && operation != result) {
                    onHandleHistoryEvent(
                        HistoryEvents.AddCalculation(
                            operation = operation,
                            result = result,
                            maxHistoryItems = maxItemsToHistory,
                            saveErrors = saveErrorsToHistory
                        )
                    )
                }
            }, type = ButtonType.ACTION)),
        ),
    )

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { pv ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
                .padding(
                    vertical = 5.dp,
                    horizontal = 10.dp
                )
                .widthIn(max = MaxContentWidth),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            // display
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .widthIn(max = MaxDisplayWidth),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = { onNavigate(SettingsHome) },
                        shapes = IconButtonDefaults.shapes()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.settings_filled),
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                    IconButton(
                        onClick = onGotoHistory,
                        shapes = IconButtonDefaults.shapes()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.history_rounded),
                            contentDescription = stringResource(R.string.history)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(28.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    contentAlignment = Alignment.BottomStart
                ) {
                    CalculationDisplay(viewModel = viewModel)
                }
            }

            // keypad
            Column(
                modifier = Modifier
                    .weight(1.4f)
                    .fillMaxHeight()
                    .widthIn(max = MaxKeypadWidth),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                keypadRows.fastForEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.fastForEach { cell ->
                            CuteButton(
                                modifier = Modifier
                                    .weight(cell.weight)
                                    .fillMaxHeight()
                                    .heightIn(min = 42.dp)
                                    .widthIn(max = MaxKeypadButtonWidth),
                                text = cell.button.text,
                                onClick = cell.button.onClick,
                                onLongClick = cell.button.onLongClick,
                                rectangle = true,
                                buttonType = cell.button.type
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class KeypadCell(val button: CalcButton, val weight: Float = 1f)

private val MaxContentWidth = 1120.dp
private val MaxDisplayWidth = 420.dp
private val MaxKeypadWidth = 720.dp
private val MaxKeypadButtonWidth = 160.dp
