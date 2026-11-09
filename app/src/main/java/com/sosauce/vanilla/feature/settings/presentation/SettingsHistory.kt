package com.sosauce.vanilla.feature.settings.presentation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.sosauce.nekobites.components.settings.SettingsGroup
import com.sosauce.nekobites.components.settings.SettingsSwitch
import com.sosauce.vanilla.R
import com.sosauce.vanilla.core.presentation.rememberHistoryMaxItems
import com.sosauce.vanilla.core.presentation.rememberSaveErrorsToHistory
import com.sosauce.vanilla.core.presentation.rememberUseHistory
import com.sosauce.vanilla.feature.settings.presentation.components.SettingsInput

@Composable
fun ColumnScope.SettingsHistory() {
    var useHistory by rememberUseHistory()
    var historyMaxItems by rememberHistoryMaxItems()
    var saveErrorsToHistory by rememberSaveErrorsToHistory()

    SettingsGroup(title = stringResource(R.string.history)) {
        SettingsSwitch(
            title = stringResource(R.string.enable_history),
            checked = useHistory,
            onCheckedChange = { useHistory = it }
        )
        SettingsSwitch(
            title = stringResource(R.string.save_errors),
            checked = saveErrorsToHistory,
            onCheckedChange = { saveErrorsToHistory = it }
        )
        SettingsInput(
            title = stringResource(R.string.max_history_items),
            value = historyMaxItems,
            range = 10..1_000_000,
            onNewValue = { historyMaxItems = it }
        )
    }
}
