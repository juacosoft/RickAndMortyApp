## Context

La capa de datos (Ktor, DTOs, repositorio) y la capa de dominio (`Character`, `CharacterPage`, `GetCharactersUseCase`) están completas. La capa de presentación existe como directorio vacío. Esta fase implementa toda la infraestructura de presentación más la primera pantalla funcional: Characters list.

Stack disponible: Jetpack Compose, Navigation Compose, Hilt, Coroutines/StateFlow/SharedFlow, Coil, MockK + Turbine para tests.

## Goals / Non-Goals

**Goals:**
- Definir `MVIContract` como interfaz genérica de contrato para todos los ViewModels.
- Crear tema Compose mínimo (`RickAndMortyTheme`).
- Configurar navegación declarativa con rutas tipadas y `AppNavigation` como composable raíz.
- Implementar componentes transversales reutilizables: `TopAppBar`, `FullScreenLoader`, `BottomLoader`, `ErrorView`.
- Implementar `CharactersViewModel` con patrón MVI, paginación incremental y protección contra doble carga.
- Implementar `CharactersScreen` con lazy list + infinity scroll, pull-to-refresh, big loader inicial, bottom loader de paginación, error con retry en ambos modos.
- Carga de imágenes con Coil: placeholder + fallback de error.
- Tests unitarios de `CharactersViewModel` cubriendo: carga inicial, paginación, retry, refresh.

**Non-Goals:**
- Pantalla de detalle de personaje.
- Cacheo local ni persistencia.
- Animaciones elaboradas o transiciones personalizadas.
- Dark mode / theming avanzado.
- Hilt ViewModel module explícito (Hilt inyecta `@HiltViewModel` automáticamente).

## Decisions

### MVIContract como interfaz con tipos genéricos
```kotlin
interface MVIContract<State, Event, Effect> {
    val uiState: StateFlow<State>
    val effect: SharedFlow<Effect>
    fun onEvent(event: Event)
}
```
Esto fuerza consistencia en todos los ViewModels sin imponer clase base. Se ubica en `presentation/MVIContract.kt`.

### Paginación: estado en el ViewModel, disparada por scroll
El ViewModel mantiene `currentPage` y `hasNextPage` en `UiState`. La screen envía `UiEvent.LoadNextPage` cuando el último ítem visible se acerca al final de la lista. El ViewModel guarda un `isLoadingMore: Boolean` para proteger contra doble carga sin necesidad de locking externo.

```
UiState.characters  → lista acumulada (no se reemplaza en cada página)
UiState.currentPage → página que se cargó por última vez
UiState.hasNextPage → controla si hay más datos
UiState.isLoading   → loader inicial (primera carga o refresh)
UiState.isLoadingMore → bottom loader (páginas 2+)
UiState.error       → error en carga inicial o refresh
UiState.paginationError → error en paginación (página > 1)
```

### Refresh: reinicia estado completo
`UiEvent.Refresh` resetea la lista, vuelve a página 1 y dispara nueva carga. Esto es correcto para pull-to-refresh.

### Retry diferenciado
- `UiEvent.Retry` con `isInitial = true` → reintenta página 1 desde cero.
- `UiEvent.Retry` con `isInitial = false` → reintenta la última página fallida (`currentPage + 1`).

Simplificación: `UiEvent` expone `RetryInitial` y `RetryPagination` como subclases selladas separadas para evitar el flag booleano.

### Navegación: routes como sealed class / object
```kotlin
sealed class AppRoute(val route: String) {
    object Characters : AppRoute("characters")
}
```
`AppNavigation` es un composable que recibe `NavHostController` y configura el `NavHost`. `MainActivity` lo llama dentro del tema.

### Componentes transversales en `presentation/components/`
- `FullScreenLoader`: `Box` centrado con `CircularProgressIndicator`.
- `BottomLoader`: `Row` centrado con `CircularProgressIndicator` de tamaño pequeño.
- `ErrorView(message, onRetry)`: `Column` centrada con texto de error y botón Retry.
- `RickAndMortyTopBar(title)`: `TopAppBar` de Material 3.

Todos son `@Composable` internos sin estado propio.

### Carga de imágenes con Coil
`AsyncImage` de `coil-compose` con `placeholder = painterResource(R.drawable.ic_placeholder)` y `error = painterResource(R.drawable.ic_error)`. Se utilizan drawables vectoriales simples para no depender de assets externos.

## Risks / Trade-offs

- **Lista acumulada en memoria** → Para la escala de esta app (800 personajes totales en la API) es aceptable. Si la lista creciera, se migraría a Paging 3.
- **Pull-to-refresh y paginación simultáneos** → El flag `isLoading` bloquea `LoadNextPage` mientras hay refresh en curso (se verifica en el ViewModel antes de disparar la carga).
- **Placeholders vectoriales** → Requiere crear 2 drawables en `res/drawable/`. Alternativa: usar color sólido como placeholder en Coil (sin drawable), pero reduce la calidad visual.
