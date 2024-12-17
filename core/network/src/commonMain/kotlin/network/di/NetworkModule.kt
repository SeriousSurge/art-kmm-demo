package network.di

import network.service.api.client
import io.ktor.client.HttpClient
import network.datasource.asset.AssetDataSource
import network.datasource.asset.AssetDataSourceImpl
import network.service.api.AssetApi
import network.service.api.KtorAssetApi
import org.koin.dsl.module

object NetworkModule {
    operator fun invoke() = module {
        single<HttpClient> { client }
        single<AssetApi> { KtorAssetApi(get()) }
        single<AssetDataSource> { AssetDataSourceImpl(get()) }
    }
}
