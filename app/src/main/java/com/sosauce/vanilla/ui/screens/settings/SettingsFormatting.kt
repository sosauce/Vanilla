@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.ui.screens.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.SelectableDropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import com.sosauce.nekobites.components.Spacer
import com.sosauce.vanilla.R
import com.sosauce.vanilla.data.datastore.rememberDecimal
import com.sosauce.vanilla.data.datastore.rememberDecimalPrecision
import com.sosauce.vanilla.data.datastore.rememberDecimalSeparator
import com.sosauce.vanilla.data.datastore.rememberGroupingSeparator
import com.sosauce.vanilla.ui.screens.settings.components.LazyRowWithScrollButton
import com.sosauce.vanilla.ui.screens.settings.components.SettingsInput
import com.sosauce.vanilla.ui.screens.settings.components.SettingsSwitch
import com.sosauce.vanilla.ui.screens.settings.components.SettingsWithTitle
import com.sosauce.vanilla.utils.DecimalSeparator
import com.sosauce.vanilla.utils.GroupingSeparator
import com.sosauce.vanilla.utils.formatNumber
import com.sosauce.vanilla.utils.rememberResolvedSeparators
import com.sosauce.vanilla.utils.rememberSeparatorSymbols

@Composable
fun SettingsFormatting() {
    var shouldFormat by rememberDecimal()
    var decimalPrecision by rememberDecimalPrecision()
    var decimalSeparator by rememberDecimalSeparator()
    var groupingSeparator by rememberGroupingSeparator()
    val symbols = rememberSeparatorSymbols()
    val (resolvedDecimal, resolvedGrouping) = rememberResolvedSeparators(
        decimalPreference = decimalSeparator,
        groupingPreference = groupingSeparator
    )

    val decimalItems = listOf(
        SeparatorItem(
            onClick = { decimalSeparator = DecimalSeparator.SYSTEM },
            symbol = symbols.decimalSeparator,
            text = R.string.separator_system_default,
            isSelected = decimalSeparator == DecimalSeparator.SYSTEM,
            enabled = true
        ),
        SeparatorItem(
            onClick = { decimalSeparator = DecimalSeparator.DOT },
            symbol = '.',
            text = R.string.separator_dot,
            isSelected = decimalSeparator == DecimalSeparator.DOT,
            enabled = '.' != resolvedGrouping || decimalSeparator == DecimalSeparator.DOT
        ),
        SeparatorItem(
            onClick = { decimalSeparator = DecimalSeparator.COMMA },
            symbol = ',',
            text = R.string.separator_comma,
            isSelected = decimalSeparator == DecimalSeparator.COMMA,
            enabled = ',' != resolvedGrouping || decimalSeparator == DecimalSeparator.COMMA
        )
    )
    val groupingItems = listOf(
        SeparatorItem(
            onClick = { groupingSeparator = GroupingSeparator.SYSTEM },
            symbol = symbols.groupingSeparator,
            text = R.string.separator_system_default,
            isSelected = groupingSeparator == GroupingSeparator.SYSTEM,
            enabled = true
        ),
        SeparatorItem(
            onClick = { groupingSeparator = GroupingSeparator.COMMA },
            symbol = ',',
            text = R.string.separator_comma,
            isSelected = groupingSeparator == GroupingSeparator.COMMA,
            enabled = ',' != resolvedDecimal || groupingSeparator == GroupingSeparator.COMMA
        ),
        SeparatorItem(
            onClick = { groupingSeparator = GroupingSeparator.DOT },
            symbol = '.',
            text = R.string.separator_dot,
            isSelected = groupingSeparator == GroupingSeparator.DOT,
            enabled = '.' != resolvedDecimal || groupingSeparator == GroupingSeparator.DOT
        ),
        SeparatorItem(
            onClick = { groupingSeparator = GroupingSeparator.SPACE },
            symbol = ' ',
            text = R.string.separator_space,
            isSelected = groupingSeparator == GroupingSeparator.SPACE,
            enabled = ' ' != resolvedDecimal || groupingSeparator == GroupingSeparator.SPACE
        ),
        SeparatorItem(
            onClick = { groupingSeparator = GroupingSeparator.APOSTROPHE },
            symbol = '\'',
            text = R.string.separator_apostrophe,
            isSelected = groupingSeparator == GroupingSeparator.APOSTROPHE,
            enabled = '\'' != resolvedDecimal || groupingSeparator == GroupingSeparator.APOSTROPHE
        ),
        SeparatorItem(
            onClick = { groupingSeparator = GroupingSeparator.NONE },
            symbol = '∅',
            text = R.string.separator_none,
            isSelected = groupingSeparator == GroupingSeparator.NONE,
            enabled = true
        )
    )

    Column {
        SettingsWithTitle(
            title = R.string.formatting
        ) {
            SettingsSwitch(
                checked = shouldFormat,
                onCheckedChange = { shouldFormat = !shouldFormat },
                topDp = 24.dp,
                bottomDp = 24.dp,
                text = R.string.decimal_formatting
            )
            Spacer(10.dp)

            AnimatedVisibility(shouldFormat) {
                Column {
                    SettingsInput(
                        value = decimalPrecision,
                        minValue = 0,
                        maxValue = 100,
                        onNewValue = { decimalPrecision = it },
                        topDp = 24.dp,
                        bottomDp = 2.dp,
                        text = R.string.decimal_precision,
                        optionalDescription = R.string.decimal_precision_desc
                    )

                    Card(
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
                        shape = RoundedCornerShape(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 1.dp)
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.decimal_separator),
                                modifier = Modifier.padding(start = 15.dp, top = 15.dp, end = 15.dp)
                            )
                            LazyRowWithScrollButton(
                                items = decimalItems
                            ) { item ->
                                SeparatorSelector(
                                    onClick = item.onClick,
                                    symbol = item.symbol,
                                    text = item.text,
                                    isSelected = item.isSelected,
                                    enabled = item.enabled
                                )
                            }
                        }
                    }
                    Card(
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
                        shape = RoundedCornerShape(
                            topStart = 2.dp,
                            topEnd = 2.dp,
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 1.dp)
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.grouping_separator),
                                modifier = Modifier.padding(start = 15.dp, top = 15.dp, end = 15.dp)
                            )
                            Text(
                                text = stringResource(R.string.grouping_separator_desc),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 15.dp)
                            )
                            LazyRowWithScrollButton(
                                items = groupingItems
                            ) { item ->
                                SeparatorSelector(
                                    onClick = item.onClick,
                                    symbol = item.symbol,
                                    text = item.text,
                                    isSelected = item.isSelected,
                                    enabled = item.enabled
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

private data class SeparatorItem(
    val onClick: () -> Unit,
    val symbol: Char,
    val text: Int,
    val isSelected: Boolean,
    val enabled: Boolean
)

@Composable
private fun SeparatorSelector(
    onClick: () -> Unit,
    symbol: Char,
    text: Int,
    isSelected: Boolean,
    enabled: Boolean
) {

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent,
    )
    val textColor by animateColorAsState(
        targetValue = if (!enabled) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
        else if (isSelected) MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.onSurface,
    )

    Column(
        modifier = Modifier
            .padding(10.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .size(50.dp)
                .clip(MaterialShapes.Cookie9Sided.toShape())
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = MaterialShapes.Cookie9Sided.toShape()
                )
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = symbol.toString(),
                style = MaterialTheme.typography.titleLargeEmphasized.copy(
                    color = contentColorFor(MaterialTheme.colorScheme.surfaceContainerHigh)
                )
            )
        }
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.bodyMediumEmphasized.copy(
                color = textColor
            ),
            modifier = Modifier.padding(
                horizontal = 5.dp
            )
        )
    }
}
