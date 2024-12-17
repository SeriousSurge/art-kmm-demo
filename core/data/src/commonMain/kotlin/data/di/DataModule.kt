package data.di

import data.repository.AssetRepository
import data.repository.AssetRepositoryImpl
import org.koin.dsl.module

object DataModule {
    operator fun invoke() = module {
        single<AssetRepository> { AssetRepositoryImpl(get(), get()) }
    }
}
