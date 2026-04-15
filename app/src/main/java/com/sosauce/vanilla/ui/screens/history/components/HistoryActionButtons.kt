@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.ui.screens.history.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sosauce.vanilla.R
import com.sosauce.vanilla.data.datastore.rememberHistoryNewestFirst

@Composable
fun HistoryActionButtons(
    modifier: Modifier = Modifier,
    onDeleteHistory: () -> Unit
) {
    var dropDownExpanded by remember { mutableStateOf(false) }
    var newestFirst by rememberHistoryNewestFirst()

    SmallFloatingActionButton(
        onClick = {},
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row {
            IconButton(
                onClick = { dropDownExpanded = true }
            ) {
                AnimatedContent(
                    targetState = !dropDownExpanded
                ) {
                    Icon(
                        painter = if (it) painterResource(R.drawable.sort_rounded) else painterResource(
                            R.drawable.close
                        ),
                        contentDescription = stringResource(R.string.sort)
                    )
                }
            }
            IconButton(
                onClick = onDeleteHistory
            ) {
                Icon(
                    painter = painterResource(R.drawable.trash_rounded),
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.error
                )
            }

            DropdownMenuPopup(
                expanded = dropDownExpanded,
                onDismissRequest = { dropDownExpanded = false }
            ) {
                DropdownMenuGroup(
                    shapes = MenuDefaults.groupShapes()
                ) {
                    DropdownMenuItem(
                        selected = newestFirst,
                        onClick = { newestFirst = true },
                        text = { Text(stringResource(R.string.newest_first)) },
                        trailingIcon = {
                            if (newestFirst) {
                                Icon(
                                    painter = painterResource(R.drawable.check),
                                    contentDescription = null
                                )
                            }
                        },
                        shapes = MenuDefaults.itemShapes()
                    )
                    DropdownMenuItem(
                        selected = !newestFirst,
                        onClick = { newestFirst = false },
                        text = { Text(stringResource(R.string.oldest_first)) },
                        trailingIcon = {
                            if (!newestFirst) {
                                Icon(
                                    painter = painterResource(R.drawable.check),
                                    contentDescription = null
                                )
                            }
                        },
                        shapes = MenuDefaults.itemShapes()
                    )
                }
            }
        }
    }
}