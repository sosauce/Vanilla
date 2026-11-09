package com.sosauce.vanilla.feature.settings.presentation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.sosauce.nekobites.components.settings.SettingsGroup
import com.sosauce.nekobites.components.settings.SettingsSwitch
import com.sosauce.vanilla.R
import com.sosauce.vanilla.core.presentation.rememberShowOnLockScreen

@Composable
fun ColumnScope.SettingsMisc() {
    var showOnLockScreen by rememberShowOnLockScreen()

    SettingsGroup(title = stringResource(R.string.misc)) {
        SettingsSwitch(
            title = stringResource(R.string.show_ls),
            description = stringResource(R.string.show_ls_desc),
            checked = showOnLockScreen,
            onCheckedChange = { showOnLockScreen = it }
        )
    }
}
