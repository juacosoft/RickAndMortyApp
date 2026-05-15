package co.martketing.rickandmortyapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterLocationDto(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)
