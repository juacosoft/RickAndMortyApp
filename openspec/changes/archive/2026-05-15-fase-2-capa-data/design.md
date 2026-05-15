## Context

La capa `data` es la única responsable de comunicarse con la API de Rick and Morty. La Fase 1 ya instaló Ktor, kotlinx.serialization y Hilt con KSP. El proyecto sigue siendo single-module; la separación de responsabilidades se hace por paquete. La capa `domain` no sabe nada de Ktor, DTOs ni HTTP.

## Goals / Non-Goals

**Goals:**
- Configurar `HttpClient` de Ktor listo para producción (serialización JSON, logging en Logcat).
- Modelar la respuesta paginada de `/character` con DTOs `@Serializable`.
- Implementar `CharacterRemoteDataSource` que llama al endpoint y retorna DTOs.
- Implementar `CharacterRepositoryImpl` que transforma DTOs en entidades de dominio.
- Proveer todo via Hilt (módulos de red, datasource, repositorio).
- Añadir `RickAndMortyApplication` con `@HiltAndroidApp` y permiso INTERNET.

**Non-Goals:**
- Cacheo local / Room (next steps).
- Lógica de negocio (UseCase) — Fase 3.
- UI / ViewModel — Fases 4–5.
- Manejo de autenticación (la API es pública).

## Decisions

### Estructura de paquetes dentro de `data/`
```
data/
├── remote/
│   ├── dto/          # CharacterDto, CharacterResponseDto, CharacterLocationDto, CharacterOriginDto, PaginationInfoDto
│   ├── datasource/   # CharacterRemoteDataSource (interfaz) + Impl
│   └── service/      # CharacterApiService (funciones Ktor)
├── repository/       # CharacterRepositoryImpl
└── mapper/           # CharacterDto.toDomain() como funciones de extensión
```

### HttpClient como singleton via Hilt `@Singleton`
Un único `HttpClient` por proceso evita abrir múltiples connection pools. Se provee en `NetworkModule` con scope `@Singleton`.

### Configuración del cliente Ktor
```kotlin
HttpClient(Android) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true; isLenient = true })
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) { Log.d("Ktor", message) }
        }
        level = LogLevel.ALL  // solo en debug; producción usará LogLevel.NONE via BuildConfig
    }
}
```
`ignoreUnknownKeys = true` es obligatorio: la API devuelve campos que no se usan aún (episode, type, etc.).

### DTOs vs Entidades de dominio
Los DTOs son `@Serializable` y reflejan la forma exacta del JSON. Las entidades de dominio (`Character`) son clases simples de Kotlin sin anotaciones de framework. El mapeo ocurre en funciones de extensión `CharacterDto.toDomain()` dentro del paquete `data/mapper/`.

### `CharacterStatus` como sealed class en domain
El campo `status` del API (`"Alive"`, `"Dead"`, `"unknown"`) se mapea a un `enum class CharacterStatus` en domain. El mapper convierte el string en el enum, con fallback a `UNKNOWN` para valores no reconocidos.

### Interfaz `CharacterRepository` en domain
La interfaz vive en `domain/repository/` para que domain no dependa de data. La implementación en `data/repository/` implementa esa interfaz e inyecta `CharacterRemoteDataSource`.

### Paginación
La API retorna `info.next` (URL completa de la siguiente página) e `info.count` (total de ítems). El repositorio expone `suspend fun getCharacters(page: Int): Result<CharacterPage>` donde `CharacterPage` agrupa la lista de `Character` y el flag `hasNextPage`. `page` es 1-indexed, igual que la API.

### Manejo de errores
El datasource envuelve la llamada Ktor en `runCatching` y lanza excepciones tipadas:
- `IOException` / `ConnectTimeoutException` → red no disponible.
- `HttpException` (status != 2xx) → error del servidor.
El repositorio propaga el `Result<T>` sin transformar; la lógica de retry y presentación queda en capas superiores.

### Módulos Hilt
| Módulo | Provee |
|---|---|
| `NetworkModule` | `HttpClient` (`@Singleton`) |
| `DataSourceModule` | `CharacterRemoteDataSource` → `CharacterRemoteDataSourceImpl` |
| `RepositoryModule` | `CharacterRepository` → `CharacterRepositoryImpl` |

Todos en `di/` con `@InstallIn(SingletonComponent::class)`.

## Risks / Trade-offs

- **`ignoreUnknownKeys = true` puede enmascarar campos nuevos de la API** → aceptable en esta fase; si se necesitan nuevos campos se añaden al DTO.
- **LogLevel.ALL en debug expone URLs y payloads en Logcat** → solo habilitado en debug build; en release se usará `LogLevel.NONE` via `if (BuildConfig.DEBUG)`.
- **`runCatching` captura `CancellationException`** → se re-lanza explícitamente para no romper la cancelación de coroutines.
