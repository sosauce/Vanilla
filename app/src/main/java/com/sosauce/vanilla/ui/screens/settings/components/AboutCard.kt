package com.sosauce.vanilla.ui.screens.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sosauce.vanilla.R
import com.sosauce.vanilla.utils.GITHUB_RELEASES
import com.sosauce.vanilla.utils.SUPPORT_PAGE

@Composable
fun AboutCard() {

    val context = LocalContext.current
    val version = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    val uriHandler = LocalUriHandler.current

    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(15.dp)
                    .clip(RoundedCornerShape(15))
                    .background(Color(0xFFf4a7bd)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.calculator),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = Color(0xFFfdd9dc)
                )
            }
            Column {
                Text(stringResource(R.string.cc_by_sosauce))
                Text(
                    text = "${stringResource(R.string.version)} $version",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Button(
                onClick = { uriHandler.openUri(GITHUB_RELEASES) },
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    bottomStart = 24.dp,
                    topEnd = 4.dp,
                    bottomEnd = 4.dp
                ),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.update)) }
            Spacer(Modifier.width(2.dp))
            Button(
                onClick = { uriHandler.openUri(SUPPORT_PAGE) },
                shape = RoundedCornerShape(
                    topStart = 4.dp,
                    bottomStart = 4.dp,
                    topEnd = 24.dp,
                    bottomEnd = 24.dp
                ),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.support)) }
        }
    }
}