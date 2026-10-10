package com.sosauce.vanilla.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.sosauce.vanilla.R
import com.sosauce.vanilla.core.data.preferences.rememberShowOnLockScreen
import com.sosauce.vanilla.ui.screens.settings.components.SettingsSwitch
import com.sosauce.vanilla.ui.screens.settings.components.SettingsWithTitle

@Composable
fun SettingsMisc() {
    var showOnLockScreen by rememberShowOnLockScreen()

    Column {
        SettingsWithTitle(
            title = R.string.misc
        ) {
            SettingsSwitch(
                checked = showOnLockScreen,
                onCheckedChange = { showOnLockScreen = !showOnLockScreen },
                topDp = 24.dp,
                bottomDp = 24.dp,
                text = R.string.show_ls,
                optionalDescription = R.string.show_ls_desc
            )
        }
    }
}