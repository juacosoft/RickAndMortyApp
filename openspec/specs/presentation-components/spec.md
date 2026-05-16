# Spec: presentation-components

## Purpose
Define the shared UI components used across the presentation layer.

## Requirements

### Requirement: FullScreenLoader muestra indicador de carga centrado
El proyecto SHALL tener un composable `FullScreenLoader` en `presentation/components/` que muestra un `CircularProgressIndicator` centrado ocupando el espacio disponible del contenedor.

#### Scenario: Visible durante carga inicial
- **WHEN** `UiState.isLoading` es `true` y la lista está vacía
- **THEN** `FullScreenLoader` ocupa toda la pantalla y no hay lista visible

### Requirement: BottomLoader muestra indicador de paginación
El proyecto SHALL tener un composable `BottomLoader` en `presentation/components/` que muestra un `CircularProgressIndicator` pequeño centrado horizontalmente, destinado a aparecer al final de la lista durante cargas de paginación.

#### Scenario: Visible al cargar página siguiente
- **WHEN** `UiState.isLoadingMore` es `true`
- **THEN** `BottomLoader` aparece como último ítem de la `LazyColumn`

### Requirement: ErrorView muestra mensaje de error con botón de retry
El proyecto SHALL tener un composable `ErrorView(message: String, onRetry: () -> Unit)` en `presentation/components/` que muestra el mensaje de error y un botón "Retry" que invoca `onRetry` al ser pulsado.

#### Scenario: Retry de carga inicial
- **WHEN** `UiState.error != null` y la lista está vacía
- **THEN** `ErrorView` ocupa toda la pantalla con el mensaje y el botón Retry visible

#### Scenario: Retry de paginación
- **WHEN** `UiState.paginationError != null` y hay personajes cargados
- **THEN** `ErrorView` aparece al final de la lista (no en pantalla completa)

### Requirement: RickAndMortyTopBar muestra el título de la pantalla
El proyecto SHALL tener un composable `RickAndMortyTopBar(title: String)` en `presentation/components/` basado en `TopAppBar` de Material 3.

#### Scenario: Título visible en la pantalla Characters
- **WHEN** `CharactersScreen` está activa
- **THEN** la `TopAppBar` muestra el texto "Characters"
