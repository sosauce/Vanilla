package com.sosauce.vanilla.data.datastore

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sosauce.vanilla.utils.CuteTheme


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data object PreferencesKeys {
    val THEME = stringPreferencesKey("theme")
    val BUTTON_VIBRATION_ENABLED = booleanPreferencesKey("button_vibration_enabled")
    val DECIMAL_FORMATTING = booleanPreferencesKey("decimal_formatting")
    val ENABLE_HISTORY = booleanPreferencesKey("enable_history")
    val HISTORY_MAX_ITEMS = longPreferencesKey("HISTORY_MAX_ITEMS")
    val SAVE_ERRORS_TO_HISTORY = booleanPreferencesKey("SAVE_ERRORS_TO_HISTORY")
    val USE_BUTTONS_ANIMATIONS = booleanPreferencesKey("use_buttons_animation")
    val USE_SYSTEM_FONT = booleanPreferencesKey("use_system_font")
    val SHOW_CLEAR_BUTTON = booleanPreferencesKey("show_clear_button")
    val DECIMAL_PRECISION = intPreferencesKey("DECIMAL_PRECISION")
    val SHOW_ON_LOCKSCREEN = booleanPreferencesKey("SHOW_ON_LOCKSCREEN")
    val HISTORY_NEWEST_FIRST = booleanPreferencesKey("HISTORY_NEWEST_FIRST")
    val COLORED_OPERATORS = booleanPreferencesKey("COLORED_OPERATORS")
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
        key = PreferencesKeys.HISTORY_MAX_ITEMS,
        defaultValue = Long.MAX_VALUE
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

fun getDecimalPrecision(context: Context) = getPreference(
    key = PreferencesKeys.DECIMAL_PRECISION,
    defaultValue = 1000,
    context = context
)


