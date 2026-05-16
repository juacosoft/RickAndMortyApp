# Spec: presentation-navigation

## Purpose
Define the navigation setup for the app using Jetpack Navigation Compose.

## Requirements

### Requirement: AppNavigation configura el NavHost de la app
El proyecto SHALL tener un composable `AppNavigation` en `presentation/navigation/` que recibe un `NavHostController` y configura un `NavHost` con `AppRoute.Characters` como destino inicial. `MainActivity` SHALL llamar a `AppNavigation` dentro del tema.

#### Scenario: Characters es el destino raíz
- **WHEN** la app arranca
- **THEN** `CharactersScreen` es el primer composable visible sin necesidad de navegación explícita

#### Scenario: Rutas definidas como sealed class
- **WHEN** se agrega un nuevo destino de navegación
- **THEN** se añade una subclase de `AppRoute` con su `route: String`, sin strings literales sueltos

### Requirement: AppRoute define todas las rutas de la app
El proyecto SHALL tener una `sealed class AppRoute(val route: String)` en `presentation/navigation/` con al menos el objeto `Characters`.

#### Scenario: Ruta Characters accesible como AppRoute.Characters.route
- **WHEN** se configura el NavHost o se navega programáticamente
- **THEN** se usa `AppRoute.Characters.route` en lugar de un string literal
