package com.sosauce.vanilla.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.sosauce.vanilla.core.data.preferences.rememberAppTheme
import com.sosauce.vanilla.core.data.preferences.rememberShowOnLockScreen
import com.sosauce.vanilla.app.navigation.Nav
import com.sosauce.vanilla.core.designsystem.theme.VanillaTheme
import com.sosauce.vanilla.core.domain.CuteTheme
import com.sosauce.vanilla.utils.showOnLockScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val isSystemInDarkTheme = isSystemInDarkTheme()
            val theme by rememberAppTheme()
            val showOnLockScreen by rememberShowOnLockScreen()

            showOnLockScreen(showOnLockScreen)

            VanillaTheme {
                WindowCompat
                    .getInsetsController(window, window.decorView)
                    .apply {

                        val isLight =
                            if (theme == CuteTheme.SYSTEM) !isSystemInDarkTheme else theme == CuteTheme.LIGHT

                        isAppearanceLightStatusBars = isLight
                        isAppearanceLightNavigationBars = isLight
                    }

                Nav()
            }
        }
    }
}

