package data.repository

import common.result.Result
import data.mapper.toAsset
import database.datasource.LocalDataSource
import database.mapper.toAsset
import database.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import model.Asset
import network.datasource.asset.AssetDataSource
import kotlinx.coroutines.flow.*

interface AssetRepository {
    val currentList: StateFlow<List<Asset>>
    val searchQuery: StateFlow<String>

    suspend fun getAssetList(): Result<Unit>
    suspend fun getAssetDetails(objectNumber: String): Result<Asset>
    suspend fun favorite(asset: Asset): Result<Unit>
    suspend fun search(query: String)
    suspend fun synchronizeAssets(): SyncResult
    suspend fun loadNextPage(): SyncResult
    fun getFavoriteAssetsFlow(): Flow<List<Asset>>
    fun getAssetsFlow(): Flow<List<Asset>>
}

class AssetRepositoryImpl(
    private val remoteDataSource: AssetDataSource,
    private val localDataSource: LocalDataSource
) : AssetRepository {

    private var currentPage = 1
    private val pageSize = 10

    // Holds the current search query. If empty, fetches a general list.
    override val searchQuery = MutableStateFlow("")

    override val currentList: MutableStateFlow<List<Asset>> = MutableStateFlow(emptyList())

    override suspend fun getAssetList(): Result<Unit> {
        // Initially load the first page (or can be triggered by viewModel)
        return loadPage(1)
            .let { if (it.error == null) Result.Success(Unit) else Result.Error(it.error) }
    }

    override suspend fun getAssetDetails(objectNumber: String): Result<Asset> {
        val detailResult = remoteDataSource.getAssetDetails(objectNumber)
        return when (detailResult) {
            is Result.Success -> {
                val asset = detailResult.data.artObject.toAsset()
                Result.Success(asset)
            }
            is Result.Error -> Result.Error(detailResult.exception)
            is Result.Loading -> Result.Loading
        }
    }

    override suspend fun favorite(asset: Asset): Result<Unit> {
        val updatedAsset = asset.copy(isFavorite = !asset.isFavorite)
        localDataSource.updateAsset(updatedAsset.toEntity())
        return Result.Success(Unit)
    }

    override suspend fun search(query: String) {
        searchQuery.value = query
        loadPage(1) // Reset to page 1 when changing query
    }

    override suspend fun synchronizeAssets(): SyncResult {
        // This can be called manually to refresh from page 1
        return loadPage(1)
    }

    override suspend fun loadNextPage(): SyncResult {
        val nextPage = currentPage + 1
        return loadPage(nextPage)
    }

    private suspend fun loadPage(page: Int): SyncResult {
        val remoteResult = remoteDataSource.getAssets(
            query = searchQuery.value.takeIf { it.isNotBlank() },
            page = page,
            pageSize = pageSize,
            culture = "en",
            imgonly = false
        )

        if (remoteResult is Result.Error) {
            return SyncResult(error = remoteResult.exception)
        }

        val remoteAssets = (remoteResult as Result.Success).data.map { it.toAsset() }

        // Get local assets to preserve favorite status
        val localEntities = localDataSource.getAllAssets().associateBy { it.id }

        val mergedAssets = remoteAssets.map { remote ->
            val localEntity = localEntities[remote.id]
            if (localEntity != null) {
                // Preserve favorite status
                remote.copy(isFavorite = localEntity.isFavorite)
            } else {
                remote
            }
        }

        // Insert or update merged assets in the DB
        localDataSource.insertAssets(mergedAssets.map { it.toEntity() })

        currentPage = page
        return SyncResult()
    }

    override fun getFavoriteAssetsFlow(): Flow<List<Asset>> {
        return localDataSource.getFavoriteAssetsFlow().map { entities ->
            entities.map { it.toAsset() }
        }
    }

    override fun getAssetsFlow(): Flow<List<Asset>> {
        return localDataSource.getAssetsFlow().map { entities ->
            entities.map { it.toAsset() }
        }
    }
}
