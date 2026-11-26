@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.feature.history.presentation

import android.content.ClipData
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.nativeClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sosauce.nekobites.animations.AnimatedDrawable
import com.sosauce.nekobites.animations.AnimatedDrawableFile
import com.sosauce.nekobites.animations.AnimatedFab
import com.sosauce.nekobites.animations.bouncySpec
import com.sosauce.nekobites.components.NoXFound
import com.sosauce.nekobites.helpers.rememberInteractionSource
import com.sosauce.nekobites.utils.groupedShape
import com.sosauce.vanilla.R
import com.sosauce.vanilla.core.domain.isErrorMessage
import com.sosauce.vanilla.core.domain.isOperator
import com.sosauce.vanilla.core.presentation.rememberColoredOperators
import com.sosauce.vanilla.core.presentation.rememberHistoryNewestFirst
import com.sosauce.vanilla.core.presentation.rememberUseHistory
import com.sosauce.vanilla.feature.history.domain.Calculation
import com.sosauce.vanilla.feature.history.presentation.components.DeletionConfirmationDialog
import com.sosauce.vanilla.feature.history.presentation.components.HistoryActionButtons

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
    val sortedCalculations = remember(calculations, newestFirst) { calculations.sort(newestFirst) }

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedFab(
                    onClick = onGotoMain,
                    icon = R.drawable.arrow_up,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
                HistoryActionButtons { showDeleteConfirmation = true }
            }
        }
    ) { pv ->
        if (!isHistoryEnable) {
            HistoryDisabled(
                onEnable = { isHistoryEnable = true },
                contentPadding = pv
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = pv.calculateTopPadding() + 16.dp,
                    bottom = pv.calculateBottomPadding() + 16.dp
                ),
                state = lazyState
            ) {
                if (sortedCalculations.isEmpty()) {
                    item {
                        NoXFound(
                            headlineText = R.string.empty_history,
                            bodyText = R.string.empty_history_desc,
                            icon = R.drawable.history_rounded,
                            modifier = Modifier.fillParentMaxSize()
                        )
                    }
                } else {
                    itemsIndexed(
                        items = sortedCalculations,
                        key = { _, item -> item.id }
                    ) { index, item ->
                        CalculationItem(
                            calculation = item,
                            onEvents = onEvents,
                            onPutBackToField = onPutBackToField,
                            shape = groupedShape(
                                isFirst = index == 0,
                                isLast = index == sortedCalculations.lastIndex
                            ),
                            modifier = Modifier
                                .animateItem()
                                .padding(horizontal = 16.dp, vertical = 1.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryDisabled(
    onEnable: () -> Unit,
    contentPadding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(140.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialShapes.Cookie9Sided.toShape()
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.history_rounded),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(70.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.history_not_enabled),
                style = MaterialTheme.typography.headlineSmallEmphasized,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.history_not_enabled_desc),
                style = MaterialTheme.typography.bodyMediumEmphasized,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onEnable,
                shapes = ButtonDefaults.shapes(),
                modifier = Modifier.heightIn(ButtonDefaults.MediumContainerHeight),
                contentPadding = ButtonDefaults.contentPaddingFor(ButtonDefaults.MediumContainerHeight)
            ) {
                Text(
                    text = stringResource(R.string.enable_history),
                    style = MaterialTheme.typography.titleMediumEmphasized
                )
            }
        }
    }
}

@Composable
private fun CalculationItem(
    calculation: Calculation,
    onEvents: (HistoryEvents) -> Unit,
    onPutBackToField: (String) -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboard.current
    val coloredOperators by rememberColoredOperators()
    var actionsExpanded by remember { mutableStateOf(false) }
    val interactionSource = rememberInteractionSource()
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = bouncySpec()
    )
    val isError = calculation.result.isErrorMessage()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.shape = shape
                clip = true
            }
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(
                interactionSource = interactionSource,
                onClick = { onPutBackToField(calculation.operation) }
            )
            .padding(start = 20.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    calculation.operation.forEach { char ->
                        if (coloredOperators && char.isOperator()) {
                            withStyle(SpanStyle(MaterialTheme.colorScheme.primary)) {
                                append(char)
                            }
                        } else append(char)
                    }
                },
                style = MaterialTheme.typography.titleMediumEmphasized,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )
            Text(
                text = calculation.result,
                style = MaterialTheme.typography.headlineMediumEmphasized,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )
        }

        IconButton(
            onClick = { actionsExpanded = true },
            shapes = IconButtonDefaults.shapes()
        ) {
            AnimatedDrawable(
                drawable = AnimatedDrawableFile.MORE_VERT,
                atEnd = actionsExpanded
            )

            DropdownMenuPopup(
                expanded = actionsExpanded,
                onDismissRequest = { actionsExpanded = false }
            ) {
                DropdownMenuGroup(shapes = MenuDefaults.groupShapes()) {
                    DropdownMenuItem(
                        onClick = {
                            onPutBackToField(calculation.operation)
                            actionsExpanded = false
                        },
                        text = { Text(stringResource(R.string.put_field)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.undo),
                                contentDescription = null
                            )
                        },
                        shape = MenuDefaults.leadingItemShape
                    )
                    DropdownMenuItem(
                        onClick = {
                            clipboardManager.nativeClipboardManager.setPrimaryClip(
                                ClipData.newPlainText(
                                    "",
                                    "${calculation.operation} = ${calculation.result}"
                                )
                            )
                            actionsExpanded = false
                        },
                        text = { Text(stringResource(R.string.copy)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.copy),
                                contentDescription = null
                            )
                        },
                        shape = MenuDefaults.trailingItemShape
                    )
                }
                Spacer(Modifier.height(MenuDefaults.GroupSpacing))
                DropdownMenuGroup(shapes = MenuDefaults.groupShapes()) {
                    DropdownMenuItem(
                        onClick = {
                            onEvents(HistoryEvents.DeleteCalculation(calculation))
                            actionsExpanded = false
                        },
                        text = {
                            Text(
                                text = stringResource(R.string.delete),
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.delete),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        shape = MenuDefaults.standaloneItemShape
                    )
                }
            }
        }
    }
}
