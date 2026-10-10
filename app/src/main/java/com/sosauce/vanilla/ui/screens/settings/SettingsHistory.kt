@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.sosauce.vanilla.R
import com.sosauce.vanilla.core.data.preferences.rememberHistoryMaxItems
import com.sosauce.vanilla.core.data.preferences.rememberSaveErrorsToHistory
import com.sosauce.vanilla.core.data.preferences.rememberUseHistory
import com.sosauce.vanilla.ui.screens.settings.components.SettingsInput
import com.sosauce.vanilla.ui.screens.settings.components.SettingsSwitch
import com.sosauce.vanilla.ui.screens.settings.components.SettingsWithTitle

@Composable
fun SettingsHistory() {

    var useHistory by rememberUseHistory()
    var historyMaxItems by rememberHistoryMaxItems()
    var saveErrorsToHistory by rememberSaveErrorsToHistory()

    Column {
        SettingsWithTitle(
            title = R.string.history
        ) {
            SettingsSwitch(
                checked = useHistory,
                onCheckedChange = { useHistory = !useHistory },
                topDp = 24.dp,
                bottomDp = 2.dp,
                text = R.string.enable_history
            )
            SettingsSwitch(
                checked = saveErrorsToHistory,
                onCheckedChange = { saveErrorsToHistory = !saveErrorsToHistory },
                topDp = 2.dp,
                bottomDp = 2.dp,
                text = R.string.save_errors
            )
            SettingsInput(
                value = historyMaxItems,
                minValue = 10,
                maxValue = 1_000_000,
                onNewValue = { historyMaxItems = it },
                topDp = 2.dp,
                bottomDp = 24.dp,
                text = R.string.max_history_items
            )
        }
    }
}