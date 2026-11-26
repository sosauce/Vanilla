@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.vanilla.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import com.sosauce.vanilla.R
import com.sosauce.vanilla.core.presentation.rememberAppTheme
import com.sosauce.vanilla.core.presentation.rememberUseSystemFont
import com.sosauce.vanilla.core.domain.CuteTheme

@Composable
fun VanillaTheme(
    content: @Composable () -> Unit
) {

    val isSystemInDarkTheme = isSystemInDarkTheme()
    val appTheme by rememberAppTheme()
    val useSystemFont by rememberUseSystemFont()


    val colorScheme = when (appTheme) {
        CuteTheme.AMOLED -> anyDarkColorScheme().copy(
            surface = Color.Black,
            inverseSurface = Color.White,
            background = Color.Black,
        )

        CuteTheme.SYSTEM -> if (isSystemInDarkTheme) anyDarkColorScheme() else anyLightColorScheme()
        CuteTheme.DARK -> anyDarkColorScheme()
        CuteTheme.LIGHT -> anyLightColorScheme()
        else -> anyDarkColorScheme()
    }



    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        content = content,
        typography = if (useSystemFont) null else GoogleSansFlexTypography
    )

}

val googleSansFlexFontFamily = FontFamily(
    Font(
        resId = R.font.google_sans_flex_static
    )
)

val GoogleSansFlexTypography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = googleSansFlexFontFamily),
        displayMedium = displayMedium.copy(fontFamily = googleSansFlexFontFamily),
        displaySmall = displaySmall.copy(fontFamily = googleSansFlexFontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = googleSansFlexFontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = googleSansFlexFontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = googleSansFlexFontFamily),
        titleLarge = titleLarge.copy(fontFamily = googleSansFlexFontFamily),
        titleMedium = titleMedium.copy(fontFamily = googleSansFlexFontFamily),
        titleSmall = titleSmall.copy(fontFamily = googleSansFlexFontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = googleSansFlexFontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = googleSansFlexFontFamily),
        bodySmall = bodySmall.copy(fontFamily = googleSansFlexFontFamily),
        labelLarge = labelLarge.copy(fontFamily = googleSansFlexFontFamily),
        labelMedium = labelMedium.copy(fontFamily = googleSansFlexFontFamily),
        labelSmall = labelSmall.copy(fontFamily = googleSansFlexFontFamily),
        displayLargeEmphasized = displayLargeEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        displayMediumEmphasized = displayMediumEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        displaySmallEmphasized = displaySmallEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        headlineLargeEmphasized = headlineLargeEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        headlineMediumEmphasized = headlineMediumEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        headlineSmallEmphasized = headlineSmallEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        titleLargeEmphasized = titleLargeEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        titleMediumEmphasized = titleMediumEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        titleSmallEmphasized = titleSmallEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        bodyLargeEmphasized = bodyLargeEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        bodyMediumEmphasized = bodyMediumEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        bodySmallEmphasized = bodySmallEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        labelLargeEmphasized = labelLargeEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        labelMediumEmphasized = labelMediumEmphasized.copy(fontFamily = googleSansFlexFontFamily),
        labelSmallEmphasized = labelSmallEmphasized.copy(fontFamily = googleSansFlexFontFamily)
    )
}
