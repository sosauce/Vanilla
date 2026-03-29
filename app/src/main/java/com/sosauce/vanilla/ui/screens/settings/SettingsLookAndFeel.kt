package com.sosauce.vanilla.ui.screens.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sosauce.vanilla.R
import com.sosauce.vanilla.data.datastore.rememberAppTheme
import com.sosauce.vanilla.data.datastore.rememberColoredOperators
import com.sosauce.vanilla.data.datastore.rememberShowClearButton
import com.sosauce.vanilla.data.datastore.rememberUseButtonsAnimation
import com.sosauce.vanilla.data.datastore.rememberUseSystemFont
import com.sosauce.vanilla.data.datastore.rememberVibration
import com.sosauce.vanilla.ui.screens.settings.components.FontSelector
import com.sosauce.vanilla.ui.screens.settings.components.LazyRowWithScrollButton
import com.sosauce.vanilla.ui.screens.settings.components.SettingsSwitch
import com.sosauce.vanilla.ui.screens.settings.components.SettingsWithTitle
import com.sosauce.vanilla.ui.screens.settings.components.ThemeItem
import com.sosauce.vanilla.ui.screens.settings.components.ThemeSelector
import com.sosauce.vanilla.ui.shared_components.CuteNavigationButton
import com.sosauce.vanilla.utils.CuteTheme
import com.sosauce.vanilla.utils.anyDarkColorScheme
import com.sosauce.vanilla.utils.anyLightColorScheme
import com.sosauce.vanilla.utils.selfAlignHorizontally

@Composable
fun SettingsLookAndFeel(
    onNavigateUp: () -> Unit,
) {
    val scrollState = rememberScrollState()
    var theme by rememberAppTheme()
    var useSystemFont by rememberUseSystemFont()
    var useButtonsAnimation by rememberUseButtonsAnimation()
    var useHapticFeedback by rememberVibration()
    var showClearButton by rememberShowClearButton()
    var coloredOperators by rememberColoredOperators()
    val themeItems = listOf(
        ThemeItem(
            onClick = { theme = CuteTheme.SYSTEM },
            backgroundColor = if (isSystemInDarkTheme()) anyDarkColorScheme().background else anyLightColorScheme().background,
            text = stringResource(R.string.follow_sys),
            isSelected = theme == CuteTheme.SYSTEM,
            iconAndTint = Pair(
                painterResource(R.drawable.system_theme),
                if (isSystemInDarkTheme()) anyDarkColorScheme().onBackground else anyLightColorScheme().onBackground
            )
        ),
        ThemeItem(
            onClick = { theme = CuteTheme.DARK },
            backgroundColor = anyDarkColorScheme().background,
            text = stringResource(R.string.dark_mode),
            isSelected = theme == CuteTheme.DARK,
            iconAndTint = Pair(
                painterResource(R.drawable.dark_mode),
                anyDarkColorScheme().onBackground
            )
        ),
        ThemeItem(
            onClick = { theme = CuteTheme.LIGHT },
            backgroundColor = anyLightColorScheme().background,
            text = stringResource(R.string.light_mode),
            isSelected = theme == CuteTheme.LIGHT,
            iconAndTint = Pair(
                painterResource(R.drawable.light_mode),
                anyLightColorScheme().onBackground
            )
        ),
        ThemeItem(
            onClick = { theme = CuteTheme.AMOLED },
            backgroundColor = Color.Black,
            text = stringResource(R.string.amoled_mode),
            isSelected = theme == CuteTheme.AMOLED,
            iconAndTint = Pair(painterResource(R.drawable.amoled), Color.White)
        )
    )
    val fontItems = listOf(
        FontItem(
            onClick = { useSystemFont = false },
            fontStyle = FontStyle.DEFAULT,
            borderColor = if (!useSystemFont) MaterialTheme.colorScheme.primary else Color.Transparent,
            text = {
                Text(
                    text = "Tt",
                    fontWeight = FontWeight.ExtraBold
                )
            },
        ),
        FontItem(
            onClick = { useSystemFont = true },
            fontStyle = FontStyle.SYSTEM,
            borderColor = if (useSystemFont) MaterialTheme.colorScheme.primary else Color.Transparent,
            text = {
                Text(
                    text = "Tt",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        )
    )

    Scaffold(
        bottomBar = {
            CuteNavigationButton(
                modifier = Modifier
                    .padding(start = 15.dp)
                    .navigationBarsPadding()
                    .selfAlignHorizontally(Alignment.Start),
                onNavigateUp = onNavigateUp
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(pv)
        ) {
            SettingsWithTitle(
                title = R.string.theme
            ) {
                Card(
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                ) {
                    LazyRowWithScrollButton(
                        items = themeItems
                    ) { item ->
                        ThemeSelector(
                            onClick = item.onClick,
                            backgroundColor = item.backgroundColor,
                            text = item.text,
                            isThemeSelected = item.isSelected,
                            icon = {
                                Icon(
                                    painter = item.iconAndTint.first,
                                    contentDescription = null,
                                    tint = item.iconAndTint.second,
                                )
                            }
                        )
                    }
                }
            }
            SettingsWithTitle(
                title = R.string.font
            ) {
                Card(
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                ) {
                    LazyRowWithScrollButton(
                        items = fontItems
                    ) { item ->
                        FontSelector(
                            item
                        )
                    }
                }
            }
            SettingsWithTitle(
                title = R.string.ui
            ) {
                SettingsSwitch(
                    checked = useButtonsAnimation,
                    onCheckedChange = { useButtonsAnimation = !useButtonsAnimation },
                    topDp = 24.dp,
                    bottomDp = 4.dp,
                    text = R.string.buttons_anim
                )
                SettingsSwitch(
                    checked = coloredOperators,
                    onCheckedChange = { coloredOperators = !coloredOperators },
                    topDp = 4.dp,
                    bottomDp = 4.dp,
                    text = R.string.colored_operatos
                )
                SettingsSwitch(
                    checked = useHapticFeedback,
                    onCheckedChange = { useHapticFeedback = !useHapticFeedback },
                    topDp = 4.dp,
                    bottomDp = 4.dp,
                    text = R.string.haptic_feedback
                )
                SettingsSwitch(
                    checked = showClearButton,
                    onCheckedChange = { showClearButton = !showClearButton },
                    topDp = 4.dp,
                    bottomDp = 24.dp,
                    text = R.string.show_clear_button,
                    optionalDescription = R.string.clear_button_desc
                )
            }
        }
    }
}

@Immutable
data class FontItem(
    val onClick: () -> Unit,
    val fontStyle: FontStyle,
    val borderColor: Color,
    val text: @Composable () -> Unit
)

enum class FontStyle {
    DEFAULT,
    SYSTEM
}