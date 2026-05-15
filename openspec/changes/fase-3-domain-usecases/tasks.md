## 1. UseCase (`domain/usecase/`)

- [x] 1.1 Crear paquete `domain/usecase/`
- [x] 1.2 Crear `GetCharactersUseCase` con `@Inject constructor(private val repository: CharacterRepository)` y `suspend operator fun invoke(page: Int): Result<CharacterPage>`

## 2. Tests Unitarios (`test/`)

- [x] 2.1 Crear `GetCharactersUseCaseTest`: verificar que `invoke(page)` retorna `Result.success` cuando el repositorio retorna éxito
- [x] 2.2 Crear `GetCharactersUseCaseTest`: verificar que `invoke(page)` retorna `Result.failure` cuando el repositorio lanza excepción
- [x] 2.3 Verificar que el UseCase propaga el resultado sin transformar (mismo objeto)

## 3. Verificación de Build

- [x] 3.1 Ejecutar `./gradlew assembleDebug` → `BUILD SUCCESSFUL`
- [x] 3.2 Ejecutar `./gradlew test` → `BUILD SUCCESSFUL`, todos los tests pasan

## 4. Pruebas Manuales

- [x] 4.1 Gradle sync en Android Studio → sin errores

## 5. Commit

- [x] 5.1 Semantic commit: `feat: add GetCharactersUseCase in domain layer`
