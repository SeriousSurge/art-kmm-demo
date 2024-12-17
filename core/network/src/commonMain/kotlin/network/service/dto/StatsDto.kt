package network.service.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtObjectDetailResponse(
    @SerialName("artObject") val artObject: DetailedArtObjectDto
)

@Serializable
data class DetailedArtObjectDto(
    val id: String,
    val objectNumber: String,
    val title: String,
    val principalMaker: String,
    val longTitle: String,
    val subTitle: String,
    val description: String?,
    @SerialName("webImage") val webImage: WebImageDto?,
    val materials: List<String>?,
    val techniques: List<String>?,
    val productionPlaces: List<String>?,
)