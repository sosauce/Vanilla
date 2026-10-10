package com.sosauce.vanilla.feature.calculator.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.InterceptPlatformTextInput
import kotlinx.coroutines.awaitCancellation

// https://stackoverflow.com/a/78720287
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DisableSoftKeyboard(
    content: @Composable () -> Unit
) {
    InterceptPlatformTextInput(
        interceptor = { _, _ ->
            awaitCancellation()
        },
        content = content
    )
}