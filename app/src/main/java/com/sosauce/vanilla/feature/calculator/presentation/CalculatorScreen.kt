@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.sosauce.vanilla.feature.calculator.presentation

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.navigation3.runtime.NavKey
import com.sosauce.vanilla.R
import com.sosauce.vanilla.feature.calculator.presentation.CalculatorAction
import com.sosauce.vanilla.feature.calculator.domain.Tokens
import com.sosauce.vanilla.core.data.preferences.rememberDecimalSeparator
import com.sosauce.vanilla.core.data.preferences.rememberGroupingSeparator
import com.sosauce.vanilla.core.data.preferences.rememberHistoryMaxItems
import com.sosauce.vanilla.core.data.preferences.rememberSaveErrorsToHistory
import com.sosauce.vanilla.core.data.preferences.rememberShowClearButton
import com.sosauce.vanilla.core.data.preferences.rememberSwapZeroAndDecimal
import com.sosauce.vanilla.core.data.preferences.rememberUseHistory
import com.sosauce.vanilla.feature.history.presentation.HistoryEvents
import com.sosauce.vanilla.app.navigation.SettingsHome
import com.sosauce.vanilla.feature.calculator.presentation.components.ButtonType
import com.sosauce.vanilla.feature.calculator.presentation.components.CalcButton
import com.sosauce.vanilla.feature.calculator.presentation.components.CalculationDisplay
import com.sosauce.vanilla.feature.calculator.presentation.components.CuteButton
import com.sosauce.vanilla.ui.screens.history.HistoryViewModel
import com.sosauce.vanilla.core.domain.BACKSPACE
import com.sosauce.vanilla.core.domain.PARENTHESES
import com.sosauce.vanilla.utils.rememberResolvedSeparators
import com.sosauce.vanilla.utils.whichParenthesis
import kotlinx.coroutines.CoroutineScope


@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel,
    onHandleHistoryEvent: (HistoryEvents)-> Unit,
    onNavigate: (NavKey) -> Unit,
    onUpdateDragAmount: (Float) -> Unit,
    onDragStopped: suspend CoroutineScope.(Float) -> Unit
) {
    val decimalPreference by rememberDecimalSeparator()
    val groupingPreference by rememberGroupingSeparator()
    val localeDecimalChar = rememberResolvedSeparators(
        decimalPreference = decimalPreference,
        groupingPreference = groupingPreference
    ).first.toString()
    val showClearButton by rememberShowClearButton()
    val saveErrorsToHistory by rememberSaveErrorsToHistory()
    val maxItemsToHistory by rememberHistoryMaxItems()
    val saveToHistory by rememberUseHistory()
    val swapZeroAndDecimal by rememberSwapZeroAndDecimal()

    val row1 = listOf(
        CalcButton(
            text = "!",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.FACTORIAL)) },
            rectangle = true,
            type = ButtonType.SPECIAL
        ),
        CalcButton(
            text = "%",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.MODULO)) },
            rectangle = true,
            type = ButtonType.SPECIAL
        ),
        CalcButton(
            text = "√",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.SQUARE_ROOT)) },
            rectangle = true,
            type = ButtonType.SPECIAL
        ),
        CalcButton(
            text = "π",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.PI)) },
            rectangle = true,
            type = ButtonType.SPECIAL
        )
    )
    val row2 = listOf(
        if (showClearButton) {
            CalcButton(
                text = "C",
                onClick = { viewModel.handleAction(CalculatorAction.ResetField) },
                type = ButtonType.ACTION
            )
        } else {
            CalcButton(
                text = "(",
                onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.OPEN_PARENTHESIS)) },
                type = ButtonType.OPERATOR
            )
        },
        if (showClearButton) {
            CalcButton(
                text = PARENTHESES,
                onClick = {
                    viewModel.handleAction(
                        CalculatorAction.AddToField(
                            viewModel.textFieldState.text.toString().whichParenthesis()
                        )
                    )
                },
                type = ButtonType.OPERATOR
            )
        } else {
            CalcButton(
                text = ")",
                onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.CLOSED_PARENTHESIS)) },
                type = ButtonType.OPERATOR
            )
        },
        CalcButton(
            text = "^",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.POWER)) },
            type = ButtonType.OPERATOR
        ),
        CalcButton(
            text = "/",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.DIVIDE)) },
            type = ButtonType.OPERATOR
        )
    )
    val row3 = listOf(
        CalcButton(
            text = "7",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.SEVEN)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "8",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.EIGHT)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "9",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.NINE)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "×",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.MULTIPLY)) },
            type = ButtonType.OPERATOR
        )
    )
    val row4 = listOf(
        CalcButton(
            text = "4",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.FOUR)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "5",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.FIVE)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "6",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.SIX)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "-",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.SUBTRACT)) },
            type = ButtonType.OPERATOR
        )
    )
    val row5 = listOf(
        CalcButton(
            text = "1",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.ONE)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "2",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.TWO)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "3",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.THREE)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "+",
            onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.ADD)) },
            type = ButtonType.OPERATOR
        )
    )
    val row6 = listOf(
        if (!swapZeroAndDecimal) {
            CalcButton(
                text = "0",
                onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.ZERO)) },
                type = ButtonType.OTHER
            )
        } else {
            CalcButton(
                text = localeDecimalChar,
                onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.DECIMAL)) },
                type = ButtonType.OTHER
            )
        },
        if (swapZeroAndDecimal) {
            CalcButton(
                text = "0",
                onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.ZERO)) },
                type = ButtonType.OTHER
            )
        } else {
            CalcButton(
                text = localeDecimalChar,
                onClick = { viewModel.handleAction(CalculatorAction.AddToField(Tokens.DECIMAL)) },
                type = ButtonType.OTHER
            )
        },
        CalcButton(
            text = BACKSPACE,
            onClick = { viewModel.handleAction(CalculatorAction.Backspace) },
            onLongClick = { viewModel.handleAction(CalculatorAction.ResetField) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "=",
            onClick = {
                val operation = viewModel.textFieldState.text.toString()
                viewModel.handleAction(CalculatorAction.GetResult)
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
            },
            type = ButtonType.ACTION
        )
    )
    val dragState = rememberDraggableState { dragAmount ->
        onUpdateDragAmount(dragAmount)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)),
                title = {
                    BottomSheetDefaults.DragHandle(
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .draggable(
                                state = dragState,
                                orientation = Orientation.Vertical,
                                onDragStopped = onDragStopped
                            )
                    )
                },
                actions = {
                    IconButton(
                        onClick = { onNavigate(SettingsHome) },
                        shapes = IconButtonDefaults.shapes()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.settings_filled),
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                }
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = PortraitMaxContentWidth)
                    .weight(1f, fill = true)
            ) {
                CalculationDisplay(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = viewModel
                )
            }
            Spacer(Modifier.height(5.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = PortraitMaxContentWidth),
                verticalArrangement = Arrangement.spacedBy(9.dp),
            ) {
                val rows = listOf(row1, row2, row3, row4, row5, row6)
                rows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        row.fastForEach { button ->
                            key(button.text) {
                                CuteButton(
                                    modifier = Modifier.weight(1f),
                                    text = button.text,
                                    onClick = button.onClick,
                                    onLongClick = button.onLongClick,
                                    rectangle = button.rectangle,
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

private val PortraitMaxContentWidth = 560.dp
