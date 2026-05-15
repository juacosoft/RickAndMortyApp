## ADDED Requirements

### Requirement: HttpClient configurado con Ktor Android engine
El proyecto SHALL proveer un único `HttpClient` (Ktor Android engine) con `ContentNegotiation` (JSON kotlinx), `Logging` (Logcat, `LogLevel.ALL` en debug, `LogLevel.NONE` en release) y `ignoreUnknownKeys = true`.

#### Scenario: Singleton provisto por Hilt
- **WHEN** se inyecta `HttpClient` en cualquier componente
- **THEN** Hilt provee la misma instancia (scope `@Singleton`)

#### Scenario: Logging activo solo en debug
- **WHEN** el build es debug
- **THEN** las peticiones y respuestas HTTP se imprimen en Logcat bajo el tag `Ktor`

#### Scenario: Logging inactivo en release
- **WHEN** el build es release
- **THEN** no se imprime ningún log HTTP en Logcat

### Requirement: DTOs serializables que reflejan la respuesta de `/character`
La capa data SHALL tener DTOs `@Serializable` que modelen exactamente la respuesta JSON de `GET /character`: `CharacterResponseDto` (info + results), `PaginationInfoDto`, `CharacterDto`, `CharacterLocationDto`, `CharacterOriginDto`.

#### Scenario: Deserialización exitosa de respuesta completa
- **WHEN** Ktor recibe una respuesta JSON válida de `GET /character`
- **THEN** se deserializa sin excepción a `CharacterResponseDto` con lista de `CharacterDto` y `PaginationInfoDto`

#### Scenario: Campos desconocidos ignorados
- **WHEN** la respuesta JSON contiene campos no definidos en los DTOs (p.ej. `episode`, `type`)
- **THEN** la deserialización no lanza excepción

### Requirement: CharacterRemoteDataSource obtiene personajes paginados
`CharacterRemoteDataSource` SHALL exponer `suspend fun getCharacters(page: Int): CharacterResponseDto` que llama a `GET https://rickandmortyapi.com/api/character?page={page}`.

#### Scenario: Llamada exitosa retorna DTOs
- **WHEN** se llama `getCharacters(page = 1)` y la red está disponible
- **THEN** retorna `CharacterResponseDto` con al menos un `CharacterDto` y `PaginationInfoDto` con `count > 0`

#### Scenario: Error de red propaga excepción
- **WHEN** se llama `getCharacters` y no hay conectividad
- **THEN** lanza una excepción de red (no devuelve datos parciales)

#### Scenario: `CancellationException` no es capturada
- **WHEN** la coroutine es cancelada durante la llamada
- **THEN** la `CancellationException` se propaga correctamente y no queda silenciada

### Requirement: CharacterRemoteDataSource provista via Hilt
`CharacterRemoteDataSource` SHALL ser una interfaz cuya implementación `CharacterRemoteDataSourceImpl` es provista por Hilt en `DataSourceModule`.

#### Scenario: Inyección de la implementación correcta
- **WHEN** Hilt inyecta `CharacterRemoteDataSource`
- **THEN** la instancia concreta es `CharacterRemoteDataSourceImpl`
