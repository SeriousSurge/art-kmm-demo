package network.service.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RijksCollectionResponse(
    @SerialName("artObjects") val artObjects: List<ArtObjectDto>
)

@Serializable
data class ArtObjectDto(
    val id: String,
    val objectNumber: String,
    val title: String,
    val hasImage: Boolean,
    val principalOrFirstMaker: String,
    @SerialName("longTitle") val longTitle: String,
    @SerialName("webImage") val webImage: WebImageDto? = null
)

@Serializable
data class WebImageDto(
    val url: String
)
