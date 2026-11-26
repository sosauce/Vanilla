package com.sosauce.vanilla.feature.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation3.runtime.NavKey
import com.sosauce.nekobites.animations.AnimatedFab
import com.sosauce.nekobites.components.settings.AboutCard
import com.sosauce.nekobites.components.settings.ScrollPage
import com.sosauce.nekobites.components.settings.SettingsCard
import com.sosauce.nekobites.components.settings.SettingsEntry
import com.sosauce.vanilla.R
import com.sosauce.vanilla.app.navigation.Formatting
import com.sosauce.vanilla.app.navigation.HistorySettings
import com.sosauce.vanilla.app.navigation.LookAndFeel
import com.sosauce.vanilla.app.navigation.Misc
import com.sosauce.vanilla.core.domain.GITHUB_RELEASES
import com.sosauce.vanilla.core.presentation.selfAlignHorizontally

@Composable
fun SettingsHomeScreen(
    onNavigate: (NavKey) -> Unit,
    onBack: () -> Unit
) {
    SettingsScaffold(onBack = onBack) { paddingValues ->
        SettingsHome(
            onNavigate = onNavigate,
            contentPadding = paddingValues
        )
    }
}

@Composable
fun SettingsDetailScaffold(
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    SettingsScaffold(onBack = onBack) { paddingValues ->
        ScrollPage(paddingValues, content)
    }
}

@Composable
private fun SettingsScaffold(
    onBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
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
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        },
        content = content
    )
}

@Composable
private fun SettingsHome(
    onNavigate: (NavKey) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val palettes = listOf(
        colors.primary,
        colors.secondary,
        colors.tertiary
    )
    val screens = listOf(
        SettingsEntry(
            icon = R.drawable.palette,
            name = stringResource(R.string.look_and_feel),
            description = stringResource(R.string.look_and_feel_desc),
            onClick = { onNavigate(LookAndFeel) }
        ),
        SettingsEntry(
            icon = R.drawable.history_rounded,
            name = stringResource(R.string.history),
            description = stringResource(R.string.history_desc),
            onClick = { onNavigate(HistorySettings) }
        ),
        SettingsEntry(
            icon = R.drawable.formatting,
            name = stringResource(R.string.formatting),
            description = stringResource(R.string.formatting_desc),
            onClick = { onNavigate(Formatting) }
        ),
        SettingsEntry(
            icon = R.drawable.more_horiz,
            name = stringResource(R.string.misc),
            description = stringResource(R.string.misc_desc),
            onClick = { onNavigate(Misc) }
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(bottom = contentPadding.calculateBottomPadding() + 16.dp)
            .padding(horizontal = 16.dp)
    ) {
        AboutCard(
            icon = R.drawable.calculator,
            appName = stringResource(R.string.app_name),
            updateLink = GITHUB_RELEASES
        )
        Spacer(Modifier.height(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            screens.fastForEachIndexed { index, screen ->
                SettingsCard(
                    entry = screen,
                    isFirst = index == 0,
                    isLast = index == screens.lastIndex,
                    badgeContainer = palettes[index % palettes.size]
                )
            }
        }
    }
}
