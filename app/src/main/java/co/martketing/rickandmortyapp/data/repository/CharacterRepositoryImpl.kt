package co.martketing.rickandmortyapp.data.repository

import co.martketing.rickandmortyapp.data.mapper.toDomain
import co.martketing.rickandmortyapp.data.remote.datasource.CharacterRemoteDataSource
import co.martketing.rickandmortyapp.domain.model.CharacterPage
import co.martketing.rickandmortyapp.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource
) : CharacterRepository {

    override suspend fun getCharacters(page: Int): Result<CharacterPage> =
        runCatching {
            val response = remoteDataSource.getCharacters(page)
            CharacterPage(
                characters = response.results.map { it.toDomain() },
                hasNextPage = response.info.next != null
            )
        }
}
