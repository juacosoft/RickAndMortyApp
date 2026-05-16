# Spec: mvi-contract

## Purpose
Define the MVI contract interface that all ViewModels in the presentation layer must implement.

## Requirements

### Requirement: MVIContract define el contrato de todos los ViewModels
El proyecto SHALL tener una interfaz `MVIContract<State, Event, Effect>` en `presentation/MVIContract.kt` con tres miembros: `val uiState: StateFlow<State>`, `val effect: SharedFlow<Effect>`, y `fun onEvent(event: Event)`. Todo ViewModel de presentación SHALL implementar esta interfaz.

#### Scenario: ViewModel expone uiState como StateFlow
- **WHEN** un composable observa el ViewModel
- **THEN** puede colectar `uiState` como `StateFlow` sin casting adicional

#### Scenario: ViewModel expone effect como SharedFlow
- **WHEN** el ViewModel emite un efecto one-shot (navegación, snackbar)
- **THEN** el composable lo recibe exactamente una vez via `SharedFlow`

#### Scenario: UI envía eventos al ViewModel via onEvent
- **WHEN** el composable llama `viewModel.onEvent(event)`
- **THEN** el ViewModel procesa el evento y actualiza el estado o emite un efecto
