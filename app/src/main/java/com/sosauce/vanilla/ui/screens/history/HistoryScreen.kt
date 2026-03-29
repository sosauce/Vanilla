@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.ui.screens.history

import android.content.ClipData
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.sosauce.vanilla.R
import com.sosauce.vanilla.data.datastore.rememberColoredOperators
import com.sosauce.vanilla.data.datastore.rememberDecimal
import com.sosauce.vanilla.data.datastore.rememberHistoryNewestFirst
import com.sosauce.vanilla.data.datastore.rememberUseHistory
import com.sosauce.vanilla.domain.model.Calculation
import com.sosauce.vanilla.domain.repository.HistoryEvents
import com.sosauce.vanilla.ui.screens.history.components.DeletionConfirmationDialog
import com.sosauce.vanilla.ui.screens.history.components.HistoryActionButtons
import com.sosauce.vanilla.ui.shared_components.CuteNavigationButton
import com.sosauce.vanilla.utils.formatExpression
import com.sosauce.vanilla.utils.formatNumber
import com.sosauce.vanilla.utils.isErrorMessage
import com.sosauce.vanilla.utils.isOperator
import com.sosauce.vanilla.utils.sort

@Composable
fun HistoryScreen(
    calculations: List<Calculation>,
    onEvents: (HistoryEvents) -> Unit,
    onPutBackToField: (String) -> Unit,
    onGotoMain: () -> Unit
) {
    val lazyState = rememberLazyListState()
    var isHistoryEnable by rememberUseHistory()
    val newestFirst by rememberHistoryNewestFirst()
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (showDeleteConfirmation) {
        DeletionConfirmationDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            onDelete = { onEvents(HistoryEvents.DeleteAllCalculation) }
        )
    }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CuteNavigationButton(
                    icon = R.drawable.arrow_up,
                    onNavigateUp = onGotoMain
                )
                HistoryActionButtons { showDeleteConfirmation = true }
            }
        }
    ) { pv ->
        if (!isHistoryEnable) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.history_not_enabled))
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = { isHistoryEnable = !isHistoryEnable },
                    shapes = ButtonDefaults.shapes()
                ) {
                    Text(stringResource(R.string.enable_history))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = pv,
                state = lazyState
            ) {

                if (calculations.isEmpty()) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.history_rounded),
                                contentDescription = null,
                                modifier = Modifier.size(70.dp)
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = stringResource(R.string.no_calc_found),
                                style = MaterialTheme.typography.headlineMediumEmphasized,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = stringResource(R.string.calc_empty),
                                style = MaterialTheme.typography.bodyMediumEmphasized,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    itemsIndexed(
                        items = calculations.sort(newestFirst),
                        key = { _, item -> item.id }
                    ) { index, item ->
                        CalculationItem(
                            calculation = item,
                            onEvents = onEvents,
                            onPutBackToField = onPutBackToField,
                            topDp = if (index == 0) 24.dp else 4.dp,
                            bottomDp = if (index == calculations.lastIndex) 24.dp else 4.dp,
                            modifier = Modifier.animateItem()
                        )
                    }
                }

            }
        }
    }

}

@Composable
private fun CalculationItem(
    calculation: Calculation,
    onEvents: (HistoryEvents) -> Unit,
    onPutBackToField: (String) -> Unit,
    topDp: Dp,
    bottomDp: Dp,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboard.current
    val localContentColor = LocalContentColor.current
    val shouldFormat by rememberDecimal()
    val coloredOperators by rememberColoredOperators()
    var actionsExpanded by remember { mutableStateOf(false) }
    val actions = listOf(
        HistoryAction(
            onClick = { onPutBackToField(calculation.operation) },
            icon = R.drawable.undo,
            text = R.string.put_field
        ),
        HistoryAction(
            onClick = {
                clipboardManager.nativeClipboard.setPrimaryClip(
                    ClipData.newPlainText(
                        "",
                        "${calculation.operation} = ${calculation.result}"
                    )
                )
            },
            icon = R.drawable.copy,
            text = R.string.copy
        ),
        HistoryAction(
            onClick = { onEvents(HistoryEvents.DeleteCalculation(calculation)) },
            icon = R.drawable.delete,
            text = R.string.delete,
            tint = MaterialTheme.colorScheme.error
        )
    )


    Card(
        onClick = { onPutBackToField(calculation.operation) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = RoundedCornerShape(
            topStart = topDp,
            topEnd = topDp,
            bottomEnd = bottomDp,
            bottomStart = bottomDp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = buildAnnotatedString {
                        calculation.operation.formatExpression(shouldFormat).forEach { char ->
                            if (coloredOperators && char.isOperator()) {
                                withStyle(SpanStyle(MaterialTheme.colorScheme.primary)) {
                                    append(char)
                                }
                            } else append(char)
                        }
                    },
                    style = MaterialTheme.typography.titleLargeEmphasized,
                    modifier = Modifier.basicMarquee()
                )
                Text(
                    text = calculation.result.formatNumber(shouldFormat),
                    style = MaterialTheme.typography.titleLargeEmphasized.copy(
                        color = if (calculation.result.isErrorMessage()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
                    ),
                    modifier = Modifier.basicMarquee()
                )
            }
            IconButton(
                onClick = { actionsExpanded = true },
                shapes = IconButtonDefaults.shapes()
            ) {
                Icon(
                    painter = painterResource(R.drawable.more_vert),
                    contentDescription = stringResource(R.string.more_actions)
                )

                DropdownMenuPopup(
                    expanded = actionsExpanded,
                    onDismissRequest = { actionsExpanded = false }
                ) {
                    DropdownMenuGroup(
                        shapes = MenuDefaults.groupShapes()
                    ) {
                        actions.fastForEachIndexed { index, action ->
                            DropdownMenuItem(
                                onClick = {
                                    action.onClick()
                                    actionsExpanded = false
                                },
                                text = {
                                    Text(
                                        text = stringResource(action.text),
                                        color = action.tint ?: localContentColor
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(action.icon),
                                        contentDescription = null,
                                        tint = action.tint ?: localContentColor
                                    )
                                },
                                shape = when (index) {
                                    0 -> MenuDefaults.leadingItemShape
                                    actions.lastIndex -> MenuDefaults.trailingItemShape
                                    else -> MenuDefaults.middleItemShape
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class HistoryAction(
    val onClick: () -> Unit,
    val icon: Int,
    val text: Int,
    val tint: Color? = null
)