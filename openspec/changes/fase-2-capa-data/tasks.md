## 1. Application y Manifest

- [x] 1.1 Crear `RickAndMortyApplication` con `@HiltAndroidApp` en el paquete raíz
- [x] 1.2 Registrar `android:name=".RickAndMortyApplication"` en `AndroidManifest.xml`
- [x] 1.3 Añadir `<uses-permission android:name="android.permission.INTERNET" />` en `AndroidManifest.xml`

## 2. DTOs (`data/remote/dto/`)

- [x] 2.1 Crear `PaginationInfoDto` (`count`, `pages`, `next`, `prev`) con `@Serializable`
- [x] 2.2 Crear `CharacterOriginDto` (`name`, `url`) con `@Serializable`
- [x] 2.3 Crear `CharacterLocationDto` (`name`, `url`) con `@Serializable`
- [x] 2.4 Crear `CharacterDto` (`id`, `name`, `status`, `species`, `type`, `gender`, `origin`, `location`, `image`, `url`) con `@Serializable`
- [x] 2.5 Crear `CharacterResponseDto` (`info: PaginationInfoDto`, `results: List<CharacterDto>`) con `@Serializable`

## 3. Entidades Domain (`domain/`)

- [x] 3.1 Crear `enum class CharacterStatus` con valores `ALIVE`, `DEAD`, `UNKNOWN` en `domain/model/`
- [x] 3.2 Crear `data class Character` con campos `id`, `name`, `status`, `species`, `gender`, `imageUrl`, `originName`, `locationName` en `domain/model/`
- [x] 3.3 Crear `data class CharacterPage` con `characters: List<Character>` y `hasNextPage: Boolean` en `domain/model/`
- [x] 3.4 Crear interfaz `CharacterRepository` con `suspend fun getCharacters(page: Int): Result<CharacterPage>` en `domain/repository/`

## 4. Mapper (`data/mapper/`)

- [x] 4.1 Crear función de extensión `CharacterDto.toDomain(): Character` mapeando todos los campos definidos
- [x] 4.2 Mapear `status` string → `CharacterStatus` con fallback a `UNKNOWN` para valores no reconocidos

## 5. Remote DataSource (`data/remote/datasource/`)

- [x] 5.1 Crear interfaz `CharacterRemoteDataSource` con `suspend fun getCharacters(page: Int): CharacterResponseDto`
- [x] 5.2 Crear `CharacterRemoteDataSourceImpl` que inyecta `HttpClient` y llama `GET /character?page={page}`
- [x] 5.3 Envolver la llamada Ktor en `runCatching`, re-lanzar `CancellationException` explícitamente

## 6. Repository Impl (`data/repository/`)

- [x] 6.1 Crear `CharacterRepositoryImpl` que implementa `CharacterRepository` e inyecta `CharacterRemoteDataSource`
- [x] 6.2 Implementar `getCharacters`: llamar datasource, mapear DTOs con `.toDomain()`, construir `CharacterPage` con `hasNextPage = info.next != null`, envolver en `Result`

## 7. Módulos Hilt (`di/`)

- [x] 7.1 Crear `NetworkModule` (`@Module`, `@InstallIn(SingletonComponent::class)`) que provee `HttpClient` `@Singleton` con Android engine, ContentNegotiation JSON, Logging (ALL en debug / NONE en release)
- [x] 7.2 Crear `DataSourceModule` que vincula `CharacterRemoteDataSourceImpl` → `CharacterRemoteDataSource`
- [x] 7.3 Crear `RepositoryModule` que vincula `CharacterRepositoryImpl` → `CharacterRepository`

## 8. Tests Unitarios (`test/`)

- [x] 8.1 Test `CharacterDtoToDomainMapperTest`: verificar mapeo correcto de todos los campos y fallback de status a `UNKNOWN`
- [x] 8.2 Test `CharacterRepositoryImplTest` con MockK: verificar `Result.success` cuando datasource retorna datos válidos
- [x] 8.3 Test `CharacterRepositoryImplTest`: verificar `hasNextPage = true` cuando `info.next != null`
- [x] 8.4 Test `CharacterRepositoryImplTest`: verificar `hasNextPage = false` cuando `info.next == null`
- [x] 8.5 Test `CharacterRepositoryImplTest`: verificar `Result.failure` cuando datasource lanza excepción

## 9. Verificación de Build

- [x] 9.1 Ejecutar `./gradlew assembleDebug` → `BUILD SUCCESSFUL`
- [x] 9.2 Ejecutar `./gradlew test` → `BUILD SUCCESSFUL`, todos los tests pasan

## 10. Pruebas Manuales

- [x] 10.1 Gradle sync en Android Studio → sin errores
- [x] 10.2 App arranca en emulador/dispositivo sin crash (Hilt inicializado correctamente)
- [x] 10.3 Verificar en Logcat que las peticiones HTTP aparecen bajo el tag `Ktor` al arrancar con un llamado de prueba

## 11. Commit

- [ ] 11.1 Semantic commit: `feat: implement data layer with Ktor remote datasource and character repository`
