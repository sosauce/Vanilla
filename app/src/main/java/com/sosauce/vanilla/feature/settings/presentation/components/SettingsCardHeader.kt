package com.sosauce.vanilla.feature.settings.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SettingsCardHeader(
    text: Int
) {
    Text(
        text = stringResource(text),
        style = MaterialTheme.typography.bodyMediumEmphasized.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.padding(
            start = 20.dp,
            top = 10.dp
        )
    )
}