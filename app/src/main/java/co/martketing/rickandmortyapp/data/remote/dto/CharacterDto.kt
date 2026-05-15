package co.martketing.rickandmortyapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("status") val status: String,
    @SerialName("species") val species: String,
    @SerialName("type") val type: String,
    @SerialName("gender") val gender: String,
    @SerialName("origin") val origin: CharacterOriginDto,
    @SerialName("location") val location: CharacterLocationDto,
    @SerialName("image") val image: String,
    @SerialName("url") val url: String
)
