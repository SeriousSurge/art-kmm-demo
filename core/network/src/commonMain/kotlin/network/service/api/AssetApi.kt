package network.service.api

import network.service.dto.ArtObjectDetailResponse
import network.service.dto.RijksCollectionResponse

interface AssetApi {
    suspend fun getAssets(
        query: String? = null,
        page: Int = 1,
        pageSize: Int = 10,
        culture: String = "en",
        involvedMaker: String? = null,
        type: String? = null,
        material: String? = null,
        technique: String? = null,
        imgonly: Boolean = false,
        toppieces: Boolean = false
    ): RijksCollectionResponse

    suspend fun getAssetDetails(
        objectNumber: String,
        culture: String = "en"
    ): ArtObjectDetailResponse
}

