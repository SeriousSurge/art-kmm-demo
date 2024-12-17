package data.repository

import model.Asset

data class SyncResult(
    val added: List<Asset> = emptyList(),
    val removed: List<Asset> = emptyList(),
    val modified: List<Asset> = emptyList(),
    val error: Exception? = null
)