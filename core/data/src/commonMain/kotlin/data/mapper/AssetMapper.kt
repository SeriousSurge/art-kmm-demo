package data.mapper

import model.Asset
import network.service.dto.ArtObjectDto
import network.service.dto.DetailedArtObjectDto

fun ArtObjectDto.toAsset(): Asset {
    return Asset(
        id = objectNumber,
        title = title,
        imageUrl = webImage?.url ?: "",
        isFavorite = false,
        principalMaker = principalOrFirstMaker,
        longTitle = longTitle
    )
}

// In your mapper (DetailedArtObjectDto.toAsset())
fun DetailedArtObjectDto.toAsset(): Asset {
    return Asset(
        id = objectNumber,
        title = title,
        imageUrl = webImage?.url ?: "",
        isFavorite = false,
        principalMaker = principalMaker,
        longTitle = longTitle,
        materials = materials,
        productionPlaces = productionPlaces
    )
}