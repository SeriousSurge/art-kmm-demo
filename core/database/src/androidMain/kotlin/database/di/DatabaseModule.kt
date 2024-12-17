package database.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.AppDatabase
import database.datasource.LocalDataSource
import database.datasource.LocalDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

object DatabaseModule {
    operator fun invoke(context: Context): Module = module {
        single<LocalDataSource> { LocalDataSourceImpl(appDatabase(context).assetDao(), appDatabase(context).syncEventDao()) }
    }
}

private fun appDatabase(context: Context): AppDatabase {
    val dbFile = context.getDatabasePath("asset.db")
    return Room.databaseBuilder<AppDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}