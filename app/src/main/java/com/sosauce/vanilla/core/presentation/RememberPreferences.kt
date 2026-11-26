package com.sosauce.vanilla.core.presentation

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sosauce.vanilla.core.data.preferences.PreferencesKeys
import com.sosauce.vanilla.core.data.preferences.PreferencesKeys.HISTORY_MAX_ITEMS
import com.sosauce.vanilla.core.data.preferences.dataStore
import com.sosauce.vanilla.core.domain.CuteTheme
import com.sosauce.vanilla.core.domain.DecimalSeparator
import com.sosauce.vanilla.core.domain.GroupingSeparator
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun <T> rememberPreference(
    key: Preferences.Key<T>,
    defaultValue: T,
): MutableState<T> {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val state by remember {
        context.dataStore.data
            .map { it[key] ?: defaultValue }
    }.collectAsStateWithLifecycle(initialValue = defaultValue)

    return remember(state) {
        object : MutableState<T> {
            override var value: T
                get() = state
                set(value) {
                    coroutineScope.launch {
                        context.dataStore.edit {
                            it[key] = value
                        }
                    }
                }

            override fun component1() = value
            override fun component2(): (T) -> Unit = { value = it }
        }
    }
}

@Composable
fun rememberVibration() =
    rememberPreference(
        key = PreferencesKeys.BUTTON_VIBRATION_ENABLED,
        defaultValue = false
    )

@Composable
fun rememberAppTheme() =
    rememberPreference(
        key = PreferencesKeys.THEME,
        defaultValue = CuteTheme.SYSTEM
    )

@Composable
fun rememberDecimal() =
    rememberPreference(
        key = PreferencesKeys.DECIMAL_FORMATTING,
        defaultValue = false
    )

@Composable
fun rememberUseHistory() =
    rememberPreference(
        key = PreferencesKeys.ENABLE_HISTORY,
        defaultValue = true
    )

@Composable
fun rememberUseButtonsAnimation() =
    rememberPreference(
        key = PreferencesKeys.USE_BUTTONS_ANIMATIONS,
        defaultValue = true
    )

@Composable
fun rememberUseSystemFont() =
    rememberPreference(
        key = PreferencesKeys.USE_SYSTEM_FONT,
        defaultValue = false
    )

@Composable
fun rememberShowClearButton() =
    rememberPreference(
        key = PreferencesKeys.SHOW_CLEAR_BUTTON,
        defaultValue = true
    )

@Composable
fun rememberHistoryMaxItems() =
    rememberPreference(
        key = HISTORY_MAX_ITEMS,
        defaultValue = Int.MAX_VALUE
    )

@Composable
fun rememberSaveErrorsToHistory() =
    rememberPreference(
        key = PreferencesKeys.SAVE_ERRORS_TO_HISTORY,
        defaultValue = false
    )

@Composable
fun rememberDecimalPrecision() =
    rememberPreference(
        key = PreferencesKeys.DECIMAL_PRECISION,
        defaultValue = 100
    )

@Composable
fun rememberShowOnLockScreen() =
    rememberPreference(
        key = PreferencesKeys.SHOW_ON_LOCKSCREEN,
        defaultValue = false
    )

@Composable
fun rememberHistoryNewestFirst() =
    rememberPreference(
        key = PreferencesKeys.HISTORY_NEWEST_FIRST,
        defaultValue = true
    )

@Composable
fun rememberColoredOperators() =
    rememberPreference(
        key = PreferencesKeys.COLORED_OPERATORS,
        defaultValue = true
    )

@Composable
fun rememberSwapZeroAndDecimal() =
    rememberPreference(
        key = PreferencesKeys.SWAP_ZERO_AND_DECIMAL,
        defaultValue = false
    )

@Composable
fun rememberDecimalSeparator() =
    rememberPreference(
        key = PreferencesKeys.DECIMAL_SEPARATOR,
        defaultValue = DecimalSeparator.SYSTEM
    )

@Composable
fun rememberGroupingSeparator() =
    rememberPreference(
        key = PreferencesKeys.GROUPING_SEPARATOR,
        defaultValue = GroupingSeparator.SYSTEM
    )

@Composable
fun rememberIsLandscape(): Boolean {
    val config = LocalConfiguration.current

    return remember(config.orientation) {
        config.orientation == Configuration.ORIENTATION_LANDSCAPE
    }
}
