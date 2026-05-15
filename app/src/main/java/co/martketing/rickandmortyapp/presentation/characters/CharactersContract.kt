package co.martketing.rickandmortyapp.presentation.characters

import co.martketing.rickandmortyapp.domain.model.Character

data class CharactersUiState(
    val characters: List<Character> = emptyList(),
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val paginationError: String? = null,
    val hasNextPage: Boolean = true,
    val currentPage: Int = 0
)

sealed class CharactersUiEvent {
    object LoadNextPage : CharactersUiEvent()
    object RetryInitial : CharactersUiEvent()
    object RetryPagination : CharactersUiEvent()
    object Refresh : CharactersUiEvent()
}

sealed class CharactersEffect
