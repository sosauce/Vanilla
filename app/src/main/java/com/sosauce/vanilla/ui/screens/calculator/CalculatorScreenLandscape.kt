@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.ui.screens.calculator

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.zIndex
import com.sosauce.vanilla.R
import com.sosauce.vanilla.data.actions.CalcAction
import com.sosauce.vanilla.data.calculator.Tokens
import com.sosauce.vanilla.data.datastore.rememberHistoryMaxItems
import com.sosauce.vanilla.data.datastore.rememberSaveErrorsToHistory
import com.sosauce.vanilla.data.datastore.rememberShowClearButton
import com.sosauce.vanilla.data.datastore.rememberUseHistory
import com.sosauce.vanilla.domain.repository.HistoryEvents
import com.sosauce.vanilla.ui.navigation.Screens
import com.sosauce.vanilla.ui.screens.calculator.components.ButtonType
import com.sosauce.vanilla.ui.screens.calculator.components.CalcButton
import com.sosauce.vanilla.ui.screens.calculator.components.CalculationDisplay
import com.sosauce.vanilla.ui.screens.calculator.components.CuteButton
import com.sosauce.vanilla.ui.screens.history.HistoryViewModel
import com.sosauce.vanilla.utils.BACKSPACE
import com.sosauce.vanilla.utils.PARENTHESES
import com.sosauce.vanilla.utils.whichParenthesis
import java.text.DecimalFormatSymbols

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalculatorScreenLandscape(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel,
    historyViewModel: HistoryViewModel,
    onNavigate: (Screens) -> Unit,
    onGotoHistory: () -> Unit
) {
    val showClearButton by rememberShowClearButton()
    val localeDecimalChar =
        remember { DecimalFormatSymbols.getInstance().decimalSeparator.toString() }
    val saveErrorsToHistory by rememberSaveErrorsToHistory()
    val maxItemsToHistory by rememberHistoryMaxItems()
    val saveToHistory by rememberUseHistory()

    val row1 = listOf(
        CalcButton(
            text = "√",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.SQUARE_ROOT)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "π",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.PI)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "9",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.NINE)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "8",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.EIGHT)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "7",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.SEVEN)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "^",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.POWER)) },
            type = ButtonType.OPERATOR
        ),
        if (showClearButton) {
            CalcButton(
                text = PARENTHESES,
                onClick = {
                    viewModel.handleAction(
                        CalcAction.AddToField(
                            viewModel.textFieldState.text.whichParenthesis()
                        )
                    )
                },
                type = ButtonType.OPERATOR
            )
        } else {
            CalcButton(
                text = "(",
                onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.OPEN_PARENTHESIS)) },
                type = ButtonType.OPERATOR
            )
        },
        if (showClearButton) {
            CalcButton(
                text = "C",
                onClick = { viewModel.handleAction(CalcAction.ResetField) },
                type = ButtonType.ACTION
            )
        } else {
            CalcButton(
                text = ")",
                onClick = {
                    viewModel.handleAction(
                        CalcAction.AddToField(Tokens.CLOSED_PARENTHESIS)
                    )
                },
                type = ButtonType.OPERATOR
            )
        }
    )
    val row2 = listOf(
        CalcButton(
            text = "%",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.MODULO)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "3",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.THREE)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "4",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.FOUR)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "5",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.FIVE)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "6",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.SIX)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "+",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.ADD)) },
            type = ButtonType.OPERATOR
        ),
        CalcButton(
            text = "-",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.SUBTRACT)) },
            type = ButtonType.OPERATOR
        ),
        CalcButton(
            text = BACKSPACE,
            onClick = { viewModel.handleAction(CalcAction.Backspace) },
            onLongClick = { viewModel.handleAction(CalcAction.ResetField) },
            type = ButtonType.ACTION
        )
    )
    val row3 = listOf(
        CalcButton(
            text = "!",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.FACTORIAL)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "2",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.TWO)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "1",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.ONE)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "0",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.ZERO)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = localeDecimalChar,
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.DECIMAL)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "×",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.MULTIPLY)) },
            type = ButtonType.OPERATOR
        ),
        CalcButton(
            text = "/",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.DIVIDE)) },
            type = ButtonType.OPERATOR
        ),
        CalcButton(
            text = "=",
            onClick = {
                val operation = viewModel.textFieldState.text.toString()
                viewModel.handleAction(CalcAction.GetResult)
                val result = viewModel.evaluatedCalculation

                if (saveToHistory && operation != result) {
                    historyViewModel.onEvent(
                        HistoryEvents.AddCalculation(
                            operation = operation,
                            result = result,
                            maxHistoryItems = maxItemsToHistory,
                            saveErrors = saveErrorsToHistory
                        )
                    )
                }
            },
            type = ButtonType.ACTION
        )
    )

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { pv ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .zIndex(1f) //history doesnt click otherwise ?
            ) {
                IconButton(
                    onClick = { onNavigate(Screens.SETTINGS) },
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

            Column {
                CalculationDisplay(
                    viewModel = viewModel,
                    onNavigate = onNavigate
                )


                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(9.dp),
                ) {
                    val rows = listOf(row1, row2, row3)

                    rows.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(9.dp)
                        ) {
                            row.fastForEach { button ->
                                CuteButton(
                                    text = button.text,
                                    onClick = button.onClick,
                                    onLongClick = button.onLongClick,
                                    rectangle = true,
                                    buttonType = button.type
                                )
                            }
                        }
                    }
                }
            }
        }


    }
}