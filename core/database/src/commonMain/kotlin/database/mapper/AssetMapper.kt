package database.mapper

import database.entities.AssetEntity
import kotlinx.serialization.encodeToString
import model.Asset
import kotlinx.serialization.json.Json

fun AssetEntity.toAsset(): Asset {
    val json = Json { ignoreUnknownKeys = true }
    return Asset(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        isFavorite = this.isFavorite,
    )
}

fun Asset.toEntity(): AssetEntity {
    val json = Json { ignoreUnknownKeys = true }
    return AssetEntity(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        isFavorite = this.isFavorite,
    )
}
