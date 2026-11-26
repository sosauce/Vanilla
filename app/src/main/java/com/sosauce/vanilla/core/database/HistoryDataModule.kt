package com.sosauce.vanilla.core.database

import androidx.room.Room
import com.sosauce.vanilla.feature.history.domain.HistoryLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val historyDataModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = HistoryDatabase::class.java,
            name = "history.db"
        ).build()
    }
    single { get<HistoryDatabase>().dao }
    singleOf(::RoomHistoryDataSource) { bind<HistoryLocalDataSource>() }
}
