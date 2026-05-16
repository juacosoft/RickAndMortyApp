package co.martketing.rickandmortyapp.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.martketing.rickandmortyapp.domain.usecase.GetCharactersUseCase
import co.martketing.rickandmortyapp.presentation.MVIContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel(), MVIContract<CharactersUiState, CharactersUiEvent, CharactersEffect> {

    private val _uiState = MutableStateFlow(CharactersUiState())
    override val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CharactersEffect>()
    override val effect: SharedFlow<CharactersEffect> = _effect.asSharedFlow()

    init {
        loadPage(1)
    }

    override fun onEvent(event: CharactersUiEvent) {
        when (event) {
            is CharactersUiEvent.LoadNextPage -> onLoadNextPage()
            is CharactersUiEvent.RetryInitial -> onRetryInitial()
            is CharactersUiEvent.RetryPagination -> onRetryPagination()
            is CharactersUiEvent.Refresh -> onRefresh()
        }
    }

    private fun onLoadNextPage() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore || state.isRefreshing || !state.hasNextPage) return
        _uiState.update { it.copy(isLoadingMore = true, paginationError = null) }
        loadPage(state.currentPage + 1)
    }

    private fun onRetryInitial() {
        _uiState.update {
            CharactersUiState(isLoading = true)
        }
        loadPage(1)
    }

    private fun onRefresh() {
        if (_uiState.value.isRefreshing || _uiState.value.isLoading) return
        _uiState.update { it.copy(isRefreshing = true, error = null, paginationError = null) }
        loadPage(1)
    }

    private fun onRetryPagination() {
        val state = _uiState.value
        if (state.isLoadingMore) return
        _uiState.update { it.copy(paginationError = null, isLoadingMore = true) }
        loadPage(state.currentPage + 1)
    }

    private fun loadPage(page: Int) {
        viewModelScope.launch {
            val current = _uiState.value
            if (page == 1 && current.characters.isEmpty() && !current.isRefreshing) {
                _uiState.update { it.copy(isLoading = true, error = null) }
            }

            getCharactersUseCase(page)
                .onSuccess { characterPage ->
                    _uiState.update { state ->
                        state.copy(
                            characters = if (page == 1) characterPage.characters
                                         else state.characters + characterPage.characters,
                            isLoading = false,
                            isRefreshing = false,
                            isLoadingMore = false,
                            hasNextPage = characterPage.hasNextPage,
                            currentPage = page,
                            error = null,
                            paginationError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    if (page == 1) {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                isLoadingMore = false,
                                error = throwable.message ?: "Unknown error"
                            )
                        }
                    } else {
                        _uiState.update { state ->
                            state.copy(
                                isLoadingMore = false,
                                paginationError = throwable.message ?: "Unknown error"
                            )
                        }
                    }
                }
        }
    }
}
