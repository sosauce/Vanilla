@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.sosauce.vanilla.ui.screens.calculator

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.sosauce.vanilla.R
import com.sosauce.vanilla.data.actions.CalcAction
import com.sosauce.vanilla.data.calculator.Tokens
import com.sosauce.vanilla.data.datastore.rememberHistoryMaxItems
import com.sosauce.vanilla.data.datastore.rememberSaveErrorsToHistory
import com.sosauce.vanilla.data.datastore.rememberShowClearButton
import com.sosauce.vanilla.data.datastore.rememberSwapZeroAndDecimal
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.DecimalFormatSymbols


@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel,
    historyViewModel: HistoryViewModel,
    onNavigate: (Screens) -> Unit,
    onUpdateDragAmount: (Float) -> Unit,
    onDragStopped: suspend CoroutineScope.(Float) -> Unit
) {
    val localeDecimalChar =
        remember { DecimalFormatSymbols.getInstance().decimalSeparator.toString() }
    val showClearButton by rememberShowClearButton()
    val saveErrorsToHistory by rememberSaveErrorsToHistory()
    val maxItemsToHistory by rememberHistoryMaxItems()
    val saveToHistory by rememberUseHistory()
    val swapZeroAndDecimal by rememberSwapZeroAndDecimal()

    val row1 = listOf(
        CalcButton(
            text = "!",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.FACTORIAL)) },
            rectangle = true,
            type = ButtonType.SPECIAL
        ),
        CalcButton(
            text = "%",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.MODULO)) },
            rectangle = true,
            type = ButtonType.SPECIAL
        ),
        CalcButton(
            text = "√",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.SQUARE_ROOT)) },
            rectangle = true,
            type = ButtonType.SPECIAL
        ),
        CalcButton(
            text = "π",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.PI)) },
            rectangle = true,
            type = ButtonType.SPECIAL
        )
    )
    val row2 = listOf(
        if (showClearButton) {
            CalcButton(
                text = "C",
                onClick = { viewModel.handleAction(CalcAction.ResetField) },
                type = ButtonType.ACTION
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
                text = PARENTHESES,
                onClick = {
                    viewModel.handleAction(
                        CalcAction.AddToField(
                            viewModel.textFieldState.text.toString().whichParenthesis()
                        )
                    )
                },
                type = ButtonType.OPERATOR
            )
        } else {
            CalcButton(
                text = ")",
                onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.CLOSED_PARENTHESIS)) },
                type = ButtonType.OPERATOR
            )
        },
        CalcButton(
            text = "^",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.POWER)) },
            type = ButtonType.OPERATOR
        ),
        CalcButton(
            text = "/",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.DIVIDE)) },
            type = ButtonType.OPERATOR
        )
    )
    val row3 = listOf(
        CalcButton(
            text = "7",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.SEVEN)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "8",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.EIGHT)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "9",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.NINE)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "×",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.MULTIPLY)) },
            type = ButtonType.OPERATOR
        )
    )
    val row4 = listOf(
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
            text = "-",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.SUBTRACT)) },
            type = ButtonType.OPERATOR
        )
    )
    val row5 = listOf(
        CalcButton(
            text = "1",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.ONE)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "2",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.TWO)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "3",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.THREE)) },
            type = ButtonType.OTHER
        ),
        CalcButton(
            text = "+",
            onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.ADD)) },
            type = ButtonType.OPERATOR
        )
    )
    val row6 = listOf(
        if (!swapZeroAndDecimal) {
            CalcButton(
                text = "0",
                onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.ZERO)) },
                type = ButtonType.OTHER
            )
        } else {
            CalcButton(
                text = localeDecimalChar,
                onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.DECIMAL)) },
                type = ButtonType.OTHER
            )
        },
        if (swapZeroAndDecimal) {
            CalcButton(
                text = "0",
                onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.ZERO)) },
                type = ButtonType.OTHER
            )
        } else {
            CalcButton(
                text = localeDecimalChar,
                onClick = { viewModel.handleAction(CalcAction.AddToField(Tokens.DECIMAL)) },
                type = ButtonType.OTHER
            )
        },
        CalcButton(
            text = BACKSPACE,
            onClick = { viewModel.handleAction(CalcAction.Backspace) },
            onLongClick = { viewModel.handleAction(CalcAction.ResetField) },
            type = ButtonType.OTHER
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
//                    IconButton(
//                        onClick = {},
//                        shapes = IconButtonDefaults.shapes()
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.history_rounded),
//                            contentDescription = stringResource(R.string.history),
//                            tint = MaterialTheme.colorScheme.onBackground
//                        )
//                    }

                    IconButton(
                        onClick = { onNavigate(Screens.SETTINGS) },
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
                .padding(horizontal = 10.dp)
                .fillMaxSize()
                .padding(pv),
            verticalArrangement = Arrangement.Bottom
        ) {
            CalculationDisplay(
                modifier = Modifier.weight(1f),
                viewModel = viewModel,
                onNavigate = onNavigate
            )
            Spacer(Modifier.height(5.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
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
