package co.martketing.rickandmortyapp.presentation.characters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import co.martketing.rickandmortyapp.presentation.components.BottomLoader
import co.martketing.rickandmortyapp.presentation.components.ErrorView
import co.martketing.rickandmortyapp.presentation.components.FullScreenLoader
import co.martketing.rickandmortyapp.presentation.components.RickAndMortyTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    viewModel: CharactersViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisible >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.onEvent(CharactersUiEvent.LoadNextPage)
        }
    }

    Scaffold(
        topBar = { RickAndMortyTopBar(title = "Characters") }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { viewModel.onEvent(CharactersUiEvent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.isLoading && state.characters.isEmpty() -> {
                    FullScreenLoader()
                }
                state.error != null && state.characters.isEmpty() -> {
                    ErrorView(
                        message = state.error!!,
                        onRetry = { viewModel.onEvent(CharactersUiEvent.RetryInitial) }
                    )
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = state.characters,
                            key = { it.id }
                        ) { character ->
                            CharacterItem(character = character)
                        }

                        when {
                            state.isLoadingMore -> item {
                                BottomLoader(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                            state.paginationError != null -> item {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    ErrorView(
                                        message = state.paginationError!!,
                                        onRetry = { viewModel.onEvent(CharactersUiEvent.RetryPagination) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
