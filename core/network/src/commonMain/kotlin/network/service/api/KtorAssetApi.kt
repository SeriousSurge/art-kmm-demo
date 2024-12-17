package network.service.api

import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import network.service.dto.ArtObjectDetailResponse
import network.service.dto.RijksCollectionResponse
import network.util.ApiConstant
import network.util.ApiConstant.BASE_URL

class KtorAssetApi(private val client: HttpClient) : AssetApi {

    override suspend fun getAssets(
        query: String?,
        page: Int,
        pageSize: Int,
        culture: String,
        involvedMaker: String?,
        type: String?,
        material: String?,
        technique: String?,
        imgonly: Boolean,
        toppieces: Boolean
    ): RijksCollectionResponse {
        val response = client.get("$BASE_URL/$culture/collection") {
            parameter("key", ApiConstant.API_KEY)
            parameter("format", "json")
            parameter("p", page)
            parameter("ps", pageSize)
            // Always pass query, or conditionally pass if not empty
            if (!query.isNullOrEmpty()) {
                parameter("q", query)
            }
            if (!involvedMaker.isNullOrEmpty()) parameter("involvedMaker", involvedMaker)
            if (!type.isNullOrEmpty()) parameter("type", type)
            if (!material.isNullOrEmpty()) parameter("material", material)
            if (!technique.isNullOrEmpty()) parameter("technique", technique)
            if (imgonly) parameter("imgonly", true)
            if (toppieces) parameter("toppieces", true)
        }

        Napier.d("Requested: query=$query, page=$page, pageSize=$pageSize, imgonly=$imgonly")
        return response.body()
    }

    override suspend fun getAssetDetails(objectNumber: String, culture: String): ArtObjectDetailResponse {
        val response = network.service.api.client.get("$BASE_URL/$culture/collection/$objectNumber") {
            parameter("key", ApiConstant.API_KEY)
            parameter("format", "json")
        }
        return response.body()
    }
}
