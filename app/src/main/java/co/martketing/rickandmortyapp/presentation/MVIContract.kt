package co.martketing.rickandmortyapp.presentation

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MVIContract<State, Event, Effect> {
    val uiState: StateFlow<State>
    val effect: SharedFlow<Effect>
    fun onEvent(event: Event)
}
