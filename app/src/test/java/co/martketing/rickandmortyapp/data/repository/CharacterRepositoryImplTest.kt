package co.martketing.rickandmortyapp.data.repository

import co.martketing.rickandmortyapp.data.remote.datasource.CharacterRemoteDataSource
import co.martketing.rickandmortyapp.data.remote.dto.CharacterDto
import co.martketing.rickandmortyapp.data.remote.dto.CharacterLocationDto
import co.martketing.rickandmortyapp.data.remote.dto.CharacterOriginDto
import co.martketing.rickandmortyapp.data.remote.dto.CharacterResponseDto
import co.martketing.rickandmortyapp.data.remote.dto.PaginationInfoDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CharacterRepositoryImplTest {

    private lateinit var dataSource: CharacterRemoteDataSource
    private lateinit var repository: CharacterRepositoryImpl

    @Before
    fun setUp() {
        dataSource = mockk()
        repository = CharacterRepositoryImpl(dataSource)
    }

    private fun buildResponse(nextUrl: String? = "https://next") = CharacterResponseDto(
        info = PaginationInfoDto(count = 826, pages = 42, next = nextUrl, prev = null),
        results = listOf(
            CharacterDto(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = CharacterOriginDto("Earth", ""),
                location = CharacterLocationDto("Citadel", ""),
                image = "https://img.com/1.jpeg",
                url = "https://rickandmortyapi.com/api/character/1"
            )
        )
    )

    @Test
    fun `getCharacters returns Result success with mapped characters`() = runTest {
        coEvery { dataSource.getCharacters(1) } returns buildResponse()

        val result = repository.getCharacters(1)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().characters.size)
        assertEquals("Rick Sanchez", result.getOrThrow().characters.first().name)
    }

    @Test
    fun `getCharacters returns hasNextPage true when next is not null`() = runTest {
        coEvery { dataSource.getCharacters(1) } returns buildResponse(nextUrl = "https://next")

        val result = repository.getCharacters(1)

        assertTrue(result.getOrThrow().hasNextPage)
    }

    @Test
    fun `getCharacters returns hasNextPage false when next is null`() = runTest {
        coEvery { dataSource.getCharacters(42) } returns buildResponse(nextUrl = null)

        val result = repository.getCharacters(42)

        assertFalse(result.getOrThrow().hasNextPage)
    }

    @Test
    fun `getCharacters returns Result failure when datasource throws`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { dataSource.getCharacters(1) } throws exception

        val result = repository.getCharacters(1)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
