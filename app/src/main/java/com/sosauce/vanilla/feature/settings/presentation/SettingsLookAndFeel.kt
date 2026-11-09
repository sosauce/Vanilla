package com.sosauce.vanilla.feature.settings.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sosauce.nekobites.components.settings.SelectableTile
import com.sosauce.nekobites.components.settings.SettingsGroup
import com.sosauce.nekobites.components.settings.SettingsPicker
import com.sosauce.nekobites.components.settings.SettingsSwitch
import com.sosauce.vanilla.R
import com.sosauce.vanilla.core.designsystem.theme.anyDarkColorScheme
import com.sosauce.vanilla.core.designsystem.theme.anyLightColorScheme
import com.sosauce.vanilla.core.domain.CuteTheme
import com.sosauce.vanilla.core.presentation.rememberAppTheme
import com.sosauce.vanilla.core.presentation.rememberColoredOperators
import com.sosauce.vanilla.core.presentation.rememberShowClearButton
import com.sosauce.vanilla.core.presentation.rememberSwapZeroAndDecimal
import com.sosauce.vanilla.core.presentation.rememberUseButtonsAnimation
import com.sosauce.vanilla.core.presentation.rememberUseSystemFont
import com.sosauce.vanilla.core.presentation.rememberVibration

@Composable
fun ColumnScope.SettingsLookAndFeel() {
    var theme by rememberAppTheme()
    var useSystemFont by rememberUseSystemFont()
    var useButtonsAnimation by rememberUseButtonsAnimation()
    var useHapticFeedback by rememberVibration()
    var showClearButton by rememberShowClearButton()
    var coloredOperators by rememberColoredOperators()
    var swapZeroAndDecimal by rememberSwapZeroAndDecimal()
    val anyDark = anyDarkColorScheme()
    val anyLight = anyLightColorScheme()
    val isSystemDark = isSystemInDarkTheme()

    val themes = listOf(
        ThemeOption(
            value = CuteTheme.SYSTEM,
            label = R.string.system,
            icon = R.drawable.system_theme,
            background = if (isSystemDark) anyDark.surfaceContainerHighest else anyLight.surfaceContainerHighest,
            iconColor = if (isSystemDark) anyDark.onSurface else anyLight.onSurface
        ),
        ThemeOption(
            value = CuteTheme.DARK,
            label = R.string.dark_mode,
            icon = R.drawable.dark_mode,
            background = anyDark.surfaceContainerHighest,
            iconColor = anyDark.onSurface
        ),
        ThemeOption(
            value = CuteTheme.LIGHT,
            label = R.string.light_mode,
            icon = R.drawable.light_mode,
            background = anyLight.surfaceContainerHighest,
            iconColor = anyLight.onSurface
        ),
        ThemeOption(
            value = CuteTheme.AMOLED,
            label = R.string.amoled_mode,
            icon = R.drawable.amoled,
            background = Color.Black,
            iconColor = Color.White
        )
    )

    SettingsGroup(title = stringResource(R.string.appearance)) {
        SettingsPicker {
            items(themes) { option ->
                SelectableTile(
                    selected = theme == option.value,
                    onClick = { theme = option.value },
                    label = stringResource(option.label),
                    containerColor = option.background
                ) {
                    Icon(
                        painter = painterResource(option.icon),
                        contentDescription = null,
                        tint = option.iconColor
                    )
                }
            }
        }
        SettingsPicker {
            item {
                FontTile(
                    selected = !useSystemFont,
                    onClick = { useSystemFont = false },
                    icon = R.drawable.match_case,
                    label = R.string.default_text
                )
            }
            item {
                FontTile(
                    selected = useSystemFont,
                    onClick = { useSystemFont = true },
                    icon = R.drawable.system_font,
                    label = R.string.system
                )
            }
        }
        SettingsSwitch(
            title = stringResource(R.string.buttons_anim),
            checked = useButtonsAnimation,
            onCheckedChange = { useButtonsAnimation = it }
        )
        SettingsSwitch(
            title = stringResource(R.string.colored_operatos),
            checked = coloredOperators,
            onCheckedChange = { coloredOperators = it }
        )
        SettingsSwitch(
            title = stringResource(R.string.swap_zero_and_decimal),
            checked = swapZeroAndDecimal,
            onCheckedChange = { swapZeroAndDecimal = it }
        )
        SettingsSwitch(
            title = stringResource(R.string.haptic_feedback),
            checked = useHapticFeedback,
            onCheckedChange = { useHapticFeedback = it }
        )
        SettingsSwitch(
            title = stringResource(R.string.show_clear_button),
            description = stringResource(R.string.clear_button_desc),
            checked = showClearButton,
            onCheckedChange = { showClearButton = it }
        )
    }
}

@Composable
private fun FontTile(
    selected: Boolean,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    @StringRes label: Int
) {
    SelectableTile(
        selected = selected,
        onClick = onClick,
        label = stringResource(label)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(26.dp)
        )
    }
}

private data class ThemeOption(
    val value: String,
    @param:StringRes val label: Int,
    @param:DrawableRes val icon: Int,
    val background: Color,
    val iconColor: Color
)
