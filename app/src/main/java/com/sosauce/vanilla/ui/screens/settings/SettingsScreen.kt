@file:OptIn(ExperimentalUuidApi::class)

package com.sosauce.vanilla.ui.screens.settings

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sosauce.vanilla.R
import com.sosauce.vanilla.ui.navigation.Screens
import com.sosauce.vanilla.ui.navigation.SettingsScreen
import com.sosauce.vanilla.ui.screens.settings.components.AboutCard
import com.sosauce.vanilla.ui.screens.settings.components.SettingsCategoryCard
import com.sosauce.vanilla.ui.shared_components.CuteNavigationButton
import com.sosauce.vanilla.utils.selfAlignHorizontally
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun SettingsScreen(
    onNavigate: (Screens) -> Unit
) {

    var screenToDisplay by rememberSaveable { mutableStateOf(SettingsScreen.SETTINGS) }


    // Mimic back behavior from navigation
    BackHandler {
        if (screenToDisplay != SettingsScreen.SETTINGS) {
            screenToDisplay = SettingsScreen.SETTINGS
        } else {
            onNavigate(Screens.MAIN)
        }
    }

    AnimatedContent(
        targetState = screenToDisplay,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { screen ->
        when (screen) {
            SettingsScreen.SETTINGS -> {
                SettingsPage(
                    onNavigate = onNavigate,
                    onNavigateSettings = { screenToDisplay = it }
                )
            }

            SettingsScreen.LOOK_AND_FEEL -> {
                SettingsLookAndFeel(
                    onNavigateUp = { screenToDisplay = SettingsScreen.SETTINGS }
                )
            }

            SettingsScreen.HISTORY -> {
                SettingsHistory(
                    onNavigateUp = { screenToDisplay = SettingsScreen.SETTINGS }
                )
            }

            SettingsScreen.FORMATTING -> {
                SettingsFormatting(
                    onNavigateUp = { screenToDisplay = SettingsScreen.SETTINGS }
                )
            }

            SettingsScreen.MISC -> {
                SettingsMisc(
                    onNavigateUp = { screenToDisplay = SettingsScreen.SETTINGS }
                )
            }
        }
    }
}

@Composable
private fun SettingsPage(
    onNavigate: (Screens) -> Unit,
    onNavigateSettings: (SettingsScreen) -> Unit
) {
    val listState = rememberLazyListState()
    val settingsCategories = arrayOf(
        SettingsCategory(
            name = R.string.look_and_feel,
            description = R.string.look_and_feel_desc,
            icon = R.drawable.palette,
            onNavigate = { onNavigateSettings(SettingsScreen.LOOK_AND_FEEL) }
        ),
        SettingsCategory(
            name = R.string.history,
            description = R.string.history_desc,
            icon = R.drawable.history_rounded,
            onNavigate = { onNavigateSettings(SettingsScreen.HISTORY) }
        ),
        SettingsCategory(
            name = R.string.formatting,
            description = R.string.formatting_desc,
            icon = R.drawable.formatting,
            onNavigate = { onNavigateSettings(SettingsScreen.FORMATTING) }
        ),
        SettingsCategory(
            name = R.string.misc,
            description = R.string.misc_desc,
            icon = R.drawable.more_horiz,
            onNavigate = { onNavigateSettings(SettingsScreen.MISC) }
        )
    )

    Scaffold(
        bottomBar = {
            CuteNavigationButton(
                modifier = Modifier
                    .padding(start = 15.dp)
                    .navigationBarsPadding()
                    .selfAlignHorizontally(Alignment.Start),
                onNavigateUp = { onNavigate(Screens.MAIN) }
            )
        }
    ) { pv ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = pv,
            state = listState
        ) {
            item {
                AboutCard()
                Spacer(Modifier.height(20.dp))
            }
            itemsIndexed(
                items = settingsCategories,
                key = { _, category -> category.id }
            ) { index, category ->
                SettingsCategoryCard(
                    icon = category.icon,
                    name = category.name,
                    description = category.description,
                    topDp = if (index == 0) 24.dp else 4.dp,
                    bottomDp = if (index == settingsCategories.lastIndex) 24.dp else 4.dp,
                    onNavigate = category.onNavigate
                )
            }
        }
    }
}

@Immutable
private data class SettingsCategory(
    val id: String = Uuid.random().toString(),
    val name: Int,
    val description: Int,
    val icon: Int,
    val onNavigate: () -> Unit
)