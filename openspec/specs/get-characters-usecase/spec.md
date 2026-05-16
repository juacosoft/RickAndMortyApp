# Spec: get-characters-usecase

## Purpose
Define the requirements for the `GetCharactersUseCase` domain use case.

## Requirements

### Requirement: GetCharactersUseCase delega en CharacterRepository
`GetCharactersUseCase` SHALL ser una clase concreta en `domain/usecase/` con `@Inject constructor(private val repository: CharacterRepository)` que expone `suspend operator fun invoke(page: Int): Result<CharacterPage>` delegando directamente en `repository.getCharacters(page)`.

#### Scenario: Invocación retorna resultado del repositorio en éxito
- **WHEN** `repository.getCharacters(page)` retorna `Result.success(CharacterPage(...))`
- **THEN** `GetCharactersUseCase(page)` retorna el mismo `Result.success` sin transformar

#### Scenario: Invocación retorna resultado del repositorio en fallo
- **WHEN** `repository.getCharacters(page)` retorna `Result.failure(exception)`
- **THEN** `GetCharactersUseCase(page)` retorna el mismo `Result.failure` sin transformar

#### Scenario: No tiene interfaz propia
- **WHEN** se revisa el paquete `domain/usecase/`
- **THEN** solo existe `GetCharactersUseCase.kt` sin una interfaz `IGetCharactersUseCase` o similar

#### Scenario: Inyectable por Hilt sin módulo extra
- **WHEN** un ViewModel declara `@Inject constructor(private val useCase: GetCharactersUseCase)`
- **THEN** Hilt puede proveer la instancia sin necesitar un módulo Hilt adicional (gracias a `@Inject constructor` en el UseCase)
