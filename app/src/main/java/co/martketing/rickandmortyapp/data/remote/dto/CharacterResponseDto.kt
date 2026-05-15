package co.martketing.rickandmortyapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponseDto(
    @SerialName("info") val info: PaginationInfoDto,
    @SerialName("results") val results: List<CharacterDto>
)
