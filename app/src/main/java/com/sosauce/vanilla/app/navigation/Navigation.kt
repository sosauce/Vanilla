package com.sosauce.vanilla.app.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.sosauce.nekobites.animations.bouncySpec
import com.sosauce.vanilla.feature.calculator.presentation.CalculatorAction
import com.sosauce.vanilla.core.data.preferences.rememberIsLandscape
import com.sosauce.vanilla.feature.calculator.presentation.CalculatorScreen
import com.sosauce.vanilla.feature.calculator.presentation.CalculatorScreenLandscape
import com.sosauce.vanilla.feature.calculator.presentation.CalculatorViewModel
import com.sosauce.vanilla.feature.history.presentation.HistoryScreen
import com.sosauce.vanilla.feature.history.presentation.HistoryViewModel
import com.sosauce.vanilla.feature.settings.presentation.SettingsFormatting
import com.sosauce.vanilla.feature.settings.presentation.SettingsHistory
import com.sosauce.vanilla.feature.settings.presentation.SettingsLookAndFeel
import com.sosauce.vanilla.feature.settings.presentation.SettingsMisc
import com.sosauce.vanilla.feature.settings.presentation.SettingsDetailScaffold
import com.sosauce.vanilla.feature.settings.presentation.SettingsHomeScreen
import com.sosauce.vanilla.app.di.CalculatorViewModelFactory
import com.sosauce.vanilla.app.di.HistoryViewModelFactory
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun Nav() {
    val activity = LocalActivity.current!!
    val isLandscape = rememberIsLandscape()
    val viewModel =
        viewModel<CalculatorViewModel>(factory = CalculatorViewModelFactory(activity.application))
    val historyViewModel =
        viewModel<HistoryViewModel>(factory = HistoryViewModelFactory(activity.application))

    val backStack = rememberNavBackStack(Main)

    val entryProvider = entryProvider {
        entry<Main> {
            MainDestination(
                isLandscape = isLandscape,
                viewModel = viewModel,
                historyViewModel = historyViewModel,
                onNavigate = backStack::navigate
            )
        }
        entry<SettingsHome> {
            SettingsHomeScreen(
                onNavigate = backStack::navigate,
                onBack = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    } else {
                        activity.moveTaskToBack(true)
                    }
                }
            )
        }
        entry<LookAndFeel> {
            SettingsDetailScaffold(onBack = backStack::navigateBack) {
                SettingsLookAndFeel()
            }
        }
        entry<HistorySettings> {
            SettingsDetailScaffold(onBack = backStack::navigateBack) {
                SettingsHistory()
            }
        }
        entry<Formatting> {
            SettingsDetailScaffold(onBack = backStack::navigateBack) {
                SettingsFormatting()
            }
        }
        entry<Misc> {
            SettingsDetailScaffold(onBack = backStack::navigateBack) {
                SettingsMisc()
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = backStack::navigateBack,
        entryProvider = entryProvider,
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        transitionSpec = {
            ContentTransform(
                targetContentEnter = slideInHorizontally { it } + fadeIn(),
                initialContentExit = slideOutHorizontally { -it / 4 } + fadeOut()
            )
        },
        popTransitionSpec = {
            ContentTransform(
                targetContentEnter = slideInHorizontally { it } + fadeIn(),
                initialContentExit = slideOutHorizontally { -it / 4 } + fadeOut()
            )
        },
        predictivePopTransitionSpec = {
            ContentTransform(
                targetContentEnter = slideInHorizontally { it } + fadeIn(),
                initialContentExit = slideOutHorizontally { -it / 4 } + fadeOut()
            )
        }
    )
}

@Composable
private fun MainDestination(
    isLandscape: Boolean,
    viewModel: CalculatorViewModel,
    historyViewModel: HistoryViewModel,
    onNavigate: (NavKey) -> Unit
) {
    val yTranslation = retain { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val windowInfo = LocalWindowInfo.current

    Box {
        val calculations by historyViewModel.allCalculations.collectAsStateWithLifecycle()
        HistoryScreen(
            calculations = calculations,
            onEvents = historyViewModel::handleHistoryEvent,
            onPutBackToField = { expression ->
                viewModel.handleAction(CalculatorAction.AddExpressionToField(expression))
            },
            onGotoMain = {
                scope.launch {
                    yTranslation.animateTo(0f, bouncySpec())
                }
            }
        )

        if (isLandscape) {
            CalculatorScreenLandscape(
                modifier = Modifier
                    .graphicsLayer {
                        translationY = yTranslation.value
                    },
                viewModel = viewModel,
                onHandleHistoryEvent = historyViewModel::handleHistoryEvent,
                onNavigate = onNavigate,
                onGotoHistory = {
                    scope.launch {
                        yTranslation.animateTo(windowInfo.containerSize.height.toFloat(), bouncySpec())
                    }
                }
            )
        } else {
            CalculatorScreen(
                modifier = Modifier
                    .graphicsLayer {
                        translationY = yTranslation.value
                    },
                viewModel = viewModel,
                onNavigate = onNavigate,
                onHandleHistoryEvent = historyViewModel::handleHistoryEvent,
                onUpdateDragAmount = { dragAmount ->
                    val value = (yTranslation.value + dragAmount).coerceAtLeast(0f)

                    scope.launch {
                        yTranslation.snapTo(value)
                    }
                },
                onDragStopped = {
                    scope.launch {
                        if (yTranslation.value.roundToInt() >= windowInfo.containerSize.height / 2) {
                            yTranslation.animateTo(windowInfo.containerSize.height.toFloat(), bouncySpec())
                        } else {
                            yTranslation.animateTo(0f, bouncySpec())
                        }
                    }
                }
            )
        }
    }
}


