package database.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.AppDatabase
import database.datasource.LocalDataSource
import database.datasource.LocalDataSourceImpl
import database.instantiateImpl
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory


object DatabaseModule {
    operator fun invoke(): Module = module {
        single<LocalDataSource> { LocalDataSourceImpl(appDatabase.assetDao(), appDatabase.syncEventDao()) }
    }
}

private val appDatabase: AppDatabase
    get() {
        val dbFile = NSHomeDirectory() + "/asset.db"
        return Room.databaseBuilder<AppDatabase>(
            name = dbFile,
            factory = { AppDatabase::class.instantiateImpl() }
        )
            .setDriver(BundledSQLiteDriver())
            .build()
    }