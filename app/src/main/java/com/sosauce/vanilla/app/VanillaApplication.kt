package com.sosauce.vanilla.app

import android.app.Application
import com.sosauce.vanilla.core.database.historyDataModule
import com.sosauce.vanilla.feature.calculator.presentation.calculatorPresentationModule
import com.sosauce.vanilla.feature.history.presentation.historyPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VanillaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@VanillaApplication)
            modules(
                historyDataModule,
                historyPresentationModule,
                calculatorPresentationModule
            )
        }
    }
}
