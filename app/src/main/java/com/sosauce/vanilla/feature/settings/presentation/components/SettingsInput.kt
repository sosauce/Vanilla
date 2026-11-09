@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.feature.settings.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sosauce.nekobites.components.settings.SettingsClickable
import com.sosauce.vanilla.R

@Composable
fun SettingsInput(
    title: String,
    value: Int,
    range: IntRange,
    onNewValue: (Int) -> Unit,
    description: String? = null
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        val focusRequester = remember { FocusRequester() }
        val textFieldState = rememberTextFieldState(value.toString())
        val typedValue = remember(textFieldState.text) { textFieldState.text.toString().toIntOrNull() ?: 0 }
        val isError = remember(typedValue) { typedValue !in range }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.enter_new_value)) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable._123),
                    contentDescription = null
                )
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                    shapes = ButtonDefaults.shapes()
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onNewValue(typedValue)
                        showDialog = false
                    },
                    shapes = ButtonDefaults.shapes(),
                    enabled = !isError
                ) {
                    Text(stringResource(R.string.apply))
                }
            },
            text = {
                OutlinedTextField(
                    state = textFieldState,
                    modifier = Modifier.focusRequester(focusRequester),
                    isError = isError,
                    inputTransformation = NumbersOnlyTransformation,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    supportingText = {
                        if (isError) {
                            Text(
                                text = stringResource(
                                    R.string.enter_value_between_range,
                                    range.first,
                                    range.last
                                )
                            )
                        }
                    }
                )
            }
        )
    }

    SettingsClickable(
        title = title,
        description = description,
        onClick = { showDialog = true },
        trailing = {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                AnimatedContent(targetState = value) {
                    Text(
                        text = it.toString(),
                        style = MaterialTheme.typography.labelLargeEmphasized,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    )
}

object NumbersOnlyTransformation : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        if (asCharSequence().isNotEmpty() && asCharSequence().toString().toIntOrNull() == null) {
            revertAllChanges()
        }
    }
}
