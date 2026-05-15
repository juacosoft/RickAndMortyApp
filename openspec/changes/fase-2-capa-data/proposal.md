## Why

Con las dependencias y la estructura de capas ya instaladas (Fase 1), el siguiente paso es construir la capa `data`: el único punto de contacto con la API de Rick and Morty. Sin esta capa no hay datos que mostrar ni lógica de dominio que operar.

## What Changes

- Crear el cliente Ktor (`HttpClient`) configurado con Android engine, `ContentNegotiation` + `kotlinx.serialization` JSON y `Logging` (nivel ALL en debug hacia Logcat).
- Crear DTOs kotlinx.serializable que reflejen la respuesta de `GET /character` (paginación + lista de personajes).
- Crear la interfaz `CharacterRemoteDataSource` y su implementación `CharacterRemoteDataSourceImpl` que consume el endpoint.
- Crear la interfaz `CharacterRepository` (en `domain`) y su implementación `CharacterRepositoryImpl` (en `data`) que delega al datasource y mapea DTOs a entidades de dominio.
- Crear funciones de extensión de mapeo `.toDomain()` en el paquete `data`.
- Crear módulos Hilt en `di/` que provean `HttpClient`, `CharacterRemoteDataSource` y `CharacterRepository`.
- Crear `RickAndMortyApplication` con `@HiltAndroidApp` y registrarla en el `AndroidManifest`.

## Capabilities

### New Capabilities

- `remote-data-source`: Configuración del cliente HTTP y acceso al endpoint `/character` con paginación.
- `character-repository`: Implementación del repositorio que mapea datos remotos a entidades de dominio y expone los datos a la capa domain.

### Modified Capabilities

_(ninguna)_

## Impact

- `app/src/main/java/.../data/`: nuevos archivos (DTOs, datasource, repository impl, mappers, módulos Hilt).
- `app/src/main/java/.../domain/`: interfaz `CharacterRepository` + entidad `Character`.
- `app/src/main/java/.../di/`: módulos `NetworkModule`, `DataSourceModule`, `RepositoryModule`.
- `AndroidManifest.xml`: atributo `android:name` apuntando a `RickAndMortyApplication`.
- `app/build.gradle.kts`: permiso `INTERNET` ya no requiere cambios (se añade en manifest).
- Ningún cambio en UI ni en Gradle.
