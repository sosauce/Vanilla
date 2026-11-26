package com.sosauce.vanilla.core.presentation

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowManager
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.sosauce.vanilla.core.domain.toDecimalChar
import com.sosauce.vanilla.core.domain.toGroupingChar
import java.text.DecimalFormatSymbols

@Composable
fun rememberSeparatorSymbols(): DecimalFormatSymbols {
    val locale = LocalConfiguration.current.locales[0]
    return remember(locale) {
        DecimalFormatSymbols.getInstance(locale)
    }
}

@Composable
fun rememberResolvedSeparators(
    decimalPreference: String,
    groupingPreference: String
): Pair<Char, Char?> {
    val symbols = rememberSeparatorSymbols()

    val decimalSeparator = remember(decimalPreference, symbols) {
        decimalPreference.toDecimalChar(symbols)
    }
    val groupingSeparator = remember(groupingPreference, symbols) {
        groupingPreference.toGroupingChar(symbols)
    }

    return decimalSeparator to groupingSeparator
}


fun Activity.showOnLockScreen(show: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(show)
    } else {
        if (show) {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }
}

fun Modifier.selfAlignHorizontally(align: Alignment.Horizontal = Alignment.CenterHorizontally): Modifier {
    return this.then(
        Modifier
            .fillMaxWidth()
            .wrapContentWidth(align)
    )
}

val Context.appVersion
    get() = packageManager.getPackageInfo(packageName, 0).versionName
