package co.martketing.rickandmortyapp.data.mapper

import co.martketing.rickandmortyapp.data.remote.dto.CharacterDto
import co.martketing.rickandmortyapp.domain.model.Character
import co.martketing.rickandmortyapp.domain.model.CharacterStatus

fun CharacterDto.toDomain(): Character = Character(
    id = id,
    name = name,
    status = when (status.lowercase()) {
        "alive" -> CharacterStatus.ALIVE
        "dead" -> CharacterStatus.DEAD
        else -> CharacterStatus.UNKNOWN
    },
    species = species,
    gender = gender,
    imageUrl = image,
    originName = origin.name,
    locationName = location.name
)
