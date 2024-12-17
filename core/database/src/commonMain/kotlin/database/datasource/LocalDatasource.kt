package database.datasource

import database.dao.AssetDao
import database.dao.SyncEventDao
import kotlinx.coroutines.flow.Flow
import database.entities.AssetEntity
import database.entities.SyncEvent
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first

interface LocalDataSource {
    fun getAssetsFlow(): Flow<List<AssetEntity>>
    fun getFavoriteAssetsFlow(): Flow<List<AssetEntity>>
    suspend fun getAllAssets(): List<AssetEntity>
    suspend fun insertAssets(assets: List<AssetEntity>)
    suspend fun insertSyncEvent(syncEvent: SyncEvent)
    suspend fun updateAsset(asset: AssetEntity)
    suspend fun deleteAsset(assetId: String)
    suspend fun getAssetById(assetId: String): AssetEntity?
}

class LocalDataSourceImpl(private val assetDao: AssetDao, private val syncEventDao: SyncEventDao) : LocalDataSource {
    override fun getAssetsFlow(): Flow<List<AssetEntity>> {
        return assetDao.getAllAssetsFlow()
    }

    override fun getFavoriteAssetsFlow(): Flow<List<AssetEntity>> {
        return assetDao.getFavoriteAssetsFlow()
    }

    override suspend fun getAllAssets(): List<AssetEntity> {
        Napier.d("getAllAssets called")
        return assetDao.getAllAssets()
    }

    override suspend fun insertAssets(assets: List<AssetEntity>) {
        Napier.d("Inserting ${assets.size} assets into local database")
        assetDao.insertAssets(assets)
    }

    override suspend fun updateAsset(asset: AssetEntity) {
        assetDao.updateAsset(asset)
    }

    override suspend fun deleteAsset(assetId: String) {
        assetDao.deleteAsset(assetId)
    }

    override suspend fun getAssetById(assetId: String): AssetEntity? {
        return assetDao.getAssetById(assetId)
    }

    override suspend fun insertSyncEvent(syncEvent: SyncEvent) {
        syncEventDao.insertSyncEvent(syncEvent)
    }
}