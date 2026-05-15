# Spec: character-repository

## Purpose

Defines the domain entity `Character`, the `CharacterRepository` contract, and its data-layer implementation including DTO mapping and Hilt wiring.

## Requirements

### Requirement: Entidad Character en domain
La capa domain SHALL tener una data class `Character` con los campos: `id`, `name`, `status: CharacterStatus`, `species`, `gender`, `imageUrl`, `originName`, `locationName`. El enum `CharacterStatus` SHALL tener valores `ALIVE`, `DEAD`, `UNKNOWN`.

#### Scenario: Campos mínimos para la pantalla Characters
- **WHEN** se usa la entidad `Character` en la capa presentation
- **THEN** están disponibles `id`, `name`, `status`, `imageUrl` sin necesidad de acceder a DTOs

#### Scenario: Status desconocido mapea a UNKNOWN
- **WHEN** el string de status del API no es `"Alive"` ni `"Dead"`
- **THEN** `CharacterStatus` es `UNKNOWN`

### Requirement: Interfaz CharacterRepository en domain
La capa domain SHALL tener la interfaz `CharacterRepository` con `suspend fun getCharacters(page: Int): Result<CharacterPage>`. `CharacterPage` SHALL contener `characters: List<Character>` y `hasNextPage: Boolean`.

#### Scenario: Contrato accesible sin dependencia de data
- **WHEN** un UseCase de domain importa `CharacterRepository`
- **THEN** no se importa ninguna clase del paquete `data`

### Requirement: CharacterRepositoryImpl mapea DTOs a entidades de dominio
`CharacterRepositoryImpl` SHALL implementar `CharacterRepository`, delegar en `CharacterRemoteDataSource` y retornar `Result<CharacterPage>` con entidades `Character` mapeadas.

#### Scenario: Resultado exitoso contiene entidades mapeadas
- **WHEN** el datasource retorna `CharacterResponseDto` válido con N personajes
- **THEN** `getCharacters` retorna `Result.success(CharacterPage)` con N `Character` y `hasNextPage` correcto

#### Scenario: `hasNextPage = true` cuando hay página siguiente
- **WHEN** `PaginationInfoDto.next` no es null
- **THEN** `CharacterPage.hasNextPage` es `true`

#### Scenario: `hasNextPage = false` en la última página
- **WHEN** `PaginationInfoDto.next` es null
- **THEN** `CharacterPage.hasNextPage` es `false`

#### Scenario: Error del datasource se envuelve en Result.failure
- **WHEN** el datasource lanza una excepción
- **THEN** `getCharacters` retorna `Result.failure` con la excepción original

### Requirement: Mapeo DTO → dominio mediante funciones de extensión
El mapeo de `CharacterDto` a `Character` SHALL realizarse mediante funciones de extensión `CharacterDto.toDomain()` en el paquete `data/mapper/`.

#### Scenario: Mapeo preserva todos los campos definidos
- **WHEN** se llama `characterDto.toDomain()`
- **THEN** el `Character` resultante tiene `id`, `name`, `status`, `species`, `gender`, `imageUrl`, `originName`, `locationName` con los valores del DTO

### Requirement: CharacterRepository provista via Hilt
`CharacterRepository` SHALL ser la interfaz inyectada por Hilt; la implementación concreta `CharacterRepositoryImpl` es vinculada en `RepositoryModule`.

#### Scenario: Inyección de la implementación correcta
- **WHEN** Hilt inyecta `CharacterRepository`
- **THEN** la instancia concreta es `CharacterRepositoryImpl`

### Requirement: RickAndMortyApplication con HiltAndroidApp
La aplicación SHALL tener una clase `RickAndMortyApplication` anotada con `@HiltAndroidApp` registrada en `AndroidManifest.xml` y el permiso `INTERNET` declarado.

#### Scenario: Hilt inicializado al arrancar la app
- **WHEN** la aplicación arranca en un dispositivo/emulador
- **THEN** no se lanza `IllegalStateException` por falta de componente Hilt

#### Scenario: Permiso INTERNET declarado
- **WHEN** se revisa `AndroidManifest.xml`
- **THEN** existe `<uses-permission android:name="android.permission.INTERNET" />`
