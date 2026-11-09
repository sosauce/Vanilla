@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.feature.settings.presentation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import com.sosauce.nekobites.components.settings.SelectableTile
import com.sosauce.nekobites.components.settings.SettingsGroup
import com.sosauce.nekobites.components.settings.SettingsPicker
import com.sosauce.nekobites.components.settings.SettingsStepper
import com.sosauce.nekobites.components.settings.SettingsSwitch
import com.sosauce.vanilla.R
import com.sosauce.vanilla.core.domain.DecimalSeparator
import com.sosauce.vanilla.core.domain.GroupingSeparator
import com.sosauce.vanilla.core.presentation.rememberDecimal
import com.sosauce.vanilla.core.presentation.rememberDecimalPrecision
import com.sosauce.vanilla.core.presentation.rememberDecimalSeparator
import com.sosauce.vanilla.core.presentation.rememberGroupingSeparator
import com.sosauce.vanilla.core.presentation.rememberResolvedSeparators
import com.sosauce.vanilla.core.presentation.rememberSeparatorSymbols

@Composable
fun ColumnScope.SettingsFormatting() {
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

    SettingsGroup(title = stringResource(R.string.formatting)) {
        SettingsSwitch(
            title = stringResource(R.string.decimal_formatting),
            checked = shouldFormat,
            onCheckedChange = { shouldFormat = it }
        )
    }

    AnimatedVisibility(shouldFormat) {
        SettingsGroup {
            SettingsStepper(
                title = stringResource(R.string.decimal_precision),
                description = stringResource(R.string.decimal_precision_desc),
                value = decimalPrecision,
                range = 0..100,
                onValueChange = { decimalPrecision = it }
            )
            SettingsPicker(title = stringResource(R.string.decimal_separator)) {
                items(decimalItems) { SeparatorTile(it) }
            }
            SettingsPicker(
                title = stringResource(R.string.grouping_separator),
                description = stringResource(R.string.grouping_separator_desc)
            ) {
                items(groupingItems) { SeparatorTile(it) }
            }
        }
    }
}

@Composable
private fun SeparatorTile(item: SeparatorItem) {
    SelectableTile(
        selected = item.isSelected,
        onClick = { if (item.enabled) item.onClick() },
        label = stringResource(item.text),
        modifier = Modifier.alpha(if (item.enabled) 1f else 0.38f)
    ) {
        Text(
            text = item.symbol.toString(),
            style = MaterialTheme.typography.titleLargeEmphasized,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private data class SeparatorItem(
    val onClick: () -> Unit,
    val symbol: Char,
    @param:StringRes val text: Int,
    val isSelected: Boolean,
    val enabled: Boolean
)
