@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.ui.shared_components

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sosauce.vanilla.R

@Composable
fun CuteNavigationButton(
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.back_arrow,
    onNavigateUp: () -> Unit
) {
    FloatingActionButton(
        onClick = onNavigateUp,
        shape = MaterialShapes.Cookie9Sided.toShape(),
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(R.string.back)
        )
    }
}