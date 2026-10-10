@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.feature.history.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.SelectableDropdownMenuItem
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
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
import com.sosauce.nekobites.animations.AnimatedDrawable
import com.sosauce.nekobites.animations.AnimatedDrawableFile
import com.sosauce.vanilla.R
import com.sosauce.vanilla.core.presentation.rememberHistoryNewestFirst

@Composable
fun HistoryActionButtons(
    modifier: Modifier = Modifier,
    onDeleteHistory: () -> Unit
) {
    var dropDownExpanded by remember { mutableStateOf(false) }
    var newestFirst by rememberHistoryNewestFirst()


    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        shadowElevation = 5.dp,
        color =  MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Row {
            IconButton(
                onClick = { dropDownExpanded = true }
            ) {
                AnimatedDrawable(
                    drawable = AnimatedDrawableFile.MORE_VERT,
                    atEnd = dropDownExpanded
                )
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
                    SelectableDropdownMenuItem(
                        selected = newestFirst,
                        onClick = { newestFirst = true },
                        text = { Text(stringResource(R.string.newest_first)) },
                        trailingContent = {
                            if (newestFirst) {
                                Icon(
                                    painter = painterResource(R.drawable.check),
                                    contentDescription = null
                                )
                            }
                        },
                        shapes = MenuDefaults.itemShapes()
                    )
                    SelectableDropdownMenuItem(
                        selected = !newestFirst,
                        onClick = { newestFirst = false },
                        text = { Text(stringResource(R.string.oldest_first)) },
                        trailingContent = {
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