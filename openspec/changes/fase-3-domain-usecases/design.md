## Context

La capa domain ya tiene `Character`, `CharacterPage`, `CharacterStatus` y la interfaz `CharacterRepository`. El repositorio expone `suspend fun getCharacters(page: Int): Result<CharacterPage>`. El UseCase es el único artefacto pendiente en esta capa.

## Goals / Non-Goals

**Goals:**
- Implementar `GetCharactersUseCase` como clase concreta con `@Inject constructor`.
- El UseCase delega en `CharacterRepository` sin añadir lógica adicional en esta fase (la validación de página, retry y paginación se manejan en el ViewModel / presentation).
- Testear el UseCase aisladamente mockeando `CharacterRepository`.

**Non-Goals:**
- Crear interfaz para el UseCase (lineamiento explícito del proyecto).
- Clase base `UseCase` (no hay lógica compartida entre UseCases en esta fase).
- Otros UseCases como `GetCharacterDetailUseCase` (fuera del alcance de las fases actuales).
- Lógica de filtrado, búsqueda o transformación adicional (no requerida aún).

## Decisions

### Firma del operador `invoke`
El UseCase expone un operador `invoke` para que el ViewModel lo llame como función:

```kotlin
class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(page: Int): Result<CharacterPage> =
        repository.getCharacters(page)
}
```

Esto permite al ViewModel escribir `getCharactersUseCase(page)` en lugar de `getCharactersUseCase.execute(page)`, siendo idiomático en Kotlin.

### Sin clase base UseCase
La guía del proyecto dice "crear clase base UseCase solo si es necesario reutilizar lógica". Con un único UseCase en esta fase, no hay lógica común que centralizar.

### Ubicación: `domain/usecase/`
Paquete plano dentro de domain; si en el futuro hay muchos UseCases se puede subdividir por feature.

### Testing
El test mockea `CharacterRepository` con MockK y verifica que el UseCase propaga correctamente el `Result` del repositorio — tanto éxito como fallo — sin alterar el valor.

## Risks / Trade-offs

- **UseCase trivial sin lógica propia** → válido en esta fase; su valor está en desacoplar ViewModel del repositorio y en ser el punto de extensión natural cuando se agregue lógica (validación, caché, etc.).
