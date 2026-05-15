package co.martketing.rickandmortyapp.domain.usecase

import co.martketing.rickandmortyapp.domain.model.CharacterPage
import co.martketing.rickandmortyapp.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(page: Int): Result<CharacterPage> =
        repository.getCharacters(page)
}
