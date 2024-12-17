package model

import kotlinx.serialization.Serializable

data class Asset(
    val id: String,
    val title: String,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val principalMaker: String? = null,
    val longTitle: String? = null,
    val materials: List<String>? = null,
    val productionPlaces: List<String>? = null
)

