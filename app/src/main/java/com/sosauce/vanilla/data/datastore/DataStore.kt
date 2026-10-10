package com.sosauce.vanilla.data.datastore

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sosauce.vanilla.data.datastore.PreferencesKeys.HISTORY_MAX_ITEMS
import com.sosauce.vanilla.core.domain.CuteTheme
import com.sosauce.vanilla.core.domain.DecimalSeparator
import com.sosauce.vanilla.core.domain.GroupingSeparator
import kotlinx.coroutines.flow.first

private const val DATA_STORE_NAME = "settings"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATA_STORE_NAME,
    produceMigrations = {
        listOf(HistoryMaxItemsMigration)
    }
)

data object PreferencesKeys {
    val THEME = stringPreferencesKey("theme")
    val BUTTON_VIBRATION_ENABLED = booleanPreferencesKey("button_vibration_enabled")
    val DECIMAL_FORMATTING = booleanPreferencesKey("decimal_formatting")
    val ENABLE_HISTORY = booleanPreferencesKey("enable_history")
    val HISTORY_MAX_ITEMS = intPreferencesKey("HISTORY_MAX_ITEMS_INT")
    val SAVE_ERRORS_TO_HISTORY = booleanPreferencesKey("SAVE_ERRORS_TO_HISTORY")
    val USE_BUTTONS_ANIMATIONS = booleanPreferencesKey("use_buttons_animation")
    val USE_SYSTEM_FONT = booleanPreferencesKey("use_system_font")
    val SHOW_CLEAR_BUTTON = booleanPreferencesKey("show_clear_button")
    val DECIMAL_PRECISION = intPreferencesKey("DECIMAL_PRECISION")
    val SHOW_ON_LOCKSCREEN = booleanPreferencesKey("SHOW_ON_LOCKSCREEN")
    val HISTORY_NEWEST_FIRST = booleanPreferencesKey("HISTORY_NEWEST_FIRST")
    val COLORED_OPERATORS = booleanPreferencesKey("COLORED_OPERATORS")
    val SWAP_ZERO_AND_DECIMAL = booleanPreferencesKey("SWAP_ZERO_AND_DECIMAL")
    val DECIMAL_SEPARATOR = stringPreferencesKey("decimal_separator")
    val GROUPING_SEPARATOR = stringPreferencesKey("grouping_separator")
}


private val LEGACY_HISTORY_MAX_ITEMS_LONG = longPreferencesKey("HISTORY_MAX_ITEMS")

private object HistoryMaxItemsMigration : DataMigration<Preferences> {
    override suspend fun cleanUp() = Unit

    override suspend fun migrate(currentData: Preferences): Preferences {

        val legacyKey = currentData[LEGACY_HISTORY_MAX_ITEMS_LONG] ?: return currentData

        return currentData.toMutablePreferences().apply {
            if (legacyKey > 1_000_000) {
                this[HISTORY_MAX_ITEMS] = 1_000_000
            }
            remove(LEGACY_HISTORY_MAX_ITEMS_LONG)

        }
    }

    override suspend fun shouldMigrate(currentData: Preferences): Boolean =
        currentData[LEGACY_HISTORY_MAX_ITEMS_LONG] is Long
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

fun getDecimalPrecision(context: Context) = getPreference(
    key = PreferencesKeys.DECIMAL_PRECISION,
    defaultValue = 1000,
    context = context
)



