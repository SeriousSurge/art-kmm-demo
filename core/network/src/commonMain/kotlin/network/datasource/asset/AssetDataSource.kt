package network.datasource.asset

import common.result.Result
import kotlinx.coroutines.ensureActive
import network.service.api.AssetApi
import network.service.dto.ArtObjectDetailResponse
import network.service.dto.ArtObjectDto
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext

interface AssetDataSource {
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
    ): Result<List<ArtObjectDto>>

    suspend fun getAssetDetails(objectNumber: String, culture: String = "en"): Result<ArtObjectDetailResponse>
}

class AssetDataSourceImpl(private val api: AssetApi) : AssetDataSource {
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
    ): Result<List<ArtObjectDto>> {
        return safeApiCall {
            val response = api.getAssets(
                query = query,
                page = page,
                pageSize = pageSize,
                culture = culture,
                involvedMaker = involvedMaker,
                type = type,
                material = material,
                technique = technique,
                imgonly = imgonly,
                toppieces = toppieces
            )
            response.artObjects
        }
    }

    override suspend fun getAssetDetails(objectNumber: String, culture: String): Result<ArtObjectDetailResponse> {
        return safeApiCall {
            api.getAssetDetails(objectNumber, culture)
        }
    }

    private suspend inline fun <T> safeApiCall(apiCall: () -> T?): Result<T> {
        return try {
            coroutineContext.ensureActive()
            val result = apiCall()
            if (result != null) {
                Result.Success(result)
            } else {
                Result.Error(NullPointerException("Response body is null"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

