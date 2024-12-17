package com.art.shared

import common.definition.viewModelDefinition
import common.di.CoroutineModule
import data.di.DataModule
import network.di.NetworkModule
import org.koin.dsl.module
import detail.AssetDetailViewModel
import favorite.FavoriteViewModel
import list.AssetListViewModel

private object ViewModelsModule {
    operator fun invoke() = module {
        viewModelDefinition { AssetListViewModel(get()) }
        viewModelDefinition { AssetDetailViewModel(get()) }
        viewModelDefinition { FavoriteViewModel(get()) }
    }
}

internal val commonModules = listOf(
    CoroutineModule(),
    NetworkModule(),
    DataModule(),
    ViewModelsModule(),
)