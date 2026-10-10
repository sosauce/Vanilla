package com.sosauce.vanilla.feature.settings.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.sosauce.vanilla.R
import com.sosauce.vanilla.core.data.preferences.rememberAppTheme
import com.sosauce.vanilla.core.data.preferences.rememberColoredOperators
import com.sosauce.vanilla.core.data.preferences.rememberShowClearButton
import com.sosauce.vanilla.core.data.preferences.rememberSwapZeroAndDecimal
import com.sosauce.vanilla.core.data.preferences.rememberUseButtonsAnimation
import com.sosauce.vanilla.core.data.preferences.rememberUseSystemFont
import com.sosauce.vanilla.core.data.preferences.rememberVibration
import com.sosauce.vanilla.feature.settings.presentation.components.LazyRowWithScrollButton
import com.sosauce.vanilla.feature.settings.presentation.components.SettingsSelector
import com.sosauce.vanilla.feature.settings.presentation.components.SettingsSwitch
import com.sosauce.vanilla.feature.settings.presentation.components.SettingsWithTitle
import com.sosauce.vanilla.core.designsystem.theme.nunitoFontFamily
import com.sosauce.vanilla.core.domain.CuteTheme
import com.sosauce.vanilla.utils.anyDarkColorScheme
import com.sosauce.vanilla.utils.anyLightColorScheme

@Composable
fun SettingsLookAndFeel() {
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

    val themeItems = listOf(
        ThemeItem(
            onClick = { theme = CuteTheme.SYSTEM },
            backgroundColor = if (isSystemDark) anyDark.surfaceContainer else anyLight.surfaceContainer,
            iconColor = if (isSystemDark) anyDark.onSurface else anyLight.onSurface,
            text = R.string.system,
            isSelected = theme == CuteTheme.SYSTEM,
            icon = R.drawable.system_theme
        ),
        ThemeItem(
            onClick = { theme = CuteTheme.DARK },
            backgroundColor = anyDark.surfaceContainer,
            iconColor = anyDark.onSurface,
            text = R.string.dark_mode,
            isSelected = theme == CuteTheme.DARK,
            icon = R.drawable.dark_mode
        ),
        ThemeItem(
            onClick = { theme = CuteTheme.LIGHT },
            backgroundColor = anyLight.surfaceContainer,
            iconColor = anyLight.onSurface,
            text = R.string.light_mode,
            icon = R.drawable.light_mode,
            isSelected = theme == CuteTheme.LIGHT
        ),
        ThemeItem(
            onClick = { theme = CuteTheme.AMOLED },
            backgroundColor = Color.Black,
            iconColor = Color.White,
            text = R.string.amoled_mode,
            icon = R.drawable.amoled,
            isSelected = theme == CuteTheme.AMOLED
        )
    )
    val fontItems = listOf(
        FontItem(
            onClick = { useSystemFont = false },
            isSelected = !useSystemFont,
            icon = R.drawable.match_case,
            text = R.string.default_text
        ),
        FontItem(
            onClick = { useSystemFont = true },
            isSelected = useSystemFont,
            icon = R.drawable.system_font,
            text = R.string.system
        )
    )
    Column {
        SettingsWithTitle(
            title = R.string.appearance
        ) {
            Card(
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp,
                    bottomStart = 2.dp,
                    bottomEnd = 2.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 1.dp)
            ) {
                LazyRowWithScrollButton(
                    items = themeItems
                ) { item ->
                    SettingsSelector(
                        onClick = item.onClick,
                        icon = item.icon,
                        text = item.text,
                        isSelected = item.isSelected
                    )
                }
            }
            Card(
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
                shape = RoundedCornerShape(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 1.dp)
            ) {
                LazyRowWithScrollButton(
                    items = fontItems
                ) { item ->
                    SettingsSelector(
                        onClick = item.onClick,
                        icon = item.icon,
                        text = item.text,
                        isSelected = item.isSelected
                    )
                }
            }
            SettingsSwitch(
                checked = useButtonsAnimation,
                onCheckedChange = { useButtonsAnimation = !useButtonsAnimation },
                topDp = 2.dp,
                bottomDp = 2.dp,
                text = R.string.buttons_anim
            )
            SettingsSwitch(
                checked = coloredOperators,
                onCheckedChange = { coloredOperators = !coloredOperators },
                topDp = 2.dp,
                bottomDp = 2.dp,
                text = R.string.colored_operatos
            )
            SettingsSwitch(
                checked = swapZeroAndDecimal,
                onCheckedChange = { swapZeroAndDecimal = !swapZeroAndDecimal },
                topDp = 2.dp,
                bottomDp = 2.dp,
                text = R.string.swap_zero_and_decimal
            )
            SettingsSwitch(
                checked = useHapticFeedback,
                onCheckedChange = { useHapticFeedback = !useHapticFeedback },
                topDp = 2.dp,
                bottomDp = 2.dp,
                text = R.string.haptic_feedback
            )
            SettingsSwitch(
                checked = showClearButton,
                onCheckedChange = { showClearButton = !showClearButton },
                topDp = 2.dp,
                bottomDp = 24.dp,
                text = R.string.show_clear_button,
                optionalDescription = R.string.clear_button_desc
            )
        }
    }
}

data class FontItem(
    val onClick: () -> Unit,
    val icon: Int,
    val text: Int,
    val isSelected: Boolean
)
data class ThemeItem(
    val onClick: () -> Unit,
    val backgroundColor: Color,
    val iconColor: Color = Color.White,
    val text: Int,
    val icon: Int,
    val isSelected: Boolean
)