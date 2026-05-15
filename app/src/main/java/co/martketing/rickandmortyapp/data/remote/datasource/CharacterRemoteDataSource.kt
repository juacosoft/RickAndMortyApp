package co.martketing.rickandmortyapp.data.remote.datasource

import co.martketing.rickandmortyapp.data.remote.dto.CharacterResponseDto

interface CharacterRemoteDataSource {
    suspend fun getCharacters(page: Int): CharacterResponseDto
}
