# Spec: characters-screen

## Purpose
Define the requirements for the `CharactersScreen` composable, its `CharactersViewModel`, and the `CharacterItem` UI component in the presentation layer.

## Requirements

### Requirement: CharactersViewModel implementa MVIContract con UiState, UiEvent y Effect
`CharactersViewModel` SHALL ser un `@HiltViewModel` que implementa `MVIContract<CharactersUiState, CharactersUiEvent, CharactersEffect>` e inyecta `GetCharactersUseCase`. El estado inicial SHALL tener la lista vacía, `isLoading = true`, `currentPage = 0`, `hasNextPage = true`.

#### Scenario: Estado inicial dispara carga de página 1
- **WHEN** el ViewModel es creado
- **THEN** emite `isLoading = true` y lanza la carga de la página 1 automáticamente

#### Scenario: Carga exitosa de página 1 actualiza la lista
- **WHEN** `GetCharactersUseCase(1)` retorna `Result.success(CharacterPage)`
- **THEN** `uiState` emite `isLoading = false`, `characters = page.characters`, `hasNextPage = page.hasNextPage`, `currentPage = 1`

#### Scenario: Error en carga inicial expone error en UiState
- **WHEN** `GetCharactersUseCase(1)` retorna `Result.failure(exception)`
- **THEN** `uiState` emite `isLoading = false`, `error = exception.message`, lista vacía

### Requirement: CharactersViewModel pagina incrementalmente sin duplicar carga
`CharactersViewModel` SHALL acumular personajes en la lista al cargar páginas sucesivas. SHALL ignorar `UiEvent.LoadNextPage` si `isLoadingMore`, `isLoading`, o `!hasNextPage` son verdaderos.

#### Scenario: LoadNextPage agrega personajes a la lista existente
- **WHEN** `UiEvent.LoadNextPage` es recibido y la condición de carga es válida
- **THEN** `uiState` emite `isLoadingMore = true`, y al completar, agrega los nuevos personajes a `characters` manteniendo los anteriores

#### Scenario: LoadNextPage es ignorado si ya hay carga en curso
- **WHEN** `UiEvent.LoadNextPage` es enviado mientras `isLoadingMore = true`
- **THEN** no se lanza ninguna nueva carga y el estado no cambia

#### Scenario: Error de paginación expone paginationError sin borrar la lista
- **WHEN** `GetCharactersUseCase(page > 1)` retorna `Result.failure`
- **THEN** `uiState` emite `isLoadingMore = false`, `paginationError = exception.message`, `characters` sin cambios

### Requirement: CharactersViewModel soporta Retry diferenciado
`CharactersViewModel` SHALL manejar `UiEvent.RetryInitial` (resetea lista y recarga página 1) y `UiEvent.RetryPagination` (reintenta la última página fallida).

#### Scenario: RetryInitial resetea la lista y recarga desde página 1
- **WHEN** `UiEvent.RetryInitial` es recibido
- **THEN** `uiState` emite `characters = emptyList()`, `error = null`, `isLoading = true` y se dispara carga de página 1

#### Scenario: RetryPagination reintenta la última página fallida
- **WHEN** `UiEvent.RetryPagination` es recibido y `paginationError != null`
- **THEN** se dispara la carga de `currentPage + 1` con `isLoadingMore = true`

### Requirement: CharactersViewModel soporta Refresh (pull-to-refresh)
`CharactersViewModel` SHALL manejar `UiEvent.Refresh` reseteando la lista y recargando desde página 1, equivalente a `RetryInitial`.

#### Scenario: Refresh limpia la lista y recarga desde el inicio
- **WHEN** `UiEvent.Refresh` es recibido
- **THEN** `characters` se vacía, `currentPage` vuelve a 0, `isLoading = true`, y se carga la página 1

### Requirement: CharactersScreen muestra la lista con paginación y manejo de errores
`CharactersScreen` SHALL ser un composable en `presentation/characters/` que observa `CharactersViewModel.uiState` y envía eventos via `onEvent`. SHALL mostrar: `FullScreenLoader` en carga inicial, `ErrorView` en error inicial, `LazyColumn` de personajes con `BottomLoader` al final durante paginación, `ErrorView` al final en error de paginación, y soporte de pull-to-refresh.

#### Scenario: Lista visible con personajes cargados
- **WHEN** `UiState.characters` no está vacía e `isLoading = false`
- **THEN** la `LazyColumn` muestra un ítem por cada `Character` con imagen, nombre y estado

#### Scenario: Paginación disparada al llegar al final de la lista
- **WHEN** el último ítem de la `LazyColumn` es visible y `hasNextPage = true` y no hay carga en curso
- **THEN** se envía `UiEvent.LoadNextPage` al ViewModel

#### Scenario: Pull-to-refresh disponible en la pantalla
- **WHEN** el usuario hace swipe hacia abajo en la lista
- **THEN** se envía `UiEvent.Refresh` al ViewModel y el indicador de refresh es visible mientras `isLoading = true`

### Requirement: CharacterItem muestra imagen, nombre y estado con Coil
Cada ítem de la lista SHALL mostrar la imagen del personaje cargada con `AsyncImage` de Coil, el nombre y el estado (`Alive`, `Dead`, `Unknown`). SHALL tener placeholder durante la carga y vista de error si la imagen falla.

#### Scenario: Imagen cargada exitosamente
- **WHEN** Coil descarga la imagen desde `Character.imageUrl`
- **THEN** la imagen aparece en el ítem en el tamaño definido

#### Scenario: Placeholder visible mientras carga la imagen
- **WHEN** la imagen aún no se descargó
- **THEN** se muestra el drawable de placeholder en lugar de un espacio vacío

#### Scenario: Vista de error si la imagen falla
- **WHEN** Coil no puede descargar la imagen
- **THEN** se muestra el drawable de error en lugar de la imagen
