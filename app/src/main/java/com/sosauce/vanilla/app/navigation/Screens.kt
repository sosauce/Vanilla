package com.sosauce.vanilla.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Main : NavKey

@Serializable
data object SettingsHome : NavKey

@Serializable
data object LookAndFeel : NavKey

@Serializable
data object HistorySettings : NavKey

@Serializable
data object Formatting : NavKey

@Serializable
data object Misc : NavKey
