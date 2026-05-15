package co.martketing.rickandmortyapp.data.remote.datasource

import co.martketing.rickandmortyapp.data.remote.dto.CharacterResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class CharacterRemoteDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : CharacterRemoteDataSource {

    override suspend fun getCharacters(page: Int): CharacterResponseDto =
        runCatching {
            httpClient.get("https://rickandmortyapi.com/api/character") {
                parameter("page", page)
            }.body<CharacterResponseDto>()
        }.getOrElse { error ->
            if (error is CancellationException) throw error
            throw error
        }
}
