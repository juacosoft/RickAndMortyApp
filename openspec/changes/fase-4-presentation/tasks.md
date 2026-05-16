## 1. MVIContract

- [x] 1.1 Crear `presentation/MVIContract.kt` con interfaz genérica `MVIContract<State, Event, Effect>` con `uiState: StateFlow<State>`, `effect: SharedFlow<Effect>` y `fun onEvent(event: Event)`

## 2. Theme

- [x] 2.1 Crear `presentation/theme/Color.kt` con paleta de colores de la app
- [x] 2.2 Crear `presentation/theme/Type.kt` con tipografía Material 3
- [x] 2.3 Crear `presentation/theme/Theme.kt` con composable `RickAndMortyTheme` que envuelve `MaterialTheme`
- [x] 2.4 Actualizar `MainActivity` para envolver el contenido en `RickAndMortyTheme`

## 3. Navegación

- [x] 3.1 Crear `presentation/navigation/AppRoute.kt` con `sealed class AppRoute(val route: String)` y objeto `Characters`
- [x] 3.2 Crear `presentation/navigation/AppNavigation.kt` con composable `AppNavigation(navController)` que configura `NavHost` con `Characters` como startDestination
- [x] 3.3 Actualizar `MainActivity` para instanciar `rememberNavController()` y llamar `AppNavigation`

## 4. Componentes Transversales

- [x] 4.1 Crear `presentation/components/FullScreenLoader.kt` con `CircularProgressIndicator` centrado
- [x] 4.2 Crear `presentation/components/BottomLoader.kt` con `CircularProgressIndicator` pequeño centrado horizontalmente
- [x] 4.3 Crear `presentation/components/ErrorView.kt` con parámetros `message: String` y `onRetry: () -> Unit` y botón "Retry"
- [x] 4.4 Crear `presentation/components/RickAndMortyTopBar.kt` con `TopAppBar` de Material 3

## 5. Drawables de Placeholder

- [x] 5.1 Crear `res/drawable/ic_placeholder.xml` (vector drawable simple, ej. silueta de persona)
- [x] 5.2 Crear `res/drawable/ic_image_error.xml` (vector drawable simple, ej. icono de imagen rota)

## 6. CharactersViewModel — Contrato MVI

- [x] 6.1 Crear `presentation/characters/CharactersContract.kt`

## 7. CharactersViewModel — Implementación

- [x] 7.1 Crear `presentation/characters/CharactersViewModel.kt` como `@HiltViewModel`
- [x] 7.2 Implementar carga inicial en `init`: emitir `isLoading = true` y cargar página 1
- [x] 7.3 Implementar `onEvent(LoadNextPage)`: guard contra `isLoading`, `isLoadingMore`, `!hasNextPage`; cargar `currentPage + 1`; acumular personajes
- [x] 7.4 Implementar `onEvent(RetryInitial)`: resetear lista, `currentPage = 0`, cargar página 1
- [x] 7.5 Implementar `onEvent(RetryPagination)`: cargar `currentPage + 1` con `isLoadingMore = true`
- [x] 7.6 Implementar `onEvent(Refresh)`: equivalente a `RetryInitial`

## 8. CharactersScreen — UI

- [x] 8.1 Crear `presentation/characters/CharacterItem.kt`
- [x] 8.2 Crear `presentation/characters/CharactersScreen.kt` con `Scaffold` + `RickAndMortyTopBar`
- [x] 8.3 Agregar detección de scroll al final en `LazyColumn` para disparar `UiEvent.LoadNextPage`
- [x] 8.4 Agregar `BottomLoader` como último ítem de `LazyColumn` cuando `isLoadingMore = true`
- [x] 8.5 Agregar `ErrorView` al final de `LazyColumn` cuando `paginationError != null`
- [x] 8.6 Integrar `PullToRefreshBox` para `UiEvent.Refresh`
- [x] 8.7 Registrar `CharactersScreen` como destino en `AppNavigation`

## 9. Tests Unitarios — CharactersViewModel

- [x] 9.1 Crear `CharactersViewModelTest`: verificar que el init dispara carga y emite `isLoading = true`
- [x] 9.2 Test: carga exitosa de página 1 → `characters` poblado, `isLoading = false`, `currentPage = 1`
- [x] 9.3 Test: error en página 1 → `error != null`, `isLoading = false`, lista vacía
- [x] 9.4 Test: `LoadNextPage` acumula personajes correctamente
- [x] 9.5 Test: `LoadNextPage` ignorado si `isLoadingMore = true`
- [x] 9.6 Test: error de paginación → `paginationError != null`, lista sin cambios
- [x] 9.7 Test: `RetryInitial` resetea lista y recarga página 1
- [x] 9.8 Test: `Refresh` limpia la lista y recarga desde página 1

## 10. Verificación de Build

- [x] 10.1 Ejecutar `./gradlew assembleDebug` → `BUILD SUCCESSFUL`
- [x] 10.2 Ejecutar `./gradlew test` → `BUILD SUCCESSFUL`, todos los tests pasan

## 11. Pruebas Manuales

- [x] 11.1 App arranca en emulador → pantalla Characters visible con lista de personajes
- [ ] 11.2 Scroll al final de la lista → bottom loader aparece y se carga la siguiente página
- [ ] 11.3 Sin red al inicio → pantalla de error visible con botón Retry funcional
- [ ] 11.4 Sin red al paginar → error al final de lista con Retry funcional
- [x] 11.5 Pull-to-refresh → lista se recarga desde el inicio

## 12. Commit

- [x] 12.1 Semantic commit: `feat: add presentation layer with CharactersViewModel and CharactersScreen`
