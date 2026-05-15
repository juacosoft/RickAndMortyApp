package co.martketing.rickandmortyapp.presentation.characters

import co.martketing.rickandmortyapp.domain.model.Character
import co.martketing.rickandmortyapp.domain.model.CharacterPage
import co.martketing.rickandmortyapp.domain.model.CharacterStatus
import co.martketing.rickandmortyapp.domain.usecase.GetCharactersUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetCharactersUseCase
    private lateinit var viewModel: CharactersViewModel

    private val sampleCharacter = Character(
        id = 1,
        name = "Rick Sanchez",
        status = CharacterStatus.ALIVE,
        species = "Human",
        gender = "Male",
        imageUrl = "https://example.com/rick.png",
        originName = "Earth",
        locationName = "Citadel"
    )

    private val samplePage1 = CharacterPage(
        characters = listOf(sampleCharacter),
        hasNextPage = true
    )

    private val samplePage2 = CharacterPage(
        characters = listOf(sampleCharacter.copy(id = 2, name = "Morty Smith")),
        hasNextPage = false
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init triggers page 1 load and emits isLoading true initially`() = runTest {
        coEvery { useCase(1) } returns Result.success(samplePage1)

        viewModel = CharactersViewModel(useCase)

        assertTrue(viewModel.uiState.value.isLoading)
        advanceUntilIdle()
    }

    @Test
    fun `successful page 1 load populates characters and clears loading`() = runTest {
        coEvery { useCase(1) } returns Result.success(samplePage1)

        viewModel = CharactersViewModel(useCase)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(samplePage1.characters, state.characters)
        assertEquals(1, state.currentPage)
        assertTrue(state.hasNextPage)
        assertNull(state.error)
    }

    @Test
    fun `page 1 failure sets error and clears loading`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { useCase(1) } returns Result.failure(exception)

        viewModel = CharactersViewModel(useCase)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.characters.isEmpty())
        assertNotNull(state.error)
        assertEquals("Network error", state.error)
    }

    @Test
    fun `LoadNextPage accumulates characters from subsequent pages`() = runTest {
        coEvery { useCase(1) } returns Result.success(samplePage1)
        coEvery { useCase(2) } returns Result.success(samplePage2)

        viewModel = CharactersViewModel(useCase)
        advanceUntilIdle()

        viewModel.onEvent(CharactersUiEvent.LoadNextPage)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.characters.size)
        assertEquals(2, state.currentPage)
        assertFalse(state.hasNextPage)
    }

    @Test
    fun `LoadNextPage is ignored when isLoadingMore is true`() = runTest {
        coEvery { useCase(1) } returns Result.success(samplePage1)

        viewModel = CharactersViewModel(useCase)
        advanceUntilIdle()

        // Simulate already loading more by sending two rapid events
        coEvery { useCase(2) } coAnswers {
            // Send second event before first completes
            viewModel.onEvent(CharactersUiEvent.LoadNextPage)
            Result.success(samplePage2)
        }

        viewModel.onEvent(CharactersUiEvent.LoadNextPage)
        advanceUntilIdle()

        // Page 2 should only be called once
        coVerify(exactly = 1) { useCase(2) }
    }

    @Test
    fun `pagination error sets paginationError and preserves existing characters`() = runTest {
        val exception = RuntimeException("Pagination failed")
        coEvery { useCase(1) } returns Result.success(samplePage1)
        coEvery { useCase(2) } returns Result.failure(exception)

        viewModel = CharactersViewModel(useCase)
        advanceUntilIdle()

        viewModel.onEvent(CharactersUiEvent.LoadNextPage)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(samplePage1.characters, state.characters)
        assertNotNull(state.paginationError)
        assertEquals("Pagination failed", state.paginationError)
        assertFalse(state.isLoadingMore)
    }

    @Test
    fun `RetryInitial resets list and reloads from page 1`() = runTest {
        val exception = RuntimeException("error")
        coEvery { useCase(1) } returnsMany listOf(
            Result.failure(exception),
            Result.success(samplePage1)
        )

        viewModel = CharactersViewModel(useCase)
        advanceUntilIdle()

        viewModel.onEvent(CharactersUiEvent.RetryInitial)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(samplePage1.characters, state.characters)
        assertNull(state.error)
    }

    @Test
    fun `Refresh clears list and reloads from page 1`() = runTest {
        coEvery { useCase(1) } returns Result.success(samplePage1)
        coEvery { useCase(2) } returns Result.success(samplePage2)

        viewModel = CharactersViewModel(useCase)
        advanceUntilIdle()

        viewModel.onEvent(CharactersUiEvent.LoadNextPage)
        advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.characters.size)

        coEvery { useCase(1) } returns Result.success(samplePage1)
        viewModel.onEvent(CharactersUiEvent.Refresh)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(samplePage1.characters, state.characters)
        assertEquals(1, state.currentPage)
        assertNull(state.error)
    }
}
