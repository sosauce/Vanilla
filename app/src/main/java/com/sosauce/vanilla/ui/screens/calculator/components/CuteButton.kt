@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.ui.screens.calculator.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.sosauce.vanilla.R
import com.sosauce.vanilla.data.datastore.rememberIsLandscape
import com.sosauce.vanilla.data.datastore.rememberUseButtonsAnimation
import com.sosauce.vanilla.data.datastore.rememberVibration
import com.sosauce.vanilla.utils.BACKSPACE
import com.sosauce.vanilla.utils.PARENTHESES

@Composable
fun RowScope.CuteButton(
    modifier: Modifier = Modifier,
    text: String,
    buttonType: ButtonType = ButtonType.OTHER,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    rectangle: Boolean
) {
    val haptic = LocalHapticFeedback.current
    LocalDensity.current
    val shouldVibrate by rememberVibration()
    val useButtonsAnimation by rememberUseButtonsAnimation()
    val isPressed by interactionSource.collectIsPressedAsState()
    val cornerRadius by animateIntAsState(
        targetValue = if (isPressed && useButtonsAnimation) 24 else 50
    )
    val isLandscape = rememberIsLandscape()
    val backgroundColor = when (buttonType) {
        ButtonType.OPERATOR -> MaterialTheme.colorScheme.primary
        ButtonType.OTHER -> MaterialTheme.colorScheme.surfaceContainer
        ButtonType.ACTION -> MaterialTheme.colorScheme.tertiary
        ButtonType.SPECIAL -> Color.Transparent
    }
    Box(
        modifier = modifier
            .semantics { role = Role.Button }
            .clip(RoundedCornerShape(cornerRadius))
            .combinedClickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = {
                    onClick()
                    if (shouldVibrate) haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                },
                onLongClick = {
                    onLongClick?.invoke()
                    if (shouldVibrate) haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                }
            )
            .defaultMinSize(
                minWidth = ButtonDefaults.MinWidth,
                minHeight = ButtonDefaults.MinHeight
            )
            .weight(1f)
            .background(backgroundColor)
            .let {
                if (!isLandscape && !rectangle) it.aspectRatio(1f) else it
            },
        contentAlignment = Alignment.Center
    ) {

        when (text) {
            BACKSPACE -> {
                Icon(
                    painter = painterResource(R.drawable.backspace_filled),
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.contentColorFor(backgroundColor),
                    modifier = Modifier.size(45.dp)
                )
            }

            PARENTHESES -> {
                Icon(
                    painter = painterResource(R.drawable.parentheses),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.contentColorFor(backgroundColor),
                    modifier = Modifier.size(45.dp)
                )
            }

            else -> {
                Text(
                    text = text,
                    color = contentColorFor(backgroundColor),
                    style = MaterialTheme.typography.displaySmallEmphasized
                )
            }
        }
    }

}

enum class ButtonType {
    OPERATOR,
    SPECIAL,
    ACTION,
    OTHER
}








