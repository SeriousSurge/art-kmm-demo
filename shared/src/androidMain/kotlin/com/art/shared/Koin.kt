package com.art.shared

import android.content.Context
import database.di.DatabaseModule
import org.koin.core.context.startKoin


fun initKoin(context: Context) {
    startKoin {
        val databaseModule = DatabaseModule.invoke(context)
        modules(commonModules.plus(databaseModule))
    }
}