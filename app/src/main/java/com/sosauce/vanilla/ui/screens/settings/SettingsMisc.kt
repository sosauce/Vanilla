package com.sosauce.vanilla.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sosauce.vanilla.R
import com.sosauce.vanilla.data.datastore.rememberShowOnLockScreen
import com.sosauce.vanilla.ui.screens.settings.components.SettingsSwitch
import com.sosauce.vanilla.ui.screens.settings.components.SettingsWithTitle
import com.sosauce.vanilla.ui.shared_components.CuteNavigationButton
import com.sosauce.vanilla.utils.selfAlignHorizontally

@Composable
fun SettingsMisc(
    onNavigateUp: () -> Unit
) {
    val scrollState = rememberScrollState()
    var showOnLockScreen by rememberShowOnLockScreen()

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
}