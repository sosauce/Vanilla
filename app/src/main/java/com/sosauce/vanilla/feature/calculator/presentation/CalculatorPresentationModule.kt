package com.sosauce.vanilla.feature.calculator.presentation

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val calculatorPresentationModule = module {
    // Lambda overload: the constructor takes the Application, which cannot
    // be resolved by constructor reference alone.
    viewModel { CalculatorViewModel(androidApplication()) }
}
