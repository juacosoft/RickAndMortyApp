package co.martketing.rickandmortyapp.domain.usecase

import co.martketing.rickandmortyapp.domain.model.Character
import co.martketing.rickandmortyapp.domain.model.CharacterPage
import co.martketing.rickandmortyapp.domain.repository.CharacterRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCharactersUseCaseTest {

    private lateinit var repository: CharacterRepository
    private lateinit var useCase: GetCharactersUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetCharactersUseCase(repository)
    }

    @Test
    fun `invoke returns Result_success when repository returns success`() = runTest {
        val page = CharacterPage(characters = emptyList<Character>(), hasNextPage = false)
        val expected = Result.success(page)
        coEvery { repository.getCharacters(1) } returns expected

        val result = useCase(1)

        assertTrue(result.isSuccess)
        assertEquals(expected, result)
    }

    @Test
    fun `invoke returns Result_failure when repository returns failure`() = runTest {
        val exception = RuntimeException("network error")
        val expected = Result.failure<CharacterPage>(exception)
        coEvery { repository.getCharacters(1) } returns expected

        val result = useCase(1)

        assertTrue(result.isFailure)
        assertEquals(expected, result)
    }

    @Test
    fun `invoke propagates result without transformation`() = runTest {
        val page = CharacterPage(characters = emptyList<Character>(), hasNextPage = true)
        val expected = Result.success(page)
        coEvery { repository.getCharacters(2) } returns expected

        val result = useCase(2)

        // Same object reference — no wrapping or transformation
        assertEquals(expected, result)
        assertEquals(page, result.getOrNull())
    }
}
