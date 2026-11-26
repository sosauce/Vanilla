package com.sosauce.vanilla.feature.history.presentation

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val historyPresentationModule = module {
    viewModelOf(::HistoryViewModel)
}
