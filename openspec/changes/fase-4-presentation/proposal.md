## Why

La capa de dominio y datos están completas. El siguiente paso es construir la capa de presentación: infraestructura de navegación, tema visual, componentes transversales reutilizables, y la pantalla Characters con su ViewModel siguiendo el patrón MVI.

## What Changes

- Crear `MVIContract` en `presentation/` como interfaz base para todos los ViewModels.
- Crear tema Compose (`RickAndMortyTheme`) en `presentation/theme/`.
- Configurar navegación con `NavHost` y rutas tipadas en `presentation/navigation/`.
- Agregar componentes transversales en `presentation/components/`: `TopAppBar`, `FullScreenLoader`, `BottomLoader`, `ErrorView` con retry.
- Crear `CharactersViewModel` en `presentation/characters/` con `UiState`, `UiEvent` y `Effect` según contrato MVI.
- Crear `CharactersScreen` composable con lista infinita (LazyColumn + paginación), pull-to-refresh, manejo de errores con retry, placeholder de imagen y vista de error si falla la imagen.
- Conectar `CharactersViewModel` con `GetCharactersUseCase`.
- Registrar `CharactersScreen` como destino inicial en el `NavHost`.

## Capabilities

### New Capabilities
- `mvi-contract`: Interfaz `MVIContract` con tipos genéricos `State`, `Event`, `Effect` que todos los ViewModels implementan.
- `presentation-theme`: Tema Compose (`RickAndMortyTheme`) con colores, tipografía y formas.
- `presentation-navigation`: `NavHost` con rutas definidas y `AppNavigation` composable; `CharactersScreen` como destino raíz.
- `presentation-components`: Componentes transversales reutilizables: `RickAndMortyTopBar`, `FullScreenLoader`, `BottomLoader`, `ErrorView`.
- `characters-screen`: `CharactersViewModel` (MVI) + `CharactersScreen` con lista paginada infinita, pull-to-refresh, manejo de errores y carga de imágenes con Coil.

### Modified Capabilities
- (ninguna)

## Impact

- **Capas afectadas**: `presentation/`, `di/` (módulo Hilt para ViewModel si aplica).
- **Nuevas dependencias**: ninguna (Compose, Navigation Compose, Coil y Hilt ya están en el proyecto).
- **Contrato MVI — CharactersViewModel**:
  - `UiState`: `isLoading: Boolean`, `isLoadingMore: Boolean`, `characters: List<Character>`, `error: String?`, `paginationError: String?`, `hasNextPage: Boolean`, `currentPage: Int`.
  - `UiEvent`: `LoadNextPage`, `Retry`, `Refresh`.
  - `Effect`: (ninguno en esta fase; navegación futura al detalle).
- **Punto de entrada**: `MainActivity` lanza `AppNavigation` dentro de `RickAndMortyTheme`.
