package com.sosauce.vanilla.feature.settings.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sosauce.vanilla.R

@Composable
fun SettingsInput(
    value: Int,
    maxValue: Int,
    minValue: Int,
    onNewValue: (Int) -> Unit,
    topDp: Dp,
    bottomDp: Dp,
    text: Int,
    @StringRes optionalDescription: Int? = null,
) {

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {

        val focusRequester = remember { FocusRequester() }
        val textFieldState = rememberTextFieldState(value.toString())
        val typedValue =
            remember(textFieldState.text) { textFieldState.text.toString().toIntOrNull() ?: 0 }
        val isError = remember(typedValue) { typedValue !in minValue..maxValue }

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
                                    minValue,
                                    maxValue
                                )
                            )
                        }
                    }
                )
            }
        )
    }



    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 1.dp),
        shape = RoundedCornerShape(
            topStart = topDp,
            topEnd = topDp,
            bottomStart = bottomDp,
            bottomEnd = bottomDp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(15.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
            ) {
                Column {
                    Text(stringResource(text))
                    optionalDescription?.let {
                        Text(
                            text = stringResource(it),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            TextButton(
                onClick = { showDialog = true },
                shapes = ButtonDefaults.shapes()
            ) {
                AnimatedContent(
                    targetState = value
                ) {
                    Text(
                        text = it.toString(),
                        style = MaterialTheme.typography.bodyLargeEmphasized.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}

object NumbersOnlyTransformation : InputTransformation {
    override fun TextFieldBuffer.transformInput() {

        if (!asCharSequence().isEmpty()) {
            val input = asCharSequence().toString().toIntOrNull()
            if (input == null) {
                revertAllChanges()
            }
        }

    }
}