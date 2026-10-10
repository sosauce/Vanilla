@file:OptIn(ExperimentalUuidApi::class)

package com.sosauce.vanilla.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation3.runtime.NavKey
import com.sosauce.nekobites.animations.AnimatedFab
import com.sosauce.vanilla.R
import com.sosauce.vanilla.app.navigation.Formatting
import com.sosauce.vanilla.app.navigation.HistorySettings
import com.sosauce.vanilla.app.navigation.LookAndFeel
import com.sosauce.vanilla.app.navigation.Misc
import com.sosauce.vanilla.feature.settings.presentation.components.AboutCard
import com.sosauce.vanilla.feature.settings.presentation.components.SettingsCategoryCard
import com.sosauce.vanilla.utils.selfAlignHorizontally
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun SettingsHomeScreen(
    onNavigate: (NavKey) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        bottomBar = {
            AnimatedFab(
                onClick = onBack,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .navigationBarsPadding()
                    .selfAlignHorizontally(Alignment.Start),
                icon = R.drawable.back_arrow,
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        }
    ) { paddingValues ->
        SettingsPage(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            onNavigateSettings = onNavigate
        )
    }
}

@Composable
fun SettingsDetailScaffold(
    onBack: () -> Unit,
    topDp: Dp = 24.dp,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            AnimatedFab(
                onClick = onBack,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .navigationBarsPadding()
                    .selfAlignHorizontally(Alignment.Start),
                icon = R.drawable.back_arrow,
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(top = topDp)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsPage(
    modifier: Modifier = Modifier,
    onNavigateSettings: (NavKey) -> Unit
) {
    val settingsCategories = listOf(
        SettingsCategory(
            name = R.string.look_and_feel,
            description = R.string.look_and_feel_desc,
            icon = R.drawable.palette,
            onNavigate = { onNavigateSettings(LookAndFeel) }
        ),
        SettingsCategory(
            name = R.string.history,
            description = R.string.history_desc,
            icon = R.drawable.history_rounded,
            onNavigate = { onNavigateSettings(HistorySettings) }
        ),
        SettingsCategory(
            name = R.string.formatting,
            description = R.string.formatting_desc,
            icon = R.drawable.formatting,
            onNavigate = { onNavigateSettings(Formatting) }
        ),
        SettingsCategory(
            name = R.string.misc,
            description = R.string.misc_desc,
            icon = R.drawable.more_horiz,
            onNavigate = { onNavigateSettings(Misc) }
        )
    )

    Column(modifier = modifier) {
        AboutCard()
        Spacer(Modifier.height(20.dp))
        settingsCategories.fastForEachIndexed { index, category ->
            SettingsCategoryCard(
                icon = category.icon,
                name = category.name,
                description = category.description,
                topDp = if (index == 0) 24.dp else 2.dp,
                bottomDp = if (index == settingsCategories.lastIndex) 24.dp else 2.dp,
                onNavigate = category.onNavigate
            )
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
