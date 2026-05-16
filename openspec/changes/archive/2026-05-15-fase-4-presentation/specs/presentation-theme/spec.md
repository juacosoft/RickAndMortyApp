## ADDED Requirements

### Requirement: RickAndMortyTheme envuelve toda la UI
El proyecto SHALL tener un composable `RickAndMortyTheme` en `presentation/theme/` que aplica `MaterialTheme` con colores, tipografía y formas propios de la app. `MainActivity` SHALL envolver su contenido en `RickAndMortyTheme`.

#### Scenario: Tema aplicado al iniciar la app
- **WHEN** la `MainActivity` arranca
- **THEN** toda la UI renderiza dentro de `RickAndMortyTheme` y los colores de Material 3 están disponibles

#### Scenario: Colores definidos para modo claro
- **WHEN** se usa `MaterialTheme.colorScheme` en cualquier composable
- **THEN** los colores devueltos corresponden al esquema definido en `RickAndMortyTheme`
