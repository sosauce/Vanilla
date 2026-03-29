@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.sosauce.vanilla.R
import com.sosauce.vanilla.data.datastore.rememberHistoryMaxItems
import com.sosauce.vanilla.data.datastore.rememberSaveErrorsToHistory
import com.sosauce.vanilla.data.datastore.rememberUseHistory
import com.sosauce.vanilla.ui.screens.settings.components.SettingsDropdownMenu
import com.sosauce.vanilla.ui.screens.settings.components.SettingsSwitch
import com.sosauce.vanilla.ui.screens.settings.components.SettingsWithTitle
import com.sosauce.vanilla.ui.shared_components.CuteNavigationButton
import com.sosauce.vanilla.utils.selfAlignHorizontally

@Composable
fun SettingsHistory(
    onNavigateUp: () -> Unit,
) {

    val scrollState = rememberScrollState()
    var useHistory by rememberUseHistory()
    var historyMaxItems by rememberHistoryMaxItems()
    var saveErrorsToHistory by rememberSaveErrorsToHistory()
    val historyItemsChoice = listOf(
        10,
        20,
        50,
        100,
        200,
        500,
        1000,
        10000,
        Long.MAX_VALUE
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
                title = R.string.history
            ) {
                SettingsSwitch(
                    checked = useHistory,
                    onCheckedChange = { useHistory = !useHistory },
                    topDp = 24.dp,
                    bottomDp = 4.dp,
                    text = R.string.enable_history
                )
                SettingsSwitch(
                    checked = saveErrorsToHistory,
                    onCheckedChange = { saveErrorsToHistory = !saveErrorsToHistory },
                    topDp = 4.dp,
                    bottomDp = 4.dp,
                    text = R.string.save_errors
                )
                SettingsDropdownMenu(
                    value = historyMaxItems,
                    topDp = 4.dp,
                    bottomDp = 24.dp,
                    text = R.string.max_history_items
                ) {
                    historyItemsChoice.fastForEachIndexed { index, number ->
                        DropdownMenuItem(
                            onClick = { historyMaxItems = number },
                            text = { Text(if (number == Long.MAX_VALUE) stringResource(R.string.no_limit) else number.toString()) },
                            leadingIcon = {
                                RadioButton(
                                    selected = historyMaxItems == number,
                                    onClick = null
                                )
                            },
                            shape = when (index) {
                                0 -> MenuDefaults.leadingItemShape
                                historyItemsChoice.lastIndex -> MenuDefaults.trailingItemShape
                                else -> MenuDefaults.middleItemShape
                            }
                        )
                    }
                }
            }
        }
    }
}