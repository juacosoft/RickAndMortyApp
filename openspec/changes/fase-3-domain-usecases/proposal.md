## Why

Las entidades de dominio y la interfaz `CharacterRepository` ya existen (Fase 2), pero no hay ninguna capa de casos de uso que orqueste la lógica de negocio. Sin UseCases, los ViewModels de la Fase 4 dependerían directamente del repositorio, violando Clean Architecture y haciendo imposible testear la lógica de negocio de forma aislada.

## What Changes

- Crear `GetCharactersUseCase` en `domain/usecase/` con `@Inject constructor(CharacterRepository)` que retorna `Result<CharacterPage>` para una página dada.
- No se crea interfaz para el UseCase (lineamiento del proyecto).
- No se crea clase base de UseCase (no hay lógica reutilizable entre casos de uso en esta fase).
- Agregar tests unitarios del UseCase que verifican los escenarios de éxito y fallo.

## Capabilities

### New Capabilities

- `get-characters-usecase`: Caso de uso que encapsula la lógica de obtener personajes paginados desde el repositorio, listo para ser inyectado en ViewModels.

### Modified Capabilities

_(ninguna)_

## Impact

- `app/src/main/java/.../domain/usecase/`: nuevo paquete con `GetCharactersUseCase`.
- `app/src/test/.../domain/usecase/`: tests unitarios del UseCase.
- Sin cambios en `data/`, `di/`, `presentation/`, ni Gradle.
