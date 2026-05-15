package co.martketing.rickandmortyapp.domain.repository

import co.martketing.rickandmortyapp.domain.model.CharacterPage

interface CharacterRepository {
    suspend fun getCharacters(page: Int): Result<CharacterPage>
}
